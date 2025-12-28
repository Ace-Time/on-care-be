package org.ateam.oncare.alarm.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    // 수신자 번호 (NOT NULL)
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    // 수신자 타입 (ENUM: RECIPIENT, EMPLOYEE)
    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private ReceiverType receiverType;

    // 제목 (VARCHAR(50), NOT NULL)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    // 내용 (VARCHAR(2000), NOT NULL)
    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    // 발송 시각
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    // 상태 (ENUM: SENT, FAILED, READ)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    // 유형 (VARCHAR(50), NOT NULL) - 예: "청구", "만료안내"
    @Column(name = "template_type", length = 50, nullable = false)
    private String templateType;

    // 중요도 (INT, NOT NULL)
    @Column(name = "severity", nullable = false)
    private Integer severity;

    // 읽은 시간 (NULL 허용)
    @Column(name = "read_at")
    private LocalDateTime readAt;

    // 타겟 타입 (ENUM: RECIPIENT, EMPLOYEE, BOTH)
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    // 읽음 처리 편의 메서드
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }
}