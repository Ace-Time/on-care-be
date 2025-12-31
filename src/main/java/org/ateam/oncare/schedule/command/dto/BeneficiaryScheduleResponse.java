package org.ateam.oncare.schedule.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class BeneficiaryScheduleResponse {

    private Long id;
    private Long beneficiaryId;
    private Integer serviceTypeId;
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;
}