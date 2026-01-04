package org.ateam.oncare.employee.query.dto;

import lombok.Data;

@Data
public class VisitScheduleVO {
    private Long vsId;
    private String startDt;
    private String endDt;
    private String visitStatus;
    private Long serviceTypeId;
    private String beneficiaryName;
}
