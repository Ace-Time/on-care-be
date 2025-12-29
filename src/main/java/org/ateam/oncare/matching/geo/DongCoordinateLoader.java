package org.ateam.oncare.matching.geo;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class DongCoordinateLoader {

    private static final String CSV_PATH = "geo/seoul_dong_latlng.csv";
    private final Map<String, DongCoordinateDto> dongMap = new HashMap<>();

    @PostConstruct
    public void load() {
        try (
                var is = new ClassPathResource(CSV_PATH).getInputStream();
                var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {
            br.readLine(); // header skip

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                line = line.replace("\uFEFF", ""); // BOM 방어
                String[] t = line.split(",", -1);
                if (t.length < 5) continue;

                String dong = normalizeDong(t[1]);
                String gu   = normalizeGu(t[2]);

                if (dong.isEmpty() || gu.isEmpty() || t[3].isBlank() || t[4].isBlank()) continue;

                double lat = Double.parseDouble(t[3].trim());
                double lng = Double.parseDouble(t[4].trim());

                String sido = "서울특별시";
                String key = buildKey(sido, gu, dong);

                dongMap.put(key, new DongCoordinateDto(sido, gu, dong, lat, lng));
            }
        } catch (Exception e) {
            throw new IllegalStateException("행정동 좌표 CSV 로딩 실패: classpath:" + CSV_PATH, e);
        }
    }

    public DongCoordinateDto find(String sido, String gu, String dong) {
        return dongMap.get(buildKey(sido, normalizeGu(gu), normalizeDong(dong)));
    }

    private String buildKey(String sido, String gu, String dong) {
        return sido + "|" + gu + "|" + dong;
    }

    private String normalizeGu(String gu) {
        return gu == null ? "" : gu.trim().replace("\uFEFF", "");
    }

    private String normalizeDong(String dong) {
        if (dong == null) return "";
        String s = dong.trim().replace("\uFEFF", "");

        // ✅ CSV 깨짐(?) / 중점(·, ㆍ) / 점(.) 표기 통일
        s = s.replace('?', '.')
                .replace('·', '.')
                .replace('ㆍ', '.');

        // 연속 점 정리 (예: 1..2 -> 1.2)
        s = s.replaceAll("\\.+", ".");

        return s;
    }
}