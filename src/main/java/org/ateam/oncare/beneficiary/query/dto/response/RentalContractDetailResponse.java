package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalContractDetailResponse {

    private Long rentalContractId;
    private String productAssetId;  // 이제 EM002 같은 코드
    private String productName;

    private String contractStatusName;

    private String contractDate;
    private String startDate;
    private String endDate;

    private Integer monthlyAmount;
    private Integer durationMonths;
    private Integer totalCost;
}
