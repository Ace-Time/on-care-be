package org.ateam.oncare.careworker.command.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CertAndEduStatus {
    PENDING(0, "승인대기"),
    REJECTED(1, "반려"),
    APPROVED(2, "승인");

    private final int code;      // DB에 저장될 숫자 (0, 1, 2)
    private final String desc;   // 설명

    // ★ 핵심: 문자열("APPROVED") -> 숫자(2) 변환 메서드
    public static int toCode(String statusStr) {
        if (statusStr == null) return PENDING.code; // 기본값

        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(statusStr)) // 대소문자 무시 비교
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 상태 값입니다: " + statusStr))
                .getCode();
    }
}