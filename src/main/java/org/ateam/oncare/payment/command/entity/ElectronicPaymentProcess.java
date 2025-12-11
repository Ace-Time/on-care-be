package org.ateam.oncare.payment.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "electronic_payment_process")
public class ElectronicPaymentProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "approve", nullable = false, columnDefinition = "ENUM('0','1')")
    private String approve; // "0": 미승인, "1": 승인

    // --- 외래키 ID 직접 매핑 (연관 관계 제거) ---

    // 기안자(작성자) Employee ID
    @Column(name = "drafter_id", nullable = false)
    private Integer drafterId;

    // 결재자(승인자) Employee ID
    @Column(name = "approver_id", nullable = false)
    private Integer approverId;

    // 연결된 결재 문서 ID
    @Column(name = "electronic_payment_id", nullable = false)
    private Integer electronicPaymentId;
}