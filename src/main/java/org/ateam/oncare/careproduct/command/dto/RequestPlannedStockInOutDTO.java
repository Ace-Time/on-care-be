package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestPlannedStockInOutDTO {
    private Long id;
    private Integer status;
    private LocalDate expectedStartDate;
    private LocalDate expectedEndDate;
    private String isConfirmed;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private String productNameOrProductCode;
    private String productId;
}
