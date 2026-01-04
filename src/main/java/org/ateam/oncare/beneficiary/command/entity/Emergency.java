package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emergency")
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "emergency_st", nullable = false, length = 1)
    private String emergencySt; // "Y", "N", default 'N'

    @Column(name = "action_st", length = 1)
    private String actionSt; // "Y", "N", default 'N'

    @Column(name = "emergency_time")
    private LocalDateTime emergencyTime; // default current_timestamp

    @Column(name = "action_time")
    private LocalDateTime actionTime; // default current_timestamp

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}