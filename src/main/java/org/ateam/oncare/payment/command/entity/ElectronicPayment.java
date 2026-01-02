package org.ateam.oncare.payment.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "electronic_payment")
public class ElectronicPayment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "electronic_payment_category_id", nullable = false)
    private Long electronicPaymentCategoryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", nullable = false)
    private Integer status; // 0:대기, 1:승인, 2:반려

    @Column(name = "priority", nullable = false)
    private Integer priority; // 0:긴급, 1:보통, 2:낮음

    @Column(name = "amount")
    private java.math.BigDecimal amount;

    @Column(name = "start_date")
    private java.time.LocalDate startDate;

    @Column(name = "end_date")
    private java.time.LocalDate endDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private java.time.LocalDateTime updatedAt;
}