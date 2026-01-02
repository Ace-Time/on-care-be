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
public class RiskLevelBeneficiaryCountDTO {
    private Integer level; // 위험 등급 (1, 2, 3...)
    private String score; // 점수 구간 또는 설명
    private Integer count; // 수급자 수
}
