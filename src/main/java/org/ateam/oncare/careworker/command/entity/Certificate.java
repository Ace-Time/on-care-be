package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "certificate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_name", nullable = false)
    private String certificateName;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "edu_cycle_years", nullable = false)
    private Integer eduCycleYears;

    @Builder
    public Certificate(String certificateName, String field5, Integer eduCycleYears) {
        this.certificateName = certificateName;
        this.organization = organization;
        this.eduCycleYears = eduCycleYears;
    }
}

