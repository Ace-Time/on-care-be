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
public class ResponseContractRentalDTO {
    private Long id;
    private String productName;
    private String beneficiary;
    private String employee;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expectedEndDate; // 예상 종료일
    private int cumulativeRevenue;
}
