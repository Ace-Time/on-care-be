package org.ateam.oncare.beneficiary.query.mapper;

import org.ateam.oncare.beneficiary.command.mapper.CareLevelExpirationCommandMapper;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.NoticeExpirationListResponse;
import org.ateam.oncare.beneficiary.query.mapper.CareLevelExpirationQueryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional // 테스트 종료 시 롤백 (실 DB에 반영 안 됨)
class CareLevelExpirationMapperTest {

    @Autowired
    private CareLevelExpirationCommandMapper commandMapper;

    @Autowired
    private CareLevelExpirationQueryMapper queryMapper;

    /**
     * 연장예정 상태 변경 → 상세조회로 검증
     */
    @Test
    @DisplayName("연장예정 상태 변경(updateExtendsStatus) 후 상세조회에 반영되어야 한다")
    void updateExtendsStatus_thenDetailReflects() {
        // Given
        Integer expirationId = 1; // dev DB에 실제 존재하는 id 사용
        String newExtendsStatus = "N";

        // When
        int updated = commandMapper.updateExtendsStatus(expirationId, newExtendsStatus);

        // Then
        assertThat(updated).isEqualTo(1);

        CareLevelExpirationDetailResponse detail =
                queryMapper.selectExpirationDetail(expirationId);

        System.out.println("=== updateExtendsStatus TEST ===");
        System.out.println("expirationId  = " + expirationId);
        System.out.println("extendsStatus = " + detail.getExtendsStatus());
        System.out.println("===============================");

        assertThat(detail).isNotNull();
        assertThat(detail.getExtendsStatus()).isEqualTo(newExtendsStatus);
    }

    /**
     * 안내유무 상태 변경 → 상세조회로 검증
     */
    @Test
    @DisplayName("안내유무 상태 변경(updateOutboundStatus) 후 상세조회에 반영되어야 한다")
    void updateOutboundStatus_thenDetailReflects() {
        // Given
        Integer expirationId = 1;
        String newOutboundStatus = "Y";

        // When
        int updated = commandMapper.updateOutboundStatus(expirationId, newOutboundStatus);

        // Then
        assertThat(updated).isEqualTo(1);

        CareLevelExpirationDetailResponse detail =
                queryMapper.selectExpirationDetail(expirationId);

        System.out.println("=== updateOutboundStatus TEST ===");
        System.out.println("expirationId   = " + expirationId);
        System.out.println("outboundStatus = " + detail.getOutboundStatus());
        System.out.println("================================");

        assertThat(detail).isNotNull();
        assertThat(detail.getOutboundStatus()).isEqualTo(newOutboundStatus);
    }

    /**
     * 안내이력 등록 → 최신 memo 조회 + 이력 목록 조회
     */
    @Test
    @DisplayName("안내이력 등록(insertNotice) 후 최신 memo와 안내이력 목록에 반영되어야 한다")
    void insertNotice_thenQueryNoticeListAndLatestMemo() {
        // Given
        Integer expirationId = 1;
        Integer empId = 1;
        String noticeDate = "2026-01-06 10:00:00";
        String memo = "안내이력 INSERT 테스트 메모";

        // When
        int inserted = commandMapper.insertNotice(expirationId, noticeDate, memo, empId);

        // Then
        assertThat(inserted).isEqualTo(1);

        String latestMemo = commandMapper.selectLatestNoticeMemo(expirationId);
        List<NoticeExpirationListResponse.Item> list =
                queryMapper.selectNoticeList(expirationId);

        System.out.println("=== insertNotice TEST ===");
        System.out.println("latestMemo = " + latestMemo);
        System.out.println("noticeCount = " + list.size());
        list.forEach(n ->
                System.out.println(" - noticeId=" + n.getNoticeId()
                        + ", noticeDate=" + n.getNoticeDate()
                        + ", memo=" + n.getMemo()
                        + ", empName=" + n.getEmpName())
        );
        System.out.println("========================");

        assertThat(latestMemo).isEqualTo(memo);
        assertThat(list.stream().anyMatch(n -> memo.equals(n.getMemo()))).isTrue();
    }

    /**
     * 안내이력 수정 → 최신 memo 변경 여부 검증
     */
    @Test
    @DisplayName("안내이력 수정(updateNotice) 후 최신 memo가 변경되어야 한다")
    void updateNotice_thenLatestMemoUpdated() {
        // Given
        Integer expirationId = 1;
        Integer empId = 1;

        // 먼저 이력 1건 생성
        commandMapper.insertNotice(
                expirationId,
                "2026-01-06 09:00:00",
                "수정 전 메모",
                empId
        );

        List<NoticeExpirationListResponse.Item> beforeList =
                queryMapper.selectNoticeList(expirationId);

        Integer noticeId = beforeList.get(0).getNoticeId();
        String updatedMemo = "수정 후 메모";

        // When
        int updated = commandMapper.updateNotice(
                expirationId,
                noticeId,
                "2026-01-06 10:30:00",
                updatedMemo,
                empId
        );

        // Then
        assertThat(updated).isEqualTo(1);

        String latestMemo = commandMapper.selectLatestNoticeMemo(expirationId);

        System.out.println("=== updateNotice TEST ===");
        System.out.println("noticeId   = " + noticeId);
        System.out.println("latestMemo = " + latestMemo);
        System.out.println("========================");

        assertThat(latestMemo).isEqualTo(updatedMemo);
    }

    /**
     * 안내이력 삭제 → 목록에서 제거되었는지 검증
     */
    @Test
    @DisplayName("안내이력 삭제(deleteNotice) 후 해당 이력이 목록에서 제거되어야 한다")
    void deleteNotice_thenNoticeRemoved() {
        // Given
        Integer expirationId = 1;
        Integer empId = 1;

        // 삭제 대상 이력 생성
        commandMapper.insertNotice(
                expirationId,
                "2026-01-06 11:00:00",
                "삭제 대상 메모",
                empId
        );

        List<NoticeExpirationListResponse.Item> beforeList =
                queryMapper.selectNoticeList(expirationId);

        Integer deleteNoticeId = beforeList.get(0).getNoticeId();

        // When
        int deleted = commandMapper.deleteNotice(expirationId, deleteNoticeId);

        // Then
        assertThat(deleted).isEqualTo(1);

        List<NoticeExpirationListResponse.Item> afterList =
                queryMapper.selectNoticeList(expirationId);

        System.out.println("=== deleteNotice TEST ===");
        System.out.println("deletedNoticeId = " + deleteNoticeId);
        System.out.println("afterCount     = " + afterList.size());
        System.out.println("========================");

        assertThat(afterList.stream()
                .noneMatch(n -> deleteNoticeId.equals(n.getNoticeId())))
                .isTrue();
    }
}
