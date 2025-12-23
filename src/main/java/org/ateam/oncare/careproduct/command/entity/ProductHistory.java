package org.ateam.oncare.careproduct.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ateam.oncare.careproduct.command.enums.ProductHistoryStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductHistoryStatus status;

    @Column(name = "content")
    private String content;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "beneficiary_id")
    private Long beneficiaryId;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}