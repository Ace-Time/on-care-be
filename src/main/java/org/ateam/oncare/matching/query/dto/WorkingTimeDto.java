package org.ateam.oncare.matching.query.dto;

import lombok.Data;

@Data
public class WorkingTimeDto {

    private Integer day;            // 1~7
    private String dayName;         // 월~일

    private String startTime;       // 09:00
    private String endTime;         // 11:00

    private String serviceTypeName; // 방문요양 / 방문간호 / 방문목욕
}