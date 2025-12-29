package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicEvaluationListDto {
    private Long evalId;
    private String evalType;
    private String evalTypeName;
    private LocalDate evalDate;
    private Long beneficiaryId;
    private String beneficiaryName;
    private String careLevel;
    private String resultGrade;
    private Integer totalScore;
    private String specialNote;
}
