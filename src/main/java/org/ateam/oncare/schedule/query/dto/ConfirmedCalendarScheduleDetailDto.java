package org.ateam.oncare.schedule.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConfirmedCalendarScheduleDetailDto {

    private Integer vsId;

    private Integer serviceTypeId;
    private String serviceTypeName;

    private LocalDate date;
    private String startTime;
    private String endTime;
    private Integer durationMinutes;

    private Long beneficiaryId;
    private String beneficiaryName;

    private Integer careWorkerId;
    private String careWorkerName;

    private String status; // visit_status (SCHEDULED / IN_PROGRESS / DONE)
}