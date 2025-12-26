package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalContractDetailResponse {

    private Long rentalContractId;
    private String productAssetId;
    private String productName;

    // 계약대기 / 접수 / 유지 / 계약중 / 종료 / 해지 / 만료
    private String contractStatusName;

    private String contractDate;
    private String startDate;
    private String endDate;

    private Integer monthlyAmount;
    private Integer durationMonths;
    private Integer totalCost;
}