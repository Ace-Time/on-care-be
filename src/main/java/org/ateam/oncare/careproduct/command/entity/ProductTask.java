package org.ateam.oncare.careproduct.command.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "expected_date")
    private LocalDate expectedDate;

    @Column(name = "is_confirmed")
    private String isConfirmed;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "employee_id")
    private Integer employeeId;

}