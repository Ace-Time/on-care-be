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
public class CareGradeBeneficiaryCountDTO {
    private String grade; // 장기요양등급 (예: "1등급", "2등급")
    private Integer count;
}
