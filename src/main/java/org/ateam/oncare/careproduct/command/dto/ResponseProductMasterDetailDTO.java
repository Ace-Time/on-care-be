package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseProductMasterDetailDTO {
    private String id;
    private String name;
    private String explanation;
    private BigDecimal amount;
    private BigDecimal rentalAmount;
    private String categoryCd;
    private int totalProducts;
    private int availableProducts;
    private int reatalProducts;
    private int reservedProducts;
    private int purchasePrice;
    private int monthlyRenalFee;
}
