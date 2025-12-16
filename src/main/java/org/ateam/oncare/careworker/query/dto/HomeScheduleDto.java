package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HomeScheduleDto {
    private Long scheduleId;
    private String recipientName; // 수급자 이름
    private String grade;         // 등급 (예: 2등급)
    private String visitTime;     // 방문 시간 (예: 09:00-11:00)
    private String address;       // 주소
    private String status;        // 상태 (예: 진행중, 완료)
}