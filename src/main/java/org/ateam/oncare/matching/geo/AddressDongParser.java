package org.ateam.oncare.matching.geo;

import java.util.Optional;

public final class AddressDongParser {

    private AddressDongParser() {}

    public static Optional<SggEmd> parse(String address) {
        if (address == null || address.isBlank()) return Optional.empty();

        // 공백 기준 토큰 분리
        String[] tokens = address.trim().split("\\s+");

        String sido = null;
        String gu = null;
        String dong = null;

        for (String raw : tokens) {
            String t = raw.trim();
            if (t.isEmpty()) continue;

            // 시/도 정규화 (서울/서울특별시 둘 다 처리)
            if (t.startsWith("서울")) sido = "서울특별시";

            // 구 추출 (ex: 강남구)
            if (t.endsWith("구") || t.endsWith("군")) gu = t;

            // 동 추출 (ex: 역삼동, 서초1동 등)
            // (주의: '로', '길', '읍', '면'은 행정동이 아니니 제외)
            if (t.endsWith("동") && !t.endsWith("로동")) dong = t;
        }

        if (sido == null) sido = "서울특별시"; // CSV가 서울만이라면 기본값

        if (gu == null || dong == null) return Optional.empty();
        return Optional.of(new SggEmd(sido, gu, dong));
    }

    public record SggEmd(String sido, String gu, String dong) {}
}