package org.ateam.oncare.matching.query.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class BeneficiaryScheduleDto {

    private Long id;
    private Integer day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long beneficiaryId;
    private Integer serviceTypeId;
}