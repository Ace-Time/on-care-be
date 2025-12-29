package org.ateam.oncare.rental.command.dto;

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
public class RequestRentalContractDTO {
    private Long id;
    private LocalDate wantedDate;
    private Integer termMonth;
    private Long beneficiaryId;
    private Long empId;
    private String productCd;
    private LocalDate expectedDate;
    private LocalDate endDate;
    private Long contractStatusCd;
}
