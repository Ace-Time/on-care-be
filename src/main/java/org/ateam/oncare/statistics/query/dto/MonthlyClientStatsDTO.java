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
public class MonthlyClientStatsDTO {
    private String month; // "YYYY-MM"
    private Integer potentialCount; // 잠재 고객 수
    private Integer contractCount; // 계약 성사 고객 수
}
