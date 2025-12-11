package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiary_care_level")
public class BeneficiaryCareLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "number", nullable = false)
    private String number; // 인정번호 등

    @Column(name = "renewal", length = 1)
    private String renewal; // "Y":갱신, "N":미갱신

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}