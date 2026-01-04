package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ✅ 프론트 옵션용 공용 DTO (id + label)
 * - 태그, 위험요소, 등급 모두 같은 형태로 내려줄 수 있음
 */
@Getter
@AllArgsConstructor
public class MetaOptionResponse {
    private final Long id;
    private final String label;
}