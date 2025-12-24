package org.ateam.oncare.global.eventType;

import lombok.Builder;
import lombok.Getter;
import org.ateam.oncare.global.enums.StockType;

import java.time.LocalDate;

@Getter
@Builder
public class ProductStockEvent {
    private StockType status;
    private LocalDate expectedDate;
    private String productCode;
    private String isConfirmed = "N";
    private Integer employeeId;
}
