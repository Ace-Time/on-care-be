package org.ateam.oncare.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBeneficiaryStatsDTO {
    private String month; // "YYYY-MM"
    private Integer newBeneficiaryCount; // 신규 수급자 수
    private Integer withdrawnBeneficiaryCount; // 탈퇴 수급자 수
}
