package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiary_significant")
public class BeneficiarySignificant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId; // 1:월 ~ 7:일

    @Column(name = "significant_id", nullable = false)
    private Long significantId;
}
