package org.ateam.oncare.rental.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contract_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

}