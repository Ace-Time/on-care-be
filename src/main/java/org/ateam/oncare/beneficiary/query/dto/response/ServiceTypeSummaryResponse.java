package org.ateam.oncare.beneficiary.query.dto.response;

// 서비스탭 2번 화면

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceTypeSummaryResponse {

    private String month; // YYYY-MM
    private List<ServiceTypeSummaryItem> serviceTypes;

    @Getter
    @Setter
    public static class ServiceTypeSummaryItem {
        private Integer serviceTypeId;
        private String serviceTypeName;

        private Integer totalAmount; // SUM(calculated_amount)
        private Integer visitCount;  // COUNT(*)

        private Double totalHours;   // ✅ 총 방문시간(시간)
    }
}