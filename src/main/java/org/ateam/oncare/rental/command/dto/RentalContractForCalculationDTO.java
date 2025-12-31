package org.ateam.oncare.rental.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class RentalContractForCalculationDTO {
    private int id;
    private String productCd;
    private int usedMonthly;
    private int usedDays;
    private int calculationAmount;
    private LocalDate calculationDate;
}
