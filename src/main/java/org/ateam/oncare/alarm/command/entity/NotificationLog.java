package org.ateam.oncare.alarm.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "notification_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId; // 수신자 PK (수급자 or 직원)

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private ReceiverType receiverType; // Enum: RECIPIENT, EMPLOYEE

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false)
    private LocalDate sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status; // Enum: SENT, FAILED, READ

    @Column(name = "read_at")
    private LocalDate readAt;

    @Column(name = "channel_type_id", nullable = false)
    private Integer channelTypeId;
}