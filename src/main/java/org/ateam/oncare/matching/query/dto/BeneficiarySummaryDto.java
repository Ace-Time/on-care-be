package org.ateam.oncare.matching.query.dto;

import lombok.Data;

@Data
public class BeneficiarySummaryDto {
    private Long beneficiaryId;
    private String name;           // 김영희
    private String gender;         // 남자/여자
    private String riskLevel;      // 저위험/중위험/고위험
    private Boolean assigned;      // 배정 여부 (true/false)
}