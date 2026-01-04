package org.ateam.oncare.counsel.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 특이사항 마스터 조회 응답 DTO
 * m_significant + m_significant_category JOIN 결과
 * 특이사항 추가 시 선택 가능한 전체 목록 제공용
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignificantMasterResponse {

    /**
     * 특이사항 ID (m_significant.id)
     */
    private Integer significantId;

    /**
     * 특이사항명 (m_significant.name)
     * 예: "렌탈금액민감", "보호자결정의존", "보편상품신뢰", "거동불편", "목욕불편" 등
     */
    private String significantName;

    /**
     * 카테고리 ID (m_significant_category.id)
     * 1: 렌탈성사도움
     * 2: 문의해결도움
     * 3: 컴플레인해결도움
     * 4: 해지상담도움
     */
    private Integer categoryId;

    /**
     * 카테고리명 (m_significant_category.name)
     * "렌탈성사도움", "문의해결도움", "컴플레인해결도움", "해지상담도움"
     */
    private String categoryName;
}
