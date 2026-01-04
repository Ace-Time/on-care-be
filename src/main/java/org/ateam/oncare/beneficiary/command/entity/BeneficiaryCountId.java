package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

// 1. 복합키 클래스
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BeneficiaryCountId implements Serializable {
    @Column(name = "beneficiary_care_level_id")
    private Integer beneficiaryCareLevelId;

    @Column(name = "m_care_level_id")
    private Integer mCareLevelId;
}