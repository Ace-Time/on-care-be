package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_risk_level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "score", nullable = false, length = 255)
    private String score;

}