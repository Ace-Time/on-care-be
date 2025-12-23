package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DashboardSummaryDto {
    private int todayScheduleCount;    // 오늘 일정 건수
    private int beneficiaryCount;      // 담당 수급자 수
    private double monthlyWorkHours;   // 이번 달 누적 근무시간
}