package org.ateam.oncare.alarm.query.dto;

import lombok.*;
import org.ateam.oncare.alarm.command.entity.NotificationLog;
import org.ateam.oncare.alarm.command.entity.NotificationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationQueryDTO {
    private Long alarmId;
    private String title;
    private String content; // Renamed from message
    private Integer severity;
    private java.time.LocalDateTime sentAt;
    private String status; // Added for MyBatis mapping
    private boolean isRead;

    public static NotificationQueryDTO from(NotificationLog entity) {
        return NotificationQueryDTO.builder()
                .alarmId(entity.getAlarmId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .severity(entity.getSeverity())
                .sentAt(entity.getSentAt())
                .status(entity.getStatus().name())
                .isRead(entity.getStatus() == NotificationStatus.READ)
                .build();
    }
}