package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RentalUsageResponse {

    // ✅ 단일 리스트(현재/과거 구분 없음)
    private List<RentalItem> items;

    @Getter
    @Setter
    public static class RentalItem {
        private Long rentalProductId;     // rp.id (카드 유니크키)
        private Long rentalContractId;    // rc.id (계약 ID)
        private String productAssetId;    // care_product.id (예: EM001-001)
        private String productName;       // m_care_product.name (예: 휠체어)

        private String contractStatusName; // cs.name
//        private String rentalStatusName;   // rps.name (용품 상태)

        private String startDate;          // rc.start_date (yyyy-MM-dd)
        private String endDate;            // rc.end_date   (yyyy-MM-dd, nullable)

        private Integer monthlyAmount;     // cp.rental_amount
        private Integer durationMonths;    // 계산
    }
}
