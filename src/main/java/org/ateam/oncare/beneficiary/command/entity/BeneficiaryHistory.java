package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiary_history")
public class BeneficiaryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;

    @Column(name = "terminate_date")
    private LocalDateTime terminateDate;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}
