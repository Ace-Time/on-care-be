package org.ateam.oncare.customer.query.service;

import org.ateam.oncare.customer.query.dto.CustomerManageDTO;
import org.ateam.oncare.customer.query.dto.CustomerManageDTO.*; // Inner Class import
import org.ateam.oncare.customer.query.mapper.CustomerManageMapper;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CustomerManageQueryServiceTest {

    @InjectMocks
    private CustomerManageQueryService customerManageQueryService;

    @Mock
    private CustomerManageMapper customerManageMapper;

    /**
     * 1. 목록 조회 및 페이징 계산 테스트
     * 시나리오: 전체 데이터가 15개이고 페이지 크기가 10일 때, 총 페이지 수가 2페이지로 계산되는지 확인
     */
    @Test
    @DisplayName("수급자 목록 조회 - 페이징 계산 로직 검증")
    void getBeneficiaryList_PaginationLogic() {
        // given
        SearchCondition condition = new SearchCondition();
        condition.setPage(0);
        condition.setSize(10);

        List<BeneficiaryListItem> mockList = List.of(new BeneficiaryListItem(), new BeneficiaryListItem());
        long totalElements = 15L; // 15개 데이터 가정

        given(customerManageMapper.selectBeneficiaryList(condition)).willReturn(mockList);
        given(customerManageMapper.countBeneficiaryList(condition)).willReturn(totalElements);

        // when
        PageResponse<BeneficiaryListItem> response = customerManageQueryService.getBeneficiaryList(condition);

        // then
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(15L);
        assertThat(response.getTotalPages()).isEqualTo(2); // 15 / 10 = 1.5 -> 올림 2
    }

    /**
     * 2. 상세 조회 및 추가 정보 로딩 테스트
     * 시나리오: 불만(Complaint), 해지(Termination) 이력이 있는 경우 추가 쿼리가 실행되어 데이터가 합쳐지는지 확인
     */
    @Test
    @DisplayName("상세 조회 - 플래그(Flag)에 따른 추가 정보 매핑 확인")
    void getCustomerManageDetail_WithAdditionalInfo() {
        // given
        Long beneficiaryId = 1L;
        CustomerManageDetail mockDetail = new CustomerManageDetail();
        mockDetail.setHasComplaint(true);     // 불만 있음
        mockDetail.setHasTermination(true);   // 해지 상담 있음
        mockDetail.setHasRentalCounsel(false); // 렌탈 없음

        CounselSummary mockComplaint = new CounselSummary();
        mockComplaint.setSummary("불만 내용");

        CounselSummary mockTermination = new CounselSummary();
        mockTermination.setSummary("해지 내용");

        // Mocking
        given(customerManageMapper.selectCustomerManageDetail(beneficiaryId)).willReturn(mockDetail);
        given(customerManageMapper.selectLatestComplaint(beneficiaryId)).willReturn(mockComplaint);
        given(customerManageMapper.selectLatestTermination(beneficiaryId)).willReturn(mockTermination);
        // 렌탈은 false이므로 Mocking 불필요 (호출 안됨)

        // when
        CustomerManageDetail result = customerManageQueryService.getCustomerManageDetail(beneficiaryId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getLatestComplaint()).isEqualTo(mockComplaint); // 매핑 확인
        assertThat(result.getLatestTermination()).isEqualTo(mockTermination); // 매핑 확인
        assertThat(result.getLatestRentalCounsel()).isNull(); // 호출 안됨 확인

        // 불필요한 쿼리가 나가지 않았는지 검증
        verify(customerManageMapper, times(0)).selectLatestRentalCounsel(beneficiaryId);
    }

    /**
     * 3. 예외 테스트
     * 시나리오: 조회하려는 수급자 ID가 없을 경우 예외 발생
     */
    @Test
    @DisplayName("상세 조회 실패 - 존재하지 않는 수급자 ID")
    void getCustomerManageDetail_NotFound() {
        // given
        Long invalidId = 999L;

        given(customerManageMapper.selectCustomerManageDetail(invalidId)).willReturn(null);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerManageQueryService.getCustomerManageDetail(invalidId);
        });

        assertThat(exception.getMessage()).isEqualTo("수급자를 찾을 수 없습니다: " + invalidId);
    }
}