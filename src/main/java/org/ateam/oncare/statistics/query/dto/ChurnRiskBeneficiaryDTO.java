package org.ateam.oncare.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChurnRiskBeneficiaryDTO {
    private Long id;
    private String name;
    private Integer age;
    private LocalDateTime lastCounselDate;
    private String riskReason; // "장기 미상담", "부정적 상담 키워드 감지" 등
}
