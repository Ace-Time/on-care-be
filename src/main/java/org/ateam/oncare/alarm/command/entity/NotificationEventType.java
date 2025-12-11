package org.ateam.oncare.alarm.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_event_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_type_id")
    private Long targetTypeId;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "severity")
    private Integer severity;
}