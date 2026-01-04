package org.ateam.oncare.beneficiary.query.dto.response;

// 서비스탭 1번 화면

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
        private String month;        // YYYY-MM
        private Integer totalAmount; // 월 누계 (cost_of_beneficiary.monthly_amount)
    }
}