package org.ateam.oncare.rental.command.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class ResponseRentalContractDTO {
    private Long id;
    private LocalDate wantedDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expectedDate;
    private LocalDateTime createdAt;
    private int termMonth;
    private int cumulativeRevenue;
    private Long beneficiaryId;
    private Long empId;
    private String productCd;
    private Long contractStatusCd;
}
