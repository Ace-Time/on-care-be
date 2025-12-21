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
public class RentalHistoryDTO {
    private String productCode;
    private String productName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String beneficiary;
}
