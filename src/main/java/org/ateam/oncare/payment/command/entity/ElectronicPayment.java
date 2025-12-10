package org.ateam.oncare.payment.command.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "electronic_payment")
public class ElectronicPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "number", nullable = false, length = 50)
    private String number; // 문서 번호 (예: "DOC-2024-001")

    @Column(name = "approve", nullable = false, columnDefinition = "ENUM('0','1')")
    private String approve; // "0": 미승인, "1": 승인

    // [Relation Removed] Employee 객체 대신 ID 값 직접 매핑
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;
}