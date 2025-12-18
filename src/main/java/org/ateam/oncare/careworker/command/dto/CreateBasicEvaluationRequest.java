package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBasicEvaluationRequest {
    private String evalType;  // FALL, BEDSORE, COGNITIVE, NEEDS
    private Long templateId;
    private LocalDate evalDate;
    private String evalData;  // JSON 형식
    private Long beneficiaryId;
}
