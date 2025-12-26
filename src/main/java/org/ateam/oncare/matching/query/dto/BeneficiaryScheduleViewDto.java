package org.ateam.oncare.matching.query.dto;

import lombok.Data;

@Data
public class BeneficiaryScheduleViewDto {
    private Integer day;            // 1~7
    private String dayName;         // 월/화/수/목/금/토/일
    private String startTime;       // "10:00"
    private String endTime;         // "12:00"
    private String serviceTypeName;
}