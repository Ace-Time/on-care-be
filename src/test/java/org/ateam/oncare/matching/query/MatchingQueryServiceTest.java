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

    /**
     * (케이스1) 수급자 기준 추천 정렬 테스트
     * 1) 태그 겹침 수 DESC
     * 2) 거리 ASC
     * 3) id ASC
     */
    @Test
    void sort_case1_beneficiary_based_tag_then_distance_then_id() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        List<CareWorkerCardDto> cards = matchingQueryService.getCandidateCareWorkers(beneficiaryId);
        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        List<Long> sortedIds = cards.stream()
                .map(CareWorkerCardDto::getCareWorkerId)
                .collect(Collectors.toList());

        assertSortedByOverlapThenDistanceThenId(beneficiaryId, sortedIds);
    }

    /**
     * (케이스2) 방문일정 기준 추천 정렬 테스트
     * - vs_id 1개 집어서 start_dt/end_dt 그대로 넣어 정렬 검증
     */
    @Test
    void sort_case2_visit_schedule_based_tag_then_distance_then_id() {
        Long vsId = pickAnyVisitScheduleIdOrNull();
        Assumptions.assumeTrue(vsId != null, "visit_schedule 데이터가 없어서 테스트 스킵");

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT beneficiary_id, start_dt, end_dt FROM visit_schedule WHERE vs_id = ?",
                vsId
        );

        Long beneficiaryId = ((Number) row.get("beneficiary_id")).longValue();
        String startDt = Objects.toString(row.get("start_dt"), null);
        String endDt = Objects.toString(row.get("end_dt"), null);

        Assumptions.assumeTrue(beneficiaryId != null && startDt != null && endDt != null,
                "vs_id의 필수값이 없어 테스트 스킵");

        List<CareWorkerCardDto> cards = matchingQueryService.getVisitTimeAvailableCareWorkers(vsId, startDt, endDt);
        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        List<Long> sortedIds = cards.stream()
                .map(CareWorkerCardDto::getCareWorkerId)
                .collect(Collectors.toList());

        assertSortedByOverlapThenDistanceThenId(beneficiaryId, sortedIds);
    }

    /**
     * (케이스3) 방문일정 "생성" 기준 추천 정렬 테스트
     */
    @Test
    void sort_case3_visit_create_based_tag_then_distance_then_id() {
        Long beneficiaryId = pickAnyBeneficiaryIdOrNull();
        Assumptions.assumeTrue(beneficiaryId != null, "beneficiary 데이터가 없어서 테스트 스킵");

        ServiceTypePairDto st = mapper.selectBeneficiaryPrimaryServiceType(beneficiaryId);
        Assumptions.assumeTrue(st != null && st.getServiceTypeId() != null,
                "수급자의 service type을 찾지 못해 테스트 스킵");

        Long serviceTypeId = st.getServiceTypeId();

        // 시간대 변경
        String startDt = "2026-01-06 10:00:00";
        String endDt = "2026-01-06 11:00:00";

        List<CareWorkerCardDto> cards =
                matchingQueryService.getCreateVisitAvailableCareWorkers(beneficiaryId, serviceTypeId, startDt, endDt);

        Assumptions.assumeTrue(cards != null && cards.size() >= 2, "후보 요양보호사가 2명 미만이라 정렬 검증 스킵");

        List<Long> sortedIds = cards.stream()
                .map(CareWorkerCardDto::getCareWorkerId)
                .collect(Collectors.toList());

        assertSortedByOverlapThenDistanceThenId(beneficiaryId, sortedIds);
    }

    // ===========================
    // helpers
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

    /**
     * 서비스 로직(sortByTagThenDistance)와 동일한 규칙을 테스트에서 재계산해서 검증
     */
    private void assertSortedByOverlapThenDistanceThenId(Long beneficiaryId, List<Long> sortedIds) {
        assertNotNull(sortedIds);
        assertTrue(sortedIds.size() >= 2);

        // beneficiary 좌표
        LatLngDto b = mapper.selectBeneficiaryLatLng(beneficiaryId);
        Double bLat = b == null ? null : b.getLat();
        Double bLng = b == null ? null : b.getLng();
        Assumptions.assumeTrue(bLat != null && bLng != null,
                "beneficiary lat/lng가 NULL이라 거리 기반 정렬 검증 스킵(geo 채움이 안된 상태)");

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

        // 인접 원소끼리 규칙 위반 없는지 확인
        for (int i = 0; i < sortedIds.size() - 1; i++) {
            Long left = sortedIds.get(i);
            Long right = sortedIds.get(i + 1);

            int leftOv = overlapMap.getOrDefault(left, 0);
            int rightOv = overlapMap.getOrDefault(right, 0);

            // 1) overlap DESC
            if (leftOv != rightOv) {
                assertTrue(
                        leftOv >= rightOv,
                        String.format(
                                "overlap DESC 위반: idx=%d left(id=%d ov=%d) right(id=%d ov=%d)",
                                i, left, leftOv, right, rightOv
                        )
                );
                continue;
            }

            // 2) distance ASC
            double leftDist = distanceKmOrMax(bLat, bLng, cwLatLngMap.get(left));
            double rightDist = distanceKmOrMax(bLat, bLng, cwLatLngMap.get(right));

            if (Double.compare(leftDist, rightDist) != 0) {
                assertTrue(
                        leftDist <= rightDist,
                        String.format(
                                "distance ASC 위반: idx=%d left(id=%d dist=%.6f) right(id=%d dist=%.6f)",
                                i, left, leftDist, right, rightDist
                        )
                );
                continue;
            }

            // 3) id ASC
            assertTrue(
                    left <= right,
                    String.format("id ASC 위반: idx=%d left=%d right=%d", i, left, right)
            );
        }
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