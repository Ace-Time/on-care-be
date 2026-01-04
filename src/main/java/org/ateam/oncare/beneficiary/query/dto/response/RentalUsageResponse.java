package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RentalUsageResponse {

    private List<RentalItem> items;

    @Getter
    @Setter
    public static class RentalItem {
        private Long rentalContractId;
        private String productAssetId;     // 이제 EM002 같은 코드
        private String productName;

        private String contractStatusName;

        private String startDate;
        private String endDate;

        private Integer monthlyAmount;
        private Integer durationMonths;
    }
}
