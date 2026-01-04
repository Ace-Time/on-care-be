package org.ateam.oncare.counsel.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * 수급자 특이사항 조회 응답 DTO
 * beneficiary_significant + m_significant + m_significant_category JOIN 결과
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiarySignificantResponse {

    /**
     * 수급자 ID
     */
    private BigInteger beneficiaryId;

    /**
     * 특이사항 ID (m_significant.id)
     */
    private Integer significantId;

    /**
     * 특이사항명 (m_significant.name)
     * 예: "렌탈금액민감", "보호자결정의존", "거동불편" 등
     */
    private String significantName;

    /**
     * 특이사항 카테고리명 (m_significant_category.name)
     * 예: "렌탈성사도움", "문의해결도움", "컴플레인해결도움", "해지상담도움"
     */
    private String significantCategoryName;

    /**
     * 특이사항 카테고리 ID (m_significant_category.id)
     */
    private Integer significantCategoryId;
}
