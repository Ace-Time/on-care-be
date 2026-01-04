package org.ateam.oncare.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatisticDTO {
    private String productName;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalRevenue;
    private BigDecimal returnRate;
}
