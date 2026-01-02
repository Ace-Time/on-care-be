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

    @Column(nullable = false)
    private String number; // 문서 번호 (예: 2024-001)

    @Column(nullable = false)
    private String approve; // '0': 미승인, '1': 승인

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "electronic_payment_category_id", nullable = false)
    private Integer categoryId;
}