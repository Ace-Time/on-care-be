package org.ateam.oncare.counsel.query.mapper;

import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CounselQueryMapperTest {

    @Autowired
    private CounselQueryMapper counselQueryMapper;

    @Test
    @DisplayName("전체 조회 시 이탈 고객(status=0)은 카테고리가 '이탈고객'으로 매핑되어야 한다")
    void testFindAllCustomers_Churned() {
        // Given
        // 3번 회원은 기존 고객 중 status가 0인 고객
        long churnedCustomerId = 3L;
        String expectedName = "박종원";

        // When
        List<CustomerListResponse> result = counselQueryMapper.findAllCustomers();

        // Then
        // 1. 이탈 대상자로 설정한 수급자를 ID로 찾는다.
        CustomerListResponse target = result.stream()
                .filter(c ->
                        // 1. ID 일치 확인 (BigInteger vs long)
                        c.getCustomerId().equals(java.math.BigInteger.valueOf(churnedCustomerId))
                                &&
                                // 2. 이름 일치 확인 (중복 방지)
                                expectedName.equals(c.getName())
                )
                .findFirst()
                .orElse(null);

        assertThat(target).isNotNull();

        // 2. DTO에 status 필드가 없으므로, 결과값인 카테고리 명칭으로 로직 적용 여부를 검증한다.
        assertThat(target.getCustomerCategoryName())
                .as("Status가 0인 고객은 이탈고객으로 분류되어야 함")
                .isEqualTo("이탈고객");
    }

    @Test
    @DisplayName("전화번호 검색 시 하이픈을 빼고 입력해도 검색되어야 한다")
    void testSearchByPhone() {
        // Given
        String keyword = "0101111";

        // When
        List<CustomerListResponse> result = counselQueryMapper.searchCustomersByPhone(keyword);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPhone().replaceAll("-", "")).contains(keyword);
    }

    @Test
    @DisplayName("이탈고객이 잠재고객인지 기존고객인지 정확히 구분돼야한다.")
    void testCustomerType() {
        // Given
        // DML 기준: '박종원'(ID 3)은 beneficiary 테이블의 status=0 (이탈한 기존고객)
        // DML 기준: '최순자'(ID 4)는 potential_customer 테이블의 willingness='N' (이탈한 잠재고객)
        String churnedBeneficiaryName = "박종원";
        String churnedPotentialName = "최순자";

        // When
        // Mapper의 이름 검색 기능 사용
        List<CustomerListResponse> beneficiaryResult = counselQueryMapper.searchCustomersByName(churnedBeneficiaryName);
        List<CustomerListResponse> potentialResult = counselQueryMapper.searchCustomersByName(churnedPotentialName);

        // Then
        // 1. 이탈 수급자 검증
        CustomerListResponse targetBeneficiary = beneficiaryResult.stream()
                .filter(c -> c.getName().equals(churnedBeneficiaryName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("테스트 데이터 '박종원'을 찾을 수 없습니다."));

        assertThat(targetBeneficiary.getCustomerCategoryName()).isEqualTo("이탈고객");
        // beneficiary 테이블에서 왔으므로 'beneficiary'여야 함
        assertThat(targetBeneficiary.getCustomerType())
                .as("수급자 테이블 데이터는 customerType이 beneficiary여야 합니다.")
                .isEqualTo("beneficiary");

        // 2. 이탈 잠재고객 검증
        CustomerListResponse targetPotential = potentialResult.stream()
                .filter(c -> c.getName().equals(churnedPotentialName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("테스트 데이터 '최순자'를 찾을 수 없습니다."));

        assertThat(targetPotential.getCustomerCategoryName()).isEqualTo("이탈고객");
        // potential_customer 테이블에서 왔으므로 'potential'이어야 함
        assertThat(targetPotential.getCustomerType())
                .as("잠재고객 테이블 데이터는 customerType이 potential이어야 합니다.")
                .isEqualTo("potential");
    }
}