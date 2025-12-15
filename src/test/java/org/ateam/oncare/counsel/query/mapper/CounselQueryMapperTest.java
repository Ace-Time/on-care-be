package org.ateam.oncare.counsel.query.mapper;

import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
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
@Transactional
class CounselQueryMapperTest {

    @Autowired
    private CounselQueryMapper counselQueryMapper;

    @Test
    @DisplayName("전체 조회 시 이탈 고객(status=0)은 카테고리가 '이탈고객'으로 매핑되어야 한다")
    void testFindAllCustomers_Churned() {
        // Given
        // DB에 status=0인 수급자 데이터가 존재한다고 가정 (예: ID 2번)
        // 실제 환경에서는 @Sql 등을 이용해 테스트 데이터를 사전에 insert 해야 함
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
}