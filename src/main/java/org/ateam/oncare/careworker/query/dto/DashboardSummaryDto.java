package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DashboardSummaryDto {
    private int scheduleCount;    // 오늘 일정 건수
    private int recipientCount;   // 담당 수급자 수
    private double workHours;     // 이번 주 누적 근무시간
}