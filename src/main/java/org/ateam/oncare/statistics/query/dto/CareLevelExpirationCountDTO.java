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
public class CareLevelExpirationCountDTO {
    private Integer days90; // 61 ~ 90일
    private Integer days60; // 46 ~ 60일
    private Integer days45; // 0 ~ 45일
}
