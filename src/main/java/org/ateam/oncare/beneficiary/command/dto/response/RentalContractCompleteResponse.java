package org.ateam.oncare.beneficiary.command.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalContractCompleteResponse {

    private boolean success;
    private String message;

    private Long beneficiaryId;
    private Long rentalContractId;

    private String contractStatus; // 종료
    private String endDate;         // yyyy-MM-dd
}