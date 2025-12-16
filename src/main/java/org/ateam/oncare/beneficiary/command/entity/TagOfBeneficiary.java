package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tag_of_beneficiary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TagOfBeneficiaryId.class) // ✅ 이게 핵심
public class TagOfBeneficiary {

    @Id
    @Column(name = "beneficiary_id")
    private Long beneficiaryId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;
}