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
public class UpdateBasicEvaluationRequest {
    private LocalDate evalDate;
    private String evalData;  // JSON 형식
}
