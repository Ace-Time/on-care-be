package org.ateam.oncare.schedule.query.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleMonthCountDto {
    private LocalDate date;   // 날짜
    private int careCount;    // 방문 요양 개수
    private int bathCount;    // 방문 목욕 개수
    private int nurseCount;   // 방문 간호 개수
}
