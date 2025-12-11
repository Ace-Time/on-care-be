package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "contract_status", columnDefinition = "ENUM('진행중','만료','해지')")
    private String contractStatus;

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}