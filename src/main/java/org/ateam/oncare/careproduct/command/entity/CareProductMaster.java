package org.ateam.oncare.careproduct.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "m_care_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CareProductMaster {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "rental_amount")
    private BigDecimal rentalAmount;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "category_cd")
    private String categoryCd;
}
