package org.ateam.oncare.beneficiary.query.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BeneficiaryDetailResponse {

    // 기본 정보
    private Long beneficiaryId;
    private String name;
    private String gender;
    private String birthdate;   // yyyy-MM-dd
    private String phone;
    private String address;
    private String status;      // ACTIVE / INACTIVE

    // 등급 / 위험
    private String careLevel;        // 2등급
    private String careLevelEndDate; // 2025-12-10
    private String riskLevel;        // 저위험 / 중위험 / 고위험

    // 담당자
    private String managerName;
    private String serviceType;

    // 보호자
    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;

    // 금액 정보
    private Integer monthlyLimit;  // 월 지원금
    private Integer usedAmount;    // 사용액
    private Integer remainingAmount;
    private Double usedRate;       // %

    // ✅ 수급자 태그 (personal_tag.tag)
    private List<String> tags;

    // ✅ 위험요소 (m_risk_factor)
    private List<RiskFactorItem> riskFactors;

    @Getter @Setter
    public static class RiskFactorItem {
        private Integer id;
        private String name;
        private Integer score;
    }


}