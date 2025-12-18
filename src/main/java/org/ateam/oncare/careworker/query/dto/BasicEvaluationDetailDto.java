package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicEvaluationDetailDto {
    // 기본 정보
    private Long evalId;
    private String evalType;
    private String evalTypeName;
    private Long templateId;
    private String templateName;
    private Integer templateVersion;
    private LocalDate evalDate;
    private LocalDateTime createdAt;

    // 수급자 정보
    private Long beneficiaryId;
    private String beneficiaryName;
    private String beneficiaryAddress;
    private String careLevel;

    // 평가 데이터 (JSON)
    private String evalData;
}
