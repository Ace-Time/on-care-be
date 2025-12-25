package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitScheduleRequest {
    private Long beneficiaryId;
    private Long serviceTypeId;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private String visitStatus;
    private String note;
}
