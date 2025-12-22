package org.ateam.oncare.schedule.command.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ConfirmedVisitScheduleTimeUpdateResponse {
    private Long vsId;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
}