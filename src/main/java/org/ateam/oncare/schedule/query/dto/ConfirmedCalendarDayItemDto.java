package org.ateam.oncare.schedule.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmedCalendarDayItemDto {

    private Integer vsId;

    private String startTime;
    private String endTime;
    private Integer durationMinutes;

    private String careWorkerName;
    private String beneficiaryName;

    private Integer serviceTypeId;
    private String serviceTypeName;

    /** DONE / IN_PROGRESS / SCHEDULED 등 */
    private String status;
}