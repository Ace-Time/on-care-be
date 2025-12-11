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
public class RiskOfMemberId implements Serializable {
    @Column(name = "beneficiary_id")
    private Long beneficiaryId;

    @Column(name = "risk_id")
    private Integer riskId;
}