package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonalScheduleRequest {
    private Long personalTypeId;
    private String customType;  // personalTypeId가 null일 경우 사용자 정의 타입
    private String title;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private String location;
    private String notes;
    private String scheduleStatus;
}
