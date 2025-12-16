package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceUsageResponse {

    private List<ServiceUsageMonthItem> histories;

    @Getter
    @Setter
    public static class ServiceUsageMonthItem {
        private String month;               // 2025-12
        private Integer totalAmount;        // cost_of_beneficiary.monthly_amount (월 누계)
        private List<ServiceTypeItem> serviceTypes;
    }

    @Getter
    @Setter
    public static class ServiceTypeItem {
        private Integer serviceTypeId;
        private String serviceTypeName;     // 방문요양, 방문목욕, 방문간호
        private Integer usageCount;         // ✅ 월/타입별 횟수
    }
}