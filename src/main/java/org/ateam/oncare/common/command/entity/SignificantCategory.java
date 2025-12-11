package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_significant_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignificantCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

}