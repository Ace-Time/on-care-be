package org.ateam.oncare.rental.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class ResponseRentalProductDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long rentalContractCd;
    private Long rentalStatusId;
    private String productId;
}
