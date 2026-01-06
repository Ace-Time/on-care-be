package org.ateam.oncare.beneficiary.query.mapper;

import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryDetailResponse;
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
class BeneficiaryDetailMapperTest {

    @Autowired
    private BeneficiaryDetailMapper beneficiaryDetailMapper;

    @Test
    @DisplayName("수급자 상세조회(selectBeneficiaryDetail): 특정 beneficiaryId의 상세 정보가 조회되어야 한다")
    void selectBeneficiaryDetail_shouldReturnDetail() {
        // Given
        Long beneficiaryId = 1L; // dev DB에 존재하는 1번 beneficiary id

        // When
        BeneficiaryDetailResponse detail =
                beneficiaryDetailMapper.selectBeneficiaryDetail(beneficiaryId);

        // Then
        assertThat(detail).isNotNull();

        System.out.println("=== selectBeneficiaryDetail ===");
        System.out.println(detail);
        System.out.println("===============================");
    }

    @Test
    @DisplayName("수급자 태그 조회(selectBeneficiaryTags): 태그 리스트가 조회되어야 한다(없을 수도 있음)")
    void selectBeneficiaryTags_shouldReturnTags() {
        // Given
        Long beneficiaryId = 1L; // dev DB에 존재하는 1번 beneficiary id

        // When
        List<String> tags = beneficiaryDetailMapper.selectBeneficiaryTags(beneficiaryId);

        // Then
        assertThat(tags).isNotNull();
        System.out.println("tags=" + tags);

        assertThat(tags).isNotEmpty();  // 태그가 반드시 있어야 하는 데이터가 아니면 비어도 OK
    }

    @Test
    @DisplayName("수급자 위험요소 조회(selectBeneficiaryRiskFactors): 위험요소 리스트가 조회되어야 한다(없을 수도 있음)")
    void selectBeneficiaryRiskFactors_shouldReturnRiskFactors() {
        // Given
        Long beneficiaryId = 1L; // dev DB에 존재하는 1번 beneficiary id

        // When
        List<BeneficiaryDetailResponse.RiskFactorItem> riskFactors =
                beneficiaryDetailMapper.selectBeneficiaryRiskFactors(beneficiaryId);

        // Then
        assertThat(riskFactors).isNotNull();

        System.out.println("===== Risk Factors =====");
        if (riskFactors.isEmpty()) {
            System.out.println("위험요소 없음");
        } else {
            for (BeneficiaryDetailResponse.RiskFactorItem rf : riskFactors) {
                System.out.println(
                        "- id=" + rf.getId()
                                + ", name=" + rf.getName()
                                + ", score=" + rf.getScore()
                );
            }
        }
        System.out.println("========================");

        assertThat(riskFactors).isNotEmpty();   // 위험요소도 케이스에 따라 없을 수 있으니 비어도 OK
    }
}
