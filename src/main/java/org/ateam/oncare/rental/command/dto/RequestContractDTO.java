package org.ateam.oncare.rental.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.ateam.oncare.rental.command.enums.ContractStatusType;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class RequestContractDTO {
    private String beneficiaryOrEmployee;
    private int contractStatus;
}
