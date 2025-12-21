package org.ateam.oncare.careproduct.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "employee_id")
    private Integer employeeId;
}