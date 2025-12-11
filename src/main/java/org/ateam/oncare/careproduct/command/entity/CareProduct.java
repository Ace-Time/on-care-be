package org.ateam.oncare.careproduct.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "care_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareProduct {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "rental_amount")
    private BigDecimal rentalAmount;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "bought_date")
    private LocalDateTime boughtDate;

    @Column(name = "product_cd")
    private String productCd;

    @Column(name = "product_status")
    private Integer productStatus;

    @Column(name = "m_location_status_cd")
    private Long mLocationStatusCd;

    @Column(name = "last_id")
    private Long lastId;

}