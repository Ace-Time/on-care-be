package org.ateam.oncare.rental.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "rental_contract_cd")
    private Long rentalContractCd;

    @Column(name = "rental_status_id")
    private Long rentalStatusId;

    @Column(name = "product_id")
    private String productId;

}