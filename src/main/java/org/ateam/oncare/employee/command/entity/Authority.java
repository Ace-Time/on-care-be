package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_authorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

}