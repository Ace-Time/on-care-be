package org.ateam.oncare.rental.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "term_month")
    private int termMonth;

    @Column(name = "beneficiary_id")
    private Long beneficiaryId;

    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "product_cd")
    private String productCd;

    @Column(name = "contract_status_cd")
    private Long contractStatusCd;

}