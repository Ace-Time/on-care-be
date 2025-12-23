package org.ateam.oncare.schedule.command.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ConfirmedVisitScheduleTimeUpdateRequest {
    private LocalDateTime startDt;
    private LocalDateTime endDt;
}