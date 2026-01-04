package org.ateam.oncare.counsel.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistNewBeneficiaryResponse {
    private Boolean success;
    private String message;
    private BigInteger beneficiaryId;
}
