package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseProductDTO {
    private String id;
    private BigDecimal amount;
    private BigDecimal rentalAmount;
    private BigDecimal cumulativeRevenue;
    private Integer productStatus;
    private String statusName;
    private String productCd;
    private String beneficiaryName;
    private LocalDate boughtDate;
}
