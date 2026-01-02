package org.ateam.oncare.beneficiary.command.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResponseBeneficiaryDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String phone;
}
