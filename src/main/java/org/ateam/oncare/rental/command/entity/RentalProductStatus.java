package org.ateam.oncare.rental.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rental_product_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalProductStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

}