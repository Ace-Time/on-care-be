package org.ateam.oncare.beneficiary.query;

import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationListResponse;
import org.ateam.oncare.beneficiary.query.dto.response.NoticeExpirationListResponse;
import org.ateam.oncare.beneficiary.query.mapper.CareLevelExpirationQueryMapper;
import org.ateam.oncare.beneficiary.query.service.CareLevelExpirationQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareLevelExpirationQueryServiceTest {

    @Mock
    private CareLevelExpirationQueryMapper mapper;

    @InjectMocks
    private CareLevelExpirationQueryService service;

    @Test
    @DisplayName("만료 예정 전체조회: mapper 결과가 응답 items에 그대로 담긴다")
    void getExpirationList_shouldWrapItems() {
        Integer section = 1;           // 90일
        String extendsStatus = "N";    // 미연장

        CareLevelExpirationListResponse.Item item1 = new CareLevelExpirationListResponse.Item();
        CareLevelExpirationListResponse.Item item2 = new CareLevelExpirationListResponse.Item();

        when(mapper.selectExpirationList(section, extendsStatus))
                .thenReturn(List.of(item1, item2));

        CareLevelExpirationListResponse res = service.getExpirationList(section, extendsStatus);

        assertThat(res).isNotNull();
        assertThat(res.getItems()).hasSize(2);
        assertThat(res.getItems().get(0)).isSameAs(item1);
        assertThat(res.getItems().get(1)).isSameAs(item2);

        verify(mapper, times(1)).selectExpirationList(section, extendsStatus);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("만료 상세조회: mapper 결과를 그대로 반환한다")
    void getExpirationDetail_shouldReturnMapperResult() {
        Integer expirationId = 10;

        CareLevelExpirationDetailResponse mockDetail = new CareLevelExpirationDetailResponse();
        when(mapper.selectExpirationDetail(expirationId)).thenReturn(mockDetail);

        CareLevelExpirationDetailResponse res = service.getExpirationDetail(expirationId);

        assertThat(res).isSameAs(mockDetail);
        verify(mapper, times(1)).selectExpirationDetail(expirationId);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("안내이력 목록: mapper 결과가 응답 items에 그대로 담긴다")
    void getNoticeList_shouldWrapItems() {
        Integer expirationId = 10;

        NoticeExpirationListResponse.Item n1 = new NoticeExpirationListResponse.Item();
        NoticeExpirationListResponse.Item n2 = new NoticeExpirationListResponse.Item();

        when(mapper.selectNoticeList(expirationId)).thenReturn(List.of(n1, n2));

        NoticeExpirationListResponse res = service.getNoticeList(expirationId);

        assertThat(res).isNotNull();
        assertThat(res.getItems()).hasSize(2);
        assertThat(res.getItems().get(0)).isSameAs(n1);
        assertThat(res.getItems().get(1)).isSameAs(n2);

        verify(mapper, times(1)).selectNoticeList(expirationId);
        verifyNoMoreInteractions(mapper);
    }
}
