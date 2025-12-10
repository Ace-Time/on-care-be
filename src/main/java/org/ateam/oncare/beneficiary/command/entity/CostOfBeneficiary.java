package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cost_of_beneficiary")
public class CostOfBeneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "monthly_amount", nullable = false)
    private BigDecimal monthlyAmount; // DECIMAL(10,2) -> BigDecimal, default = 0.00

    @Column(name = "month", nullable = false, length = 7)
    private String month; // "YYYY-MM"

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}