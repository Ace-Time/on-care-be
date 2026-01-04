package org.ateam.oncare.schedule.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BeneficiaryScheduleUpdateRequest {

    private Integer serviceTypeId;
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDate effectiveDate;
}