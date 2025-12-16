package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RentalUsageResponse {
    private List<RentalUsageResponse.RentalItem> current; // 현재 사용 중인 용품
    private List<RentalUsageResponse.RentalItem> history; // 과거 렌탈 이력

    @Getter
    @Setter
    public static class RentalItem {
        private Long rentalContractId;      // 계약 ID
        private String productAssetId;      // care_product.id (예: EM001-001)
        private String productName;         // m_care_product.name (예: 휠체어)
        private String contractLabel;       // "계약중" / "계약완료"
        private String contractStatusName;  // 원본(contract_status.name) - 디버깅/관리용
        private String rentalStatusName;    // rental_product_status.name (유지/회수접수/회수완료)

        private String startDate;           // yyyy-MM-dd
        private String endDate;             // yyyy-MM-dd (nullable)

        private Integer monthlyAmount;      // cp.rental_amount (월 렌탈료)
        private Integer durationMonths;     // 계약기간 개월수(계산)
    }
}