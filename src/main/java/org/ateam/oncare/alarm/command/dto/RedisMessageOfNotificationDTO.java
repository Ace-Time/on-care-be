package org.ateam.oncare.alarm.command.dto;

import lombok.*;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RedisMessageOfNotificationDTO {
    private Long alarmId;
    private String title;
    private String content; // Renamed from message
    private Integer severity;
    private LocalDateTime sentAt;
    private String status; // Added for MyBatis mapping
    private boolean isRead;
    private Long receiverId;
}
