package org.ateam.oncare.schedule.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class BeneficiaryScheduleCreateRequest {

    private Long beneficiaryId;
    private Integer serviceTypeId;
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;
}