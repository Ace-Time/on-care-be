package org.ateam.oncare.beneficiary.query.dto.response;
// 목록 응답 DTO

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiaryListItemResponse {

    private Long beneficiaryId;
    private String name;

    private String careLevel;   // 2등급
    private String riskLevel;   // 고위험

    private String managerName;
    private String serviceType;

    private String status;      // ACTIVE / INACTIVE
}