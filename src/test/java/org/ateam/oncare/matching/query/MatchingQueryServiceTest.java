package org.ateam.oncare.matching.query;

import org.ateam.oncare.matching.query.dto.*;
import org.ateam.oncare.matching.query.mapper.MatchingQueryMapper;
import org.ateam.oncare.matching.query.service.MatchingQueryService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MatchingQueryServiceTest {

    @Autowired private MatchingQueryService matchingQueryService;
    @Autowired private MatchingQueryMapper mapper;
    @Autowired private JdbcTemplate jdbcTemplate;

    // ===========================
    // CASE 1: 수급자 기준
    // ===========================

    @Test
    void sort_case1_beneficiary_TOTAL() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        List<CareWorkerCardDto> cards = matchingQueryService.getCandidateCareWorkers(beneficiaryId, "TOTAL");
        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "TOTAL");
    }

    @Test
    void sort_case1_beneficiary_TAG() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        List<CareWorkerCardDto> cards = matchingQueryService.getCandidateCareWorkers(beneficiaryId, "TAG");
        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "TAG");
    }

    @Test
    void sort_case1_beneficiary_DIST() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        List<CareWorkerCardDto> cards = matchingQueryService.getCandidateCareWorkers(beneficiaryId, "DIST");
        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "DIST");
    }

    // ===========================
    // CASE 2: 방문일정 기준
    // ===========================

    @Test
    void sort_case2_visit_schedule_TOTAL() {
        Long vsId = pickAnyVisitScheduleIdOrNull();
        Assumptions.assumeTrue(vsId != null, "visit_schedule 데이터가 없어서 테스트 스킵");

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT beneficiary_id, start_dt, end_dt FROM visit_schedule WHERE vs_id = ?",
                vsId
        );

        Long beneficiaryId = ((Number) row.get("beneficiary_id")).longValue();
        String startDt = Objects.toString(row.get("start_dt"), null);
        String endDt = Objects.toString(row.get("end_dt"), null);

        Assumptions.assumeTrue(startDt != null && endDt != null, "vs_id의 필수값이 없어 테스트 스킵");

        List<CareWorkerCardDto> cards =
                matchingQueryService.getVisitTimeAvailableCareWorkers(vsId, startDt, endDt, "TOTAL");

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "TOTAL");
    }

    @Test
    void sort_case2_visit_schedule_TAG() {
        Long vsId = pickAnyVisitScheduleIdOrNull();
        Assumptions.assumeTrue(vsId != null, "visit_schedule 데이터가 없어서 테스트 스킵");

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT beneficiary_id, start_dt, end_dt FROM visit_schedule WHERE vs_id = ?",
                vsId
        );

        Long beneficiaryId = ((Number) row.get("beneficiary_id")).longValue();
        String startDt = Objects.toString(row.get("start_dt"), null);
        String endDt = Objects.toString(row.get("end_dt"), null);

        Assumptions.assumeTrue(startDt != null && endDt != null, "vs_id의 필수값이 없어 테스트 스킵");

        List<CareWorkerCardDto> cards =
                matchingQueryService.getVisitTimeAvailableCareWorkers(vsId, startDt, endDt, "TAG");

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "TAG");
    }

    @Test
    void sort_case2_visit_schedule_DIST() {
        Long vsId = pickAnyVisitScheduleIdOrNull();
        Assumptions.assumeTrue(vsId != null, "visit_schedule 데이터가 없어서 테스트 스킵");

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT beneficiary_id, start_dt, end_dt FROM visit_schedule WHERE vs_id = ?",
                vsId
        );

        Long beneficiaryId = ((Number) row.get("beneficiary_id")).longValue();
        String startDt = Objects.toString(row.get("start_dt"), null);
        String endDt = Objects.toString(row.get("end_dt"), null);

        Assumptions.assumeTrue(startDt != null && endDt != null, "vs_id의 필수값이 없어 테스트 스킵");

        List<CareWorkerCardDto> cards =
                matchingQueryService.getVisitTimeAvailableCareWorkers(vsId, startDt, endDt, "DIST");

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "DIST");
    }

    // ===========================
    // CASE 3: 방문 생성 기준
    // ===========================

    @Test
    void sort_case3_visit_create_TOTAL() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        ServiceTypePairDto st = mapper.selectBeneficiaryPrimaryServiceType(beneficiaryId);
        Assumptions.assumeTrue(st != null && st.getServiceTypeId() != null,
                "수급자의 service type을 찾지 못해 테스트 스킵");

        Long serviceTypeId = st.getServiceTypeId();

        String startDt = "2026-01-06 10:00:00";
        String endDt = "2026-01-06 11:00:00";

        List<CareWorkerCardDto> cards =
                matchingQueryService.getCreateVisitAvailableCareWorkers(
                        beneficiaryId, serviceTypeId, startDt, endDt, "TOTAL"
                );

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "TOTAL");
    }

    @Test
    void sort_case3_visit_create_TAG() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        ServiceTypePairDto st = mapper.selectBeneficiaryPrimaryServiceType(beneficiaryId);
        Assumptions.assumeTrue(st != null && st.getServiceTypeId() != null,
                "수급자의 service type을 찾지 못해 테스트 스킵");

        Long serviceTypeId = st.getServiceTypeId();

        String startDt = "2026-01-06 10:00:00";
        String endDt = "2026-01-06 11:00:00";

        List<CareWorkerCardDto> cards =
                matchingQueryService.getCreateVisitAvailableCareWorkers(
                        beneficiaryId, serviceTypeId, startDt, endDt, "TAG"
                );

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "TAG");
    }

    @Test
    void sort_case3_visit_create_DIST() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        ServiceTypePairDto st = mapper.selectBeneficiaryPrimaryServiceType(beneficiaryId);
        Assumptions.assumeTrue(st != null && st.getServiceTypeId() != null,
                "수급자의 service type을 찾지 못해 테스트 스킵");

        Long serviceTypeId = st.getServiceTypeId();

        String startDt = "2026-01-06 10:00:00";
        String endDt = "2026-01-06 11:00:00";

        List<CareWorkerCardDto> cards =
                matchingQueryService.getCreateVisitAvailableCareWorkers(
                        beneficiaryId, serviceTypeId, startDt, endDt, "DIST"
                );

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        assertSortedByMode(beneficiaryId, cards, "DIST");
    }

    // ===========================
    // helpers: pick ids
    // ===========================

    private Long pickAnyBeneficiaryIdOrNull() {
        List<Long> list = jdbcTemplate.query(
                "SELECT id FROM beneficiary WHERE status = 1 ORDER BY id LIMIT 1",
                (rs, rowNum) -> rs.getLong("id")
        );
        return list.isEmpty() ? null : list.get(0);
    }

    private Long pickAnyVisitScheduleIdOrNull() {
        List<Long> list = jdbcTemplate.query(
                "SELECT vs_id FROM visit_schedule ORDER BY vs_id LIMIT 1",
                (rs, rowNum) -> rs.getLong("vs_id")
        );
        return list.isEmpty() ? null : list.get(0);
    }

    // ===========================
    // core assertion (모드별)
    // ===========================

    private void assertSortedByMode(Long beneficiaryId, List<CareWorkerCardDto> cards, String mode) {
        List<Long> sortedIds = cards.stream()
                .filter(Objects::nonNull)
                .map(CareWorkerCardDto::getCareWorkerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertTrue(sortedIds.size() >= 2, "정렬 검증을 위한 후보가 2명 미만");

        // beneficiary 좌표 (없으면 Double.MAX_VALUE로 처리 -> null last 룰에 의해 뒤로 밀림)
        LatLngDto b = mapper.selectBeneficiaryLatLng(beneficiaryId);
        Double bLat = (b == null) ? null : b.getLat();
        Double bLng = (b == null) ? null : b.getLng();

        // overlapCount map
        Map<Long, Integer> overlapMap = mapper.selectTagOverlapCounts(beneficiaryId, sortedIds).stream()
                .collect(Collectors.toMap(
                        TagOverlapCountDto::getCareWorkerId,
                        dto -> dto.getOverlapCount() == null ? 0 : dto.getOverlapCount(),
                        (a, c) -> a
                ));

        // careworker 좌표 map
        Map<Long, LatLngDto> cwLatLngMap = mapper.selectCareWorkerLatLngByIds(sortedIds).stream()
                .collect(Collectors.toMap(
                        CareWorkerLatLngDto::getCareWorkerId,
                        dto -> {
                            LatLngDto ll = new LatLngDto();
                            ll.setLat(dto.getLat());
                            ll.setLng(dto.getLng());
                            return ll;
                        },
                        (a, c) -> a
                ));

        // card 점수 map(서비스가 applyScores로 넣은 값 검증용)
        Map<Long, CareWorkerCardDto> cardMap = cards.stream()
                .filter(Objects::nonNull)
                .filter(c -> c.getCareWorkerId() != null)
                .collect(Collectors.toMap(
                        CareWorkerCardDto::getCareWorkerId,
                        c -> c,
                        (a, c) -> a
                ));

        for (int i = 0; i < sortedIds.size() - 1; i++) {
            Long leftId = sortedIds.get(i);
            Long rightId = sortedIds.get(i + 1);

            int leftOv = overlapMap.getOrDefault(leftId, 0);
            int rightOv = overlapMap.getOrDefault(rightId, 0);

            double leftDist = distanceKmOrMax(bLat, bLng, cwLatLngMap.get(leftId));
            double rightDist = distanceKmOrMax(bLat, bLng, cwLatLngMap.get(rightId));

            CareWorkerCardDto left = cardMap.get(leftId);
            CareWorkerCardDto right = cardMap.get(rightId);

            int leftTagScore = (left == null || left.getTagScore() == null) ? (leftOv * 2) : left.getTagScore();
            int rightTagScore = (right == null || right.getTagScore() == null) ? (rightOv * 2) : right.getTagScore();

            // distanceScore는 서비스에서 geoMissing이면 null, 아니면 10-floor(dist/2)
            Integer leftDistScoreView = (left == null) ? null : left.getDistanceScore();
            Integer rightDistScoreView = (right == null) ? null : right.getDistanceScore();

            boolean leftGeoMissing = leftDist == Double.MAX_VALUE;
            boolean rightGeoMissing = rightDist == Double.MAX_VALUE;

            int leftDistScoreForSum = leftGeoMissing ? 0 : (leftDistScoreView == null ? 0 : leftDistScoreView);
            int rightDistScoreForSum = rightGeoMissing ? 0 : (rightDistScoreView == null ? 0 : rightDistScoreView);

            int leftTotalView = leftTagScore + leftDistScoreForSum;
            int rightTotalView = rightTagScore + rightDistScoreForSum;

            // TOTAL 정렬용: geoMissing이면 null 취급(맨 뒤)
            Integer leftTotalSort = leftGeoMissing ? null : leftTotalView;
            Integer rightTotalSort = rightGeoMissing ? null : rightTotalView;

            switch (normalizeMode(mode)) {
                case "DIST" -> {
                    // distanceKm ASC (null last) -> id ASC
                    int distCmp = compareDistAscNullLast(leftDist, rightDist);
                    if (distCmp != 0) {
                        assertTrue(distCmp <= 0, msg(i, mode, leftId, rightId, leftDist, rightDist));
                        continue;
                    }
                    assertTrue(leftId <= rightId, "DIST: id ASC 위반 idx=" + i + " left=" + leftId + " right=" + rightId);
                }
                case "TAG" -> {
                    // overlap DESC -> distanceKm ASC(null last) -> id ASC
                    if (leftOv != rightOv) {
                        assertTrue(leftOv >= rightOv, "TAG: overlap DESC 위반 idx=" + i
                                + " left(id=" + leftId + ", ov=" + leftOv + ") right(id=" + rightId + ", ov=" + rightOv + ")");
                        continue;
                    }

                    int distCmp = compareDistAscNullLast(leftDist, rightDist);
                    if (distCmp != 0) {
                        assertTrue(distCmp <= 0, msg(i, mode, leftId, rightId, leftDist, rightDist));
                        continue;
                    }

                    assertTrue(leftId <= rightId, "TAG: id ASC 위반 idx=" + i + " left=" + leftId + " right=" + rightId);
                }
                default -> {
                    // TOTAL: totalScoreSort DESC(null last) -> distanceKm ASC(null last) -> id ASC
                    int totalCmp = compareTotalDescNullLast(leftTotalSort, rightTotalSort);
                    if (totalCmp != 0) {
                        assertTrue(totalCmp <= 0, "TOTAL: totalScoreSort DESC(null last) 위반 idx=" + i
                                + " left(id=" + leftId + ", totalSort=" + leftTotalSort + ") right(id=" + rightId + ", totalSort=" + rightTotalSort + ")");
                        continue;
                    }

                    int distCmp = compareDistAscNullLast(leftDist, rightDist);
                    if (distCmp != 0) {
                        assertTrue(distCmp <= 0, msg(i, mode, leftId, rightId, leftDist, rightDist));
                        continue;
                    }

                    assertTrue(leftId <= rightId, "TOTAL: id ASC 위반 idx=" + i + " left=" + leftId + " right=" + rightId);
                }
            }
        }
    }

    private String normalizeMode(String mode) {
        if (mode == null) return "TOTAL";
        String v = mode.trim().toUpperCase();
        return switch (v) {
            case "TAG" -> "TAG";
            case "DIST" -> "DIST";
            default -> "TOTAL";
        };
    }

    // distance ASC, null(last) = Double.MAX_VALUE 처리
    private int compareDistAscNullLast(double leftDist, double rightDist) {
        double a = (leftDist == Double.MAX_VALUE) ? Double.MAX_VALUE : leftDist;
        double b = (rightDist == Double.MAX_VALUE) ? Double.MAX_VALUE : rightDist;
        return Double.compare(a, b);
    }

    // total DESC, null(last)
    private int compareTotalDescNullLast(Integer leftTotalSort, Integer rightTotalSort) {
        if (leftTotalSort == null && rightTotalSort == null) return 0;
        if (leftTotalSort == null) return 1;   // left 뒤로
        if (rightTotalSort == null) return -1; // right 뒤로
        return Integer.compare(rightTotalSort, leftTotalSort); // DESC
    }

    private String msg(int idx, String mode, Long leftId, Long rightId, double leftDist, double rightDist) {
        return mode + ": distance ASC(null last) 위반 idx=" + idx
                + " left(id=" + leftId + ", dist=" + leftDist + ") right(id=" + rightId + ", dist=" + rightDist + ")";
    }

    private double distanceKmOrMax(Double bLat, Double bLng, LatLngDto cw) {
        if (bLat == null || bLng == null) return Double.MAX_VALUE;
        if (cw == null || cw.getLat() == null || cw.getLng() == null) return Double.MAX_VALUE;
        return haversineKm(bLat, bLng, cw.getLat(), cw.getLng());
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0088;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}