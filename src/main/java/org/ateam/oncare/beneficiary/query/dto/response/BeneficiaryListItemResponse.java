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
    private String riskLevel;   // 저위험/중위험/고위험

    // ✅ 요양보호사 (care_worker PK)
    private Long careWorkerId;  // care_worker.id

    // ✅ 화면에 뿌릴 요양보호사 이름 (employee.name)
    private String managerName; // (= 요양보호사 이름)

    private String serviceType;

    private String status;      // 서비스 중 / 서비스 해지
}