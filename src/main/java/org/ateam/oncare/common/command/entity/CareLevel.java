package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_care_level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false, length = 50)
    private String level;

    @Column(name = "validity", nullable = false)
    private Integer validity;

    @Column(name = "monthly_limit", nullable = false)
    private Integer monthlyLimit;

}