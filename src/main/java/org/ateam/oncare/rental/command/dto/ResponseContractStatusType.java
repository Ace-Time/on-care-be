package org.ateam.oncare.rental.command.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ateam.oncare.rental.command.enums.ContractStatusType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseContractStatusType {
    private Long id;
    private ContractStatusType name;
}
