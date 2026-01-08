package org.ateam.oncare.beneficiary.command;

import org.ateam.oncare.beneficiary.command.dto.request.CreateNoticeRequest;
import org.ateam.oncare.beneficiary.command.dto.request.UpdateExtendsStatusRequest;
import org.ateam.oncare.beneficiary.command.dto.request.UpdateNoticeRequest;
import org.ateam.oncare.beneficiary.command.mapper.CareLevelExpirationCommandMapper;
import org.ateam.oncare.beneficiary.command.service.CareLevelExpirationCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CareLevelExpirationCommandServiceTest {

    @Mock
    private CareLevelExpirationCommandMapper mapper;

    @InjectMocks
    private CareLevelExpirationCommandService service;

    @Test
    @DisplayName("updateExtendsStatus: 요청값을 mapper로 그대로 전달한다")
    void updateExtendsStatus_shouldCallMapper() {
        Integer expirationId = 10;

        UpdateExtendsStatusRequest req = new UpdateExtendsStatusRequest();
        req.setExtendsStatus("N");

        when(mapper.updateExtendsStatus(expirationId, "N")).thenReturn(1);

        int updated = service.updateExtendsStatus(expirationId, req);

        assertThat(updated).isEqualTo(1);
        verify(mapper, times(1)).updateExtendsStatus(expirationId, "N");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("recordAbsent: 자동문구로 insert 후 최신 memo가 자동문구면 outbound_status는 N으로 갱신된다")
    void recordAbsent_shouldInsertAndSetOutboundN_whenLatestIsAutoAbsentMemo() {
        Integer expirationId = 1;
        Integer empId = 2;
        String noticeDate = "2026-01-06 10:00:00";

        // insert는 성공했다고 가정(리턴값 안 쓰므로 stubbing 불필요)
        // refreshOutboundStatus에서 최신 메모 조회
        when(mapper.selectLatestNoticeMemo(expirationId))
                .thenReturn("연락 시도했으나 부재중. 추후 재연락 필요.");

        service.recordAbsent(expirationId, empId, noticeDate);

        verify(mapper, times(1)).insertNotice(
                eq(expirationId),
                eq(noticeDate),
                eq("연락 시도했으나 부재중. 추후 재연락 필요."),
                eq(empId)
        );

        verify(mapper, times(1)).selectLatestNoticeMemo(expirationId);
        verify(mapper, times(1)).updateOutboundStatus(expirationId, "N");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("completeNotice: 사용자 메모로 insert 후 최신 memo가 자동문구가 아니면 outbound_status는 Y로 갱신된다")
    void completeNotice_shouldInsertAndSetOutboundY_whenLatestIsNormalMemo() {
        Integer expirationId = 1;

        CreateNoticeRequest req = new CreateNoticeRequest();
        req.setEmpId(7);
        req.setNoticeDate("2026-01-06 10:00:00");
        req.setMemo("보호자와 통화 완료");

        when(mapper.selectLatestNoticeMemo(expirationId)).thenReturn("보호자와 통화 완료");

        service.completeNotice(expirationId, req);

        verify(mapper, times(1)).insertNotice(expirationId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
        verify(mapper, times(1)).selectLatestNoticeMemo(expirationId);
        verify(mapper, times(1)).updateOutboundStatus(expirationId, "Y");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("completeNotice: 최신 memo가 null이면 outbound_status는 N으로 갱신된다")
    void completeNotice_shouldSetOutboundN_whenLatestMemoNull() {
        Integer expirationId = 1;

        CreateNoticeRequest req = new CreateNoticeRequest();
        req.setEmpId(7);
        req.setNoticeDate("2026-01-06 10:00:00");
        req.setMemo("아무거나");

        when(mapper.selectLatestNoticeMemo(expirationId)).thenReturn(null);

        service.completeNotice(expirationId, req);

        verify(mapper).insertNotice(expirationId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
        verify(mapper).selectLatestNoticeMemo(expirationId);
        verify(mapper).updateOutboundStatus(expirationId, "N");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("updateNotice: 수정 성공(>0)이면 outbound_status를 재계산한다")
    void updateNotice_shouldRefreshOutbound_whenUpdated() {
        Integer expirationId = 1;
        Integer noticeId = 10;

        UpdateNoticeRequest req = new UpdateNoticeRequest();
        req.setEmpId(3);
        req.setNoticeDate("2026-01-06 10:30:00");
        req.setMemo("수정된 메모");

        when(mapper.updateNotice(expirationId, noticeId, req.getNoticeDate(), req.getMemo(), req.getEmpId()))
                .thenReturn(1);
        when(mapper.selectLatestNoticeMemo(expirationId)).thenReturn("수정된 메모");

        int updated = service.updateNotice(expirationId, noticeId, req);

        assertThat(updated).isEqualTo(1);

        verify(mapper, times(1)).updateNotice(expirationId, noticeId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
        verify(mapper, times(1)).selectLatestNoticeMemo(expirationId);
        verify(mapper, times(1)).updateOutboundStatus(expirationId, "Y");
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("updateNotice: 수정 실패(0)이면 outbound_status 재계산을 하지 않는다")
    void updateNotice_shouldNotRefreshOutbound_whenNotUpdated() {
        Integer expirationId = 1;
        Integer noticeId = 10;

        UpdateNoticeRequest req = new UpdateNoticeRequest();
        req.setEmpId(3);
        req.setNoticeDate("2026-01-06 10:30:00");
        req.setMemo("수정된 메모");

        when(mapper.updateNotice(expirationId, noticeId, req.getNoticeDate(), req.getMemo(), req.getEmpId()))
                .thenReturn(0);

        int updated = service.updateNotice(expirationId, noticeId, req);

        assertThat(updated).isEqualTo(0);

        verify(mapper, times(1)).updateNotice(expirationId, noticeId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
        verify(mapper, never()).selectLatestNoticeMemo(anyInt());
        verify(mapper, never()).updateOutboundStatus(anyInt(), anyString());
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("deleteNotice: 삭제 성공(>0)이면 outbound_status를 재계산한다")
    void deleteNotice_shouldRefreshOutbound_whenDeleted() {
        Integer expirationId = 1;
        Integer noticeId = 10;

        when(mapper.deleteNotice(expirationId, noticeId)).thenReturn(1);
        when(mapper.selectLatestNoticeMemo(expirationId)).thenReturn("보호자와 통화 완료"); // 자동문구 아님

        int deleted = service.deleteNotice(expirationId, noticeId);

        assertThat(deleted).isEqualTo(1);

        verify(mapper, times(1)).deleteNotice(expirationId, noticeId);
        verify(mapper, times(1)).selectLatestNoticeMemo(expirationId);
        verify(mapper, times(1)).updateOutboundStatus(expirationId, "Y");
        verifyNoMoreInteractions(mapper);
    }
}
