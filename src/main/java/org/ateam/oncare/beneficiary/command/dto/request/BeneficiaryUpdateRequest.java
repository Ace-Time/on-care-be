package org.ateam.oncare.beneficiary.command.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BeneficiaryUpdateRequest {

    // beneficiary 테이블 수정
    private String name;
    private String gender;     // "M" / "F" 등
    private String birthdate;  // "yyyy-MM-dd"
    private String phone;
    private String address;
    private Integer status;    // (예: 1=서비스중, 0=해지)  ※ 너희 DB 기준

    // 보호자(수급자당 1명 구조)
    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;

    // 태그 동기화
    private List<Long> tagIds;     // personal_tag.id

    // 위험요소 동기화
    private List<Integer> riskFactorIds; // m_risk_factor.id

    // (옵션) 등급 종료일 수정
    private String careLevelEndDate; // "yyyy-MM-dd"
}
