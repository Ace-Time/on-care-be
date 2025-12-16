package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalContractDetailResponse {

    private Long rentalContractId;
    private Long productAssetId;
    private String productName;

    private String contractLabel;
    private String contractStatusName;

    private String contractDate;
    private String startDate;
    private String endDate;

    private Integer monthlyAmount;
    private Integer durationMonths;
    private Integer totalCost;
}