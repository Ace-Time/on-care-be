package org.ateam.oncare.careproduct.command.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductAmountForRentalDTO {
    private String productCode;
    private int amountMonthly;
    private int amountDaily;
}
