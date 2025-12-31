package org.ateam.oncare.payment.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "electronic_payment_process")
public class ElectronicPaymentProcess {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String approve; // '0': 미승인, '1': 승인

    @Column(name = "drafter_id", nullable = false)
    private Integer drafterId; // 기안자 (혹은 전 단계 승인자)

    @Column(name = "approver_id", nullable = false)
    private Integer approverId; // 승인해야 할 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "electronic_payment_id")
    private ElectronicPayment electronicPayment;
}