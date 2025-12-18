package org.ateam.oncare.schedule.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vs_id")
    private Long vsId;

    @Column(name = "start_dt", nullable = false)
    private LocalDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    private LocalDateTime endDt;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status", nullable = false)
    private VisitStatus visitStatus; // Enum 생성 필요

    @Column(name = "rfid_start_time")
    private LocalDateTime rfidStartTime;

    @Column(name = "rfid_end_time")
    private LocalDateTime rfidEndTime;

    @Column(name = "is_log_written", nullable = false)
    private Boolean isLogWritten;

    @Column(name = "service_type_id", nullable = false)
    private Long serviceTypeId;

    @Column(name = "care_worker_id", nullable = false)
    private Long careWorkerId;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public enum VisitStatus {
        SCHEDULED, IN_PROGRESS, DONE
    }
}