package org.ateam.oncare.rental.command.dto;

import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@Builder
public class RentalProductForCalculationDTO {
    private String productCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private int usedDate;
    private int calcAmount;
}
