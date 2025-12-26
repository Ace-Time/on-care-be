package org.ateam.oncare.rental.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ateam.oncare.rental.command.enums.ContractStatusType;

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
    @Enumerated(EnumType.STRING)
    private ContractStatusType name;

}