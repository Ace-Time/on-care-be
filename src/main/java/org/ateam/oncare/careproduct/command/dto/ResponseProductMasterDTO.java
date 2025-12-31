package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseProductMasterDTO {
    private String id;
    private String name;
    private String explanation;
    private BigDecimal amount;
    private BigDecimal rentalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryCd;
    private String categoryName;
}
