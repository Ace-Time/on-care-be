package org.ateam.oncare.schedule.query.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmedCalendarMonthCountDto {
    private LocalDate date;        // yyyy-MM-dd
    private Integer careCount;     // 방문요양 개수
    private Integer bathCount;     // 방문목욕 개수
    private Integer nurseCount;    // 방문간호 개수
}