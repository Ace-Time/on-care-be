package org.ateam.oncare.beneficiary.query;

import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryDetailResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryDetailMapper;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryDetailServiceTest {

    @Mock
    private BeneficiaryDetailMapper beneficiaryDetailMapper;

    @InjectMocks
    private BeneficiaryDetailService service;

    @Test
    @DisplayName("getDetail: mapper에서 조회한 상세를 그대로 반환한다")
    void getDetail_shouldReturnMapperResult() {
        // Given
        Long beneficiaryId = 123L;

        BeneficiaryDetailResponse mockDetail = new BeneficiaryDetailResponse();
        // 필요하면 mockDetail에 필드 값 세팅 (setter가 있다면)
        // mockDetail.setName("홍길동");

        when(beneficiaryDetailMapper.selectBeneficiaryDetail(beneficiaryId))
                .thenReturn(mockDetail);

        // When
        BeneficiaryDetailResponse res = service.getDetail(beneficiaryId);

        // Then
        assertThat(res).isSameAs(mockDetail);
        verify(beneficiaryDetailMapper, times(1)).selectBeneficiaryDetail(beneficiaryId);
        verifyNoMoreInteractions(beneficiaryDetailMapper);
    }

    @Test
    @DisplayName("getDetail: 데이터가 없으면 null을 그대로 반환한다")
    void getDetail_whenNoData_shouldReturnNull() {
        // Given
        Long beneficiaryId = 999L;

        when(beneficiaryDetailMapper.selectBeneficiaryDetail(beneficiaryId))
                .thenReturn(null);

        // When
        BeneficiaryDetailResponse res = service.getDetail(beneficiaryId);

        // Then
        assertThat(res).isNull();
        verify(beneficiaryDetailMapper, times(1)).selectBeneficiaryDetail(beneficiaryId);
        verifyNoMoreInteractions(beneficiaryDetailMapper);
    }
}
