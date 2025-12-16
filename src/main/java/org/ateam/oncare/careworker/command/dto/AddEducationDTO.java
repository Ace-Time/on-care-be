package org.ateam.oncare.careworker.command.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddEducationDTO {
    private String eduName;
    private String institution;
    private LocalDate eduDate;
    private LocalDate nextEduDate;
    private Boolean isOverdue;     // 미이수 여부
    private Integer status;        // 승인 상태 (0:대기)
}
