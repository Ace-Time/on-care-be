package org.ateam.oncare.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueBeneficiaryDTO {
    private Long beneficiaryId;
    private String name;
    private String phone;
    private BigDecimal totalOverdueAmount; // 총 연체 금액
    private Integer overdueMonthsCount; // 연체 개월 수
    private String lastOverdueMonth; // 최근 연체 월 (YYYY-MM)
}
