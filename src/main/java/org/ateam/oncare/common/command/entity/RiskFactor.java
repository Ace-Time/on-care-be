package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_risk_factor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "score", nullable = false)
    private Integer score;

}