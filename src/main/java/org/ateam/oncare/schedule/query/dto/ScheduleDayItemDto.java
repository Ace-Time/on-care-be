package org.ateam.oncare.schedule.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleDayItemDto {
    private Long matchingId;

    private LocalDate date;

    private Long beneficiaryId;
    private String beneficiaryName;

    private Integer careWorkerId;
    private String careWorkerName;

    private Integer serviceTypeId;
    private String serviceTypeName;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer durationMinutes;
}