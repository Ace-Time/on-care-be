package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bill")
    private String bill; // 청구서 제목 등

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "is_sent", columnDefinition = "ENUM('초안','보류','전송','지불됨','취소됨')")
    private String isSent; // default : '초안'

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "own_count")
    private Integer ownCount;

    @Column(name = "payment_status", columnDefinition = "ENUM('미납','완납')")
    private String paymentStatus; // default '미납'

    @Column(name = "billing_month", length = 7)
    private String billingMonth; // "YYYY-MM"

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}