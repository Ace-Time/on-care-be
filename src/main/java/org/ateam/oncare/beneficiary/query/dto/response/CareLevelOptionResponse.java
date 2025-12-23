package org.ateam.oncare.beneficiary.query.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ✅ 등급 옵션(필요하면 monthlyLimit/validity 같이 내려줄 수 있음)
 * - 지금은 label만 써도 되지만, 나중에 화면에 한도/유효기간 보여주려면 유용
 */
@Getter
@AllArgsConstructor
public class CareLevelOptionResponse {
    private final Long id;
    private final String label;
    private final Integer validity;      // 개월수 같은 유효기간(DDL 기준)
    private final Long monthlyLimit;     // 월 한도
}