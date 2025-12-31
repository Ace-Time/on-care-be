package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.math.BigDecimal;

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
    private int rentalProducts;
    private int discardProducts;
    private int reservedProducts;
    private int purchasePrice;
    private int monthlyRenalFee;
    private String categoryCode;
    private String categoryName;
}
