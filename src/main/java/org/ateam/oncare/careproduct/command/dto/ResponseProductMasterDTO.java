package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseProductMasterDTO {
    private String id;
    private String name;
    private BigDecimal amount;
    private BigDecimal rentalAmount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String categoryCd;
}
