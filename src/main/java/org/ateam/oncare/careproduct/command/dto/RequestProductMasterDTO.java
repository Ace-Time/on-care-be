package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestProductMasterDTO {
    private String id;
    private String name;
    private String categoryCd;
    private String explanation;
    private BigDecimal amount;
    private BigDecimal rentalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
