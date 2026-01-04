package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 2. 엔티티 클래스
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiary_count")
public class BeneficiaryCount {

    @EmbeddedId
    private BeneficiaryCountId id;

    // 추가 컬럼이 있다면 여기에 작성
}