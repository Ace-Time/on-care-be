package org.ateam.oncare.careproduct.command.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponsePlannedStockInOutDTO {
    private Long id;
    private Integer status;
    private String statusName;
    private LocalDate expectedDate;
    private String isConfirmed;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private String productId;
    private String productName;
    private Integer employeeId;
    private String employeeName;
}
