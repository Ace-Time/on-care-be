package org.ateam.oncare.matching.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateVisitScheduleRequest {

    private Long beneficiaryId;
    private Long careWorkerId;
    private Long serviceTypeId;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private String note;
}