package org.ateam.oncare.rental.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ateam.oncare.rental.command.enums.ContractStatusType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRetnalProductStatusType {
    private Long id;
    private String name;
}
