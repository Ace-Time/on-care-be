package org.ateam.oncare.beneficiary.command.entity;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class TagOfBeneficiaryId implements Serializable {
    private Long beneficiaryId;
    private Long tagId;
}