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

    @Column(name = "electronic_payment_id", nullable = false)
    private Integer electronicPaymentId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // 결재자 ID

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "status", nullable = false)
    private Integer status; // 0:대기, 1:승인, 2:반려

    @Column(name = "comment")
    private String comment;

    @Column(name = "processed_at")
    private java.time.LocalDateTime processedAt;
}