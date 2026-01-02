package org.ateam.oncare.rental.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
public class RentalContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wanted_date")
    private LocalDate wantedDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "expected_date")
    private LocalDate expectedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "term_month")
    private int termMonth;

    @Column(name = "cumulative_revenue")
    private int cumulativeRevenue;

    @Column(name = "beneficiary_id")
    private Long beneficiaryId;

    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "product_cd")
    private String productCd;

    @Column(name = "contract_status_cd")
    private Long contractStatusCd;

}