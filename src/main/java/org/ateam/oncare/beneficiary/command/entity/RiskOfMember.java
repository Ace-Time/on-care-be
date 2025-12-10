package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 2. 엔티티 클래스
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "riskOfMember")
public class RiskOfMember {

    @EmbeddedId
    private RiskOfMemberId id;
}