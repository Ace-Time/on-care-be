package org.ateam.oncare.matching.geo;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DongCoordinateLoader {

    private static final String CSV_PATH = "geo/seoul_dong_latlng.csv";

    /**
     * 1) exactKey: "서울특별시|중구|명동" 같은 형태(동 정규화 포함)
     */
    private final Map<String, DongCoordinateDto> exactMap = new HashMap<>();

    /**
     * 2) looseKey: 동 기호/구분자 제거(숫자/한글만 남김) 버전
     *    예: "종로1234가동", "상계34동"
     */
    private final Map<String, DongCoordinateDto> looseMap = new HashMap<>();

    /**
     * 3) 후보 검색용(구 단위): "중구" -> 해당 구의 동 row 목록
     */
    private final Map<String, List<Row>> rowsByGu = new HashMap<>();

    private static class Row {
        final String sido;
        final String gu;
        final String dongNorm;   // normalizeDong()
        final String dongLoose;  // normalizeDongLoose()
        final double lat;
        final double lng;
        final double areaWeight; // 면적 없으면 1.0

        Row(String sido, String gu, String dongNorm, String dongLoose,
            double lat, double lng, double areaWeight) {
            this.sido = sido;
            this.gu = gu;
            this.dongNorm = dongNorm;
            this.dongLoose = dongLoose;
            this.lat = lat;
            this.lng = lng;
            this.areaWeight = areaWeight;
        }
    }

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

                // 최소: 코드, 동명, 구, lat, lng (그리고 area가 있으면 6번째)
                if (t.length < 5) continue;

                String sido = "서울특별시";

                String dongRaw = safe(t, 1);
                String guRaw   = safe(t, 2);

                String dongNorm  = normalizeDong(dongRaw);
                String dongLoose = normalizeDongLoose(dongRaw);
                String gu        = normalizeGu(guRaw);

                if (dongNorm.isEmpty() || gu.isEmpty()) continue;

                String latStr = safe(t, 3);
                String lngStr = safe(t, 4);
                if (latStr.isBlank() || lngStr.isBlank()) continue;

                double lat = Double.parseDouble(latStr.trim());
                double lng = Double.parseDouble(lngStr.trim());

                // area: CSV에 6번째 컬럼(영역_면적)이 있으면 가중치로 사용, 없으면 1.0
                double areaWeight = 1.0;
                if (t.length >= 6) {
                    String areaStr = safe(t, 5);
                    if (!areaStr.isBlank()) {
                        try {
                            areaWeight = Double.parseDouble(areaStr.trim());
                            if (areaWeight <= 0) areaWeight = 1.0;
                        } catch (Exception ignored) {
                            areaWeight = 1.0;
                        }
                    }
                }

                // exact / loose 인덱스 채우기
                String exactKey = buildKey(sido, gu, dongNorm);
                exactMap.put(exactKey, new DongCoordinateDto(sido, gu, dongNorm, lat, lng));

                String looseKey = buildKey(sido, gu, dongLoose);
                // looseMap은 충돌 가능성이 있으니 "첫 값 유지"로 둠(충돌은 드물고, 있어도 fallback이 있음)
                looseMap.putIfAbsent(looseKey, new DongCoordinateDto(sido, gu, dongNorm, lat, lng));

                // 후보용 row 저장
                rowsByGu.computeIfAbsent(gu, k -> new ArrayList<>())
                        .add(new Row(sido, gu, dongNorm, dongLoose, lat, lng, areaWeight));
            }

        } catch (Exception e) {
            throw new IllegalStateException("행정동 좌표 CSV 로딩 실패: classpath:" + CSV_PATH, e);
        }
    }

    /**
     * find 전략 (순서 중요)
     * 1) exact match: normalizeDong 기준
     * 2) loose match: 기호 제거(숫자/한글만) 기준
     * 3) fallback: "역삼동"처럼 번호 없는 동 -> 같은 구에서 "역삼1동/역삼2동..." 후보를 찾아
     *              면적 가중 평균 좌표로 합쳐서 반환
     */
    public DongCoordinateDto find(String sido, String gu, String dong) {
        String s = normalizeSido(sido);
        String g = normalizeGu(gu);
        String dNorm  = normalizeDong(dong);
        String dLoose = normalizeDongLoose(dong);

        if (s.isEmpty() || g.isEmpty() || dNorm.isEmpty()) return null;

        // 1) exact
        DongCoordinateDto exact = exactMap.get(buildKey(s, g, dNorm));
        if (exact != null) return exact;

        // 2) loose
        DongCoordinateDto loose = looseMap.get(buildKey(s, g, dLoose));
        if (loose != null) return loose;

        // 3) 번호 없는 동 fallback: "역삼동" -> "역삼1동, 역삼2동" 등
        //    (조건) 숫자가 없고 "동"으로 끝나는 경우에만 시도
        if (endsWithDong(dNorm) && !containsDigit(dNorm)) {
            String base = dNorm.substring(0, dNorm.length() - 1); // "역삼동" -> "역삼"
            List<Row> candidates = rowsByGu.getOrDefault(g, List.of()).stream()
                    .filter(r -> r != null
                            && r.dongNorm != null
                            && r.dongNorm.startsWith(base)
                            && endsWithDong(r.dongNorm)
                            && containsDigit(r.dongNorm)) // 보통 "역삼1동" 형태
                    .collect(Collectors.toList());

            if (!candidates.isEmpty()) {
                // 면적 가중 평균
                double sumW = 0.0;
                double sumLat = 0.0;
                double sumLng = 0.0;

                for (Row r : candidates) {
                    double w = (r.areaWeight <= 0) ? 1.0 : r.areaWeight;
                    sumW += w;
                    sumLat += (r.lat * w);
                    sumLng += (r.lng * w);
                }

                double lat = sumLat / sumW;
                double lng = sumLng / sumW;

                // 반환 dto의 dong은 입력(정규화) 기준으로 넣어두면 로그/디버깅이 편함
                return new DongCoordinateDto(s, g, dNorm, lat, lng);
            }
        }

        return null;
    }

    private String buildKey(String sido, String gu, String dong) {
        return sido + "|" + gu + "|" + dong;
    }

    private String safe(String[] t, int idx) {
        if (t == null || idx < 0 || idx >= t.length) return "";
        return t[idx] == null ? "" : t[idx].trim().replace("\uFEFF", "");
    }

    private String normalizeSido(String sido) {
        if (sido == null) return "서울특별시";
        String s = sido.trim().replace("\uFEFF", "");
        return s.isEmpty() ? "서울특별시" : s;
    }

    private String normalizeGu(String gu) {
        return gu == null ? "" : gu.trim().replace("\uFEFF", "");
    }

    /**
     * normalizeDong:
     * - CSV/주소에서 오는 이상 문자 통일
     * - "상계3?4동", "상계3·4동", "상계3ㆍ4동", "상계3-4동", "상계3 4동" 등을 최대한 같은 형태로
     */
    private String normalizeDong(String dong) {
        if (dong == null) return "";
        String s = dong.trim().replace("\uFEFF", "");

        // 흔한 구분자/깨짐 문자 통일
        s = s.replace('?', '.')    // CSV 깨짐(?) -> 점
                .replace('·', '.')
                .replace('ㆍ', '.')
                .replace('-', '.')
                .replace('~', '.')
                .replace('∼', '.')
                .replace('–', '.')
                .replace('—', '.');

        // 공백 제거
        s = s.replaceAll("\\s+", "");

        // 연속 점 정리
        s = s.replaceAll("\\.+", ".");

        return s;
    }

    /**
     * normalizeDongLoose:
     * - 동 비교를 더 강하게: 한글/숫자만 남김
     * - 예: "종로1.2.3.4가동" -> "종로1234가동"
     */
    private String normalizeDongLoose(String dong) {
        String s = normalizeDong(dong);
        if (s.isEmpty()) return "";
        return s.replaceAll("[^0-9가-힣]", "");
    }

    private boolean containsDigit(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) return true;
        }
        return false;
    }

    private boolean endsWithDong(String s) {
        return s != null && s.endsWith("동");
    }
}