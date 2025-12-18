package org.ateam.oncare.employee.command.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EducationInputDTO {
    private String eduName;
    private String institution;
    private LocalDate eduDate;
    private LocalDate nextEduDate;
    private Boolean isOverdue;
    private Integer status; // 0:대기, 2:승인 등
}
