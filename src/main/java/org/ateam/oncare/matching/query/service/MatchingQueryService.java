package org.ateam.oncare.matching.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.matching.geo.AddressDongParser;
import org.ateam.oncare.matching.geo.DongCoordinateLoader;
import org.ateam.oncare.matching.query.dto.*;
import org.ateam.oncare.matching.query.mapper.MatchingQueryMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingQueryService {

    private final MatchingQueryMapper mapper;

    // ✅ CSV 로더 주입
    private final DongCoordinateLoader dongCoordinateLoader;

    // =========================
    // sort 모드
    // =========================
    private enum SortMode {
        TOTAL, TAG, DIST;

        static SortMode from(String s) {
            if (s == null || s.isBlank()) return TOTAL;
            String v = s.trim().toUpperCase();
            return switch (v) {
                case "TAG" -> TAG;
                case "DIST" -> DIST;
                case "TOTAL" -> TOTAL;
                default -> TOTAL;
            };
        }
    }

    // =========================
    // 후보 ID 필터링(케이스별)
    // =========================
    public List<Long> selectFinalCandidateCareWorkerIds(Long beneficiaryId) {

        var schedules = mapper.selectBeneficiarySchedules(beneficiaryId);
        log.info("[SCHEDULE] beneficiaryId={} count={}", beneficiaryId, schedules == null ? 0 : schedules.size());

        if (schedules == null || schedules.isEmpty()) {
            log.warn("[STOP] No schedules for beneficiaryId={}", beneficiaryId);
            return List.of();
        }

        Set<Long> timeIntersect = schedules.stream()
                .map(s -> mapper.selectAvailableCareWorkerIds(
                        beneficiaryId,
                        s.getDay(),
                        s.getStartTime(),
                        s.getEndTime()
                ))
                .map(list -> list.stream()
                        .map(CareWorkerIdDto::getCareWorkerId)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                })
                .orElseGet(LinkedHashSet::new);

        log.info("[TIME INTERSECT] count={}", timeIntersect.size());
        if (timeIntersect.isEmpty()) return List.of();

        Set<Long> serviceTypeSet = mapper.selectCareWorkerIdsByServiceType(beneficiaryId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.info("[SERVICE TYPE SET] count={}", serviceTypeSet.size());
        timeIntersect.retainAll(serviceTypeSet);
        if (timeIntersect.isEmpty()) return List.of();

        Set<Long> riskCertSet = mapper.selectCareWorkerIdsByRiskCertificates(beneficiaryId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.info("[RISK->CERT SET] count={}", riskCertSet.size());
        timeIntersect.retainAll(riskCertSet);
        if (timeIntersect.isEmpty()) return List.of();

        Long assignedId = mapper.selectAssignedCareWorkerId(beneficiaryId);
        if (assignedId != null) {
            boolean removed = timeIntersect.remove(assignedId);
            log.info("[EXCLUDE ASSIGNED] beneficiaryId={} assignedId={} removed={}", beneficiaryId, assignedId, removed);
        }

        var result = new ArrayList<>(timeIntersect);
        log.info("[FINAL] beneficiaryId={} finalCount={}", beneficiaryId, result.size());
        return result;
    }

    public List<Long> selectFinalCandidateCareWorkerIdsByVisitSchedule(
            Long vsId, String startDt, String endDt
    ) {
        Long beneficiaryId = mapper.selectVisitScheduleBeneficiaryId(vsId);
        if (beneficiaryId == null) return List.of();

        Set<Long> timeSet = mapper.selectAvailableCareWorkerIdsByVisitSchedule(vsId, startDt, endDt).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (timeSet.isEmpty()) return List.of();

        Set<Long> serviceTypeSet = mapper.selectCareWorkerIdsByVisitServiceType(vsId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        timeSet.retainAll(serviceTypeSet);
        if (timeSet.isEmpty()) return List.of();

        Set<Long> riskCertSet = mapper.selectCareWorkerIdsByRiskCertificates(beneficiaryId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        timeSet.retainAll(riskCertSet);
        if (timeSet.isEmpty()) return List.of();

        Long currentCareWorkerId = mapper.selectCareWorkerIdByVisitScheduleId(vsId);
        if (currentCareWorkerId != null) timeSet.remove(currentCareWorkerId);

        return new ArrayList<>(timeSet);
    }

    public List<Long> selectFinalCandidateCareWorkerIdsForCreateVisit(
            Long beneficiaryId, Long serviceTypeId, String startDt, String endDt
    ) {
        if (beneficiaryId == null) return List.of();
        if (serviceTypeId == null) return List.of();
        if (startDt == null || startDt.isBlank() || endDt == null || endDt.isBlank()) return List.of();

        int conflict = mapper.existsBeneficiaryVisitConflict(beneficiaryId, startDt, endDt);
        if (conflict == 1) {
            log.info("[STOP] beneficiary time conflict beneficiaryId={} startDt={} endDt={}",
                    beneficiaryId, startDt, endDt);
            return List.of();
        }

        Set<Long> serviceTypeSet = mapper.selectCareWorkerIdsByServiceTypeId(serviceTypeId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (serviceTypeSet.isEmpty()) return List.of();

        Set<Long> riskCertSet = mapper.selectCareWorkerIdsByRiskCertificates(beneficiaryId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        serviceTypeSet.retainAll(riskCertSet);
        if (serviceTypeSet.isEmpty()) return List.of();

        Set<Long> noVisitConflictSet = mapper.selectAvailableCareWorkerIdsByVisitTime(startDt, endDt).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        serviceTypeSet.retainAll(noVisitConflictSet);
        if (serviceTypeSet.isEmpty()) return List.of();

        return new ArrayList<>(serviceTypeSet);
    }

    // =========================
    // Beneficiary
    // =========================
    public List<BeneficiarySummaryDto> getBeneficiariesSummary(int page, int size, String keyword, String assigned) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = safePage * safeSize;

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        String a = (assigned == null || assigned.trim().isEmpty()) ? null : assigned.trim();

        return mapper.selectBeneficiariesSummary(offset, safeSize, q, a);
    }

    public BeneficiaryPageResponse getBeneficiariesPage(int page, int size, String keyword, String assigned) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = safePage * safeSize;

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        String a = (assigned == null || assigned.trim().isEmpty()) ? null : assigned.trim().toUpperCase();
        if (!"Y".equals(a) && !"N".equals(a)) a = null;

        List<BeneficiarySummaryDto> list =
                mapper.selectBeneficiariesSummary(offset, safeSize, q, a);

        long total = mapper.countBeneficiaries(q, a);

        return new BeneficiaryPageResponse(list, safePage, safeSize, total);
    }

    public BeneficiaryDetailDto getBeneficiaryDetail(Long beneficiaryId) {
        var detail = mapper.selectBeneficiaryDetail(beneficiaryId);
        if (detail == null) {
            log.warn("[BENEFICIARY DETAIL] beneficiaryId={} NOT FOUND", beneficiaryId);
            return null;
        }

        var st = mapper.selectBeneficiaryPrimaryServiceType(beneficiaryId);
        if (st != null) {
            detail.setServiceTypeId(st.getServiceTypeId());
            detail.setServiceTypeName(st.getServiceTypeName());
        }

        detail.setServiceTypes(mapper.selectBeneficiaryServiceTypes(beneficiaryId));

        log.info("[BENEFICIARY DETAIL] beneficiaryId={} name={} serviceTypeId={}",
                beneficiaryId, detail.getName(), detail.getServiceTypeId());

        return detail;
    }

    // =========================
    // CareWorker detail
    // =========================
    public CareWorkerDetailDto getCareWorkerDetail(Long careWorkerId) {
        var detail = mapper.selectCareWorkerDetail(careWorkerId);
        if (detail == null) {
            log.warn("[CAREWORKER DETAIL] careWorkerId={} NOT FOUND", careWorkerId);
            return null;
        }
        log.info("[CAREWORKER DETAIL] careWorkerId={} name={}", careWorkerId, detail.getName());
        return detail;
    }

    /**
     * ⚠️ 중요: mapper SQL이 ORDER BY cw.id 라도,
     * 여기서 ids 순서대로 카드 리스트를 재정렬해서 반환합니다.
     */
    public List<CareWorkerCardDto> getCareWorkerCardsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        var list = mapper.selectCareWorkerCardsByIds(ids);
        if (list == null || list.isEmpty()) return List.of();

        Map<Long, CareWorkerCardDto> map = list.stream()
                .collect(Collectors.toMap(
                        CareWorkerCardDto::getCareWorkerId,
                        dto -> dto,
                        (a, b) -> a
                ));

        List<CareWorkerCardDto> ordered = new ArrayList<>(ids.size());
        for (Long id : ids) {
            CareWorkerCardDto dto = map.get(id);
            if (dto != null) ordered.add(dto);
        }

        log.info("[CAREWORKER CARDS] requestedIds={} returned={} (ordered)", ids.size(), ordered.size());
        return ordered;
    }

    // =========================
    // 케이스1/2/3: 전체 리스트(정렬 포함)
    // =========================

    /**
     * ✅ (케이스1) 수급자 기준 추천: 필터링 -> 정렬 -> 카드 (+점수 주입)
     */
    public List<CareWorkerCardDto> getCandidateCareWorkers(Long beneficiaryId, String sort) {
        var ids = selectFinalCandidateCareWorkerIds(beneficiaryId);

        ScoreResult scoreResult = sortByModeWithScore(beneficiaryId, ids, sort);
        List<CareWorkerCardDto> cards = getCareWorkerCardsByIds(scoreResult.getSortedIds());
        applyScores(cards, scoreResult.getScoreMap());

        return cards;
    }

    /**
     * ✅ (케이스3) 생성 기준 추천: 필터링 -> 정렬 -> 카드 (+점수 주입)
     */
    public List<CareWorkerCardDto> getCreateVisitAvailableCareWorkers(
            Long beneficiaryId, Long serviceTypeId, String startDt, String endDt, String sort
    ) {
        var ids = selectFinalCandidateCareWorkerIdsForCreateVisit(beneficiaryId, serviceTypeId, startDt, endDt);

        ScoreResult scoreResult = sortByModeWithScore(beneficiaryId, ids, sort);
        List<CareWorkerCardDto> cards = getCareWorkerCardsByIds(scoreResult.getSortedIds());
        applyScores(cards, scoreResult.getScoreMap());

        return cards;
    }

    /**
     * ✅ (케이스2) 방문일정 기준 추천: 필터링 -> 정렬 -> 카드 (+점수 주입)
     */
    public List<CareWorkerCardDto> getVisitTimeAvailableCareWorkers(
            Long vsId, String startDt, String endDt, String sort
    ) {
        Long beneficiaryId = mapper.selectVisitScheduleBeneficiaryId(vsId);
        if (beneficiaryId == null) return List.of();

        var ids = selectFinalCandidateCareWorkerIdsByVisitSchedule(vsId, startDt, endDt);

        ScoreResult scoreResult = sortByModeWithScore(beneficiaryId, ids, sort);
        List<CareWorkerCardDto> cards = getCareWorkerCardsByIds(scoreResult.getSortedIds());
        applyScores(cards, scoreResult.getScoreMap());

        return cards;
    }

    // =========================
    // Page (케이스1/2/3)
    // =========================

    public CareWorkerPageResponse getCandidateCareWorkersPage(
            Long beneficiaryId, int page, int size, String keyword, String sort
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        List<CareWorkerCardDto> all = getCandidateCareWorkers(beneficiaryId, sort);

        List<CareWorkerCardDto> filtered;
        if (q == null) {
            filtered = all;
        } else {
            String lower = q.toLowerCase();
            filtered = all.stream()
                    .filter(cw -> cw != null && cw.getName() != null
                            && cw.getName().toLowerCase().contains(lower))
                    .toList();
        }

        long total = filtered.size();

        int from = safePage * safeSize;
        int to = Math.min(from + safeSize, filtered.size());

        List<CareWorkerCardDto> pageList =
                (from >= filtered.size()) ? List.of() : filtered.subList(from, to);

        return new CareWorkerPageResponse(pageList, safePage, safeSize, total);
    }

    public CareWorkerPageResponse getVisitTimeAvailableCareWorkersPage(
            Long vsId, String startDt, String endDt, int page, String sort
    ) {
        int safePage = Math.max(page, 0);
        int size = 3;

        List<CareWorkerCardDto> all = getVisitTimeAvailableCareWorkers(vsId, startDt, endDt, sort);
        if (all == null || all.isEmpty()) {
            return new CareWorkerPageResponse(List.of(), safePage, size, 0);
        }

        long total = all.size();

        int from = safePage * size;
        int to = Math.min(from + size, all.size());

        List<CareWorkerCardDto> pageList =
                (from >= all.size()) ? List.of() : all.subList(from, to);

        return new CareWorkerPageResponse(pageList, safePage, size, total);
    }

    public CareWorkerPageResponse getCreateVisitAvailableCareWorkersPage(
            Long beneficiaryId, Long serviceTypeId, String startDt, String endDt,
            int page, int size, String keyword, String sort
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        List<CareWorkerCardDto> all =
                getCreateVisitAvailableCareWorkers(beneficiaryId, serviceTypeId, startDt, endDt, sort);

        if (all == null || all.isEmpty()) {
            return new CareWorkerPageResponse(List.of(), safePage, safeSize, 0);
        }

        List<CareWorkerCardDto> filtered;
        if (q == null) {
            filtered = all;
        } else {
            String lower = q.toLowerCase();
            filtered = all.stream()
                    .filter(cw -> cw != null && cw.getName() != null
                            && cw.getName().toLowerCase().contains(lower))
                    .toList();
        }

        long total = filtered.size();

        int from = safePage * safeSize;
        int to = Math.min(from + safeSize, filtered.size());

        List<CareWorkerCardDto> pageList =
                (from >= filtered.size()) ? List.of() : filtered.subList(from, to);

        return new CareWorkerPageResponse(pageList, safePage, safeSize, total);
    }

    // =========================
    // 정렬 + 점수 계산
    // =========================

    /**
     * ✅ mode별 정렬
     * - TOTAL: totalScore DESC, (동점) distanceKm ASC, id ASC
     * - TAG: overlapCount DESC, (동점) distanceKm ASC, id ASC
     * - DIST: distanceKm ASC(null last), id ASC
     */
    private ScoreResult sortByModeWithScore(Long beneficiaryId, List<Long> ids, String sort) {
        SortMode mode = SortMode.from(sort);

        // 점수/거리/겹침수는 항상 계산(프론트 표시/디버깅 용이)
        ScoreResult base = buildScoreResult(beneficiaryId, ids);

        List<Long> sorted = new ArrayList<>(base.sortedIds); // base.sortedIds는 ids 그대로(아래에서 정렬)
        Map<Long, ScoreDetail> scoreMap = base.scoreMap;

        Comparator<Long> byIdAsc = Long::compare;

        Comparator<Long> byDistAscNullLast = (a, b) -> {
            ScoreDetail sa = scoreMap.get(a);
            ScoreDetail sb = scoreMap.get(b);
            double da = (sa == null || sa.distanceKm == null) ? Double.MAX_VALUE : sa.distanceKm;
            double db = (sb == null || sb.distanceKm == null) ? Double.MAX_VALUE : sb.distanceKm;
            int cmp = Double.compare(da, db);
            return (cmp != 0) ? cmp : byIdAsc.compare(a, b);
        };

        Comparator<Long> byTotalDesc = (a, b) -> {
            ScoreDetail sa = scoreMap.get(a);
            ScoreDetail sb = scoreMap.get(b);

            // 좌표 없는 사람은 null -> 맨 뒤로
            Integer ta = (sa == null) ? null : sa.totalScoreSort;
            Integer tb = (sb == null) ? null : sb.totalScoreSort;

            int cmp;
            if (ta == null && tb == null) cmp = 0;
            else if (ta == null) cmp = 1;      // a 뒤로
            else if (tb == null) cmp = -1;     // b 뒤로
            else cmp = Integer.compare(tb, ta); // DESC

            if (cmp != 0) return cmp;
            return byDistAscNullLast.compare(a, b);
        };

        Comparator<Long> byTagDesc = (a, b) -> {
            ScoreDetail sa = scoreMap.get(a);
            ScoreDetail sb = scoreMap.get(b);
            int ta = (sa == null || sa.overlapCount == null) ? Integer.MIN_VALUE : sa.overlapCount;
            int tb = (sb == null || sb.overlapCount == null) ? Integer.MIN_VALUE : sb.overlapCount;
            int cmp = Integer.compare(tb, ta); // DESC
            if (cmp != 0) return cmp;
            return byDistAscNullLast.compare(a, b);
        };

        switch (mode) {
            case DIST -> sorted.sort(byDistAscNullLast);
            case TAG -> sorted.sort(byTagDesc);
            case TOTAL -> sorted.sort(byTotalDesc);
        }

        // 로그(상위 10명)
        for (int i = 0; i < Math.min(10, sorted.size()); i++) {
            Long id = sorted.get(i);
            ScoreDetail sd = scoreMap.get(id);

            log.info("[SORT-CHK] mode={} rank={} careWorkerId={} overlap={} distKm={} tagScore={} distScore={} totalView={} totalSort={}",
                    mode,
                    i + 1,
                    id,
                    sd == null ? null : sd.overlapCount,
                    sd == null ? null : sd.distanceKm,
                    sd == null ? null : sd.tagScore,
                    sd == null ? null : sd.distanceScore,
                    sd == null ? null : sd.totalScoreView,
                    sd == null ? null : sd.totalScoreSort
            );
        }

        return new ScoreResult(sorted, scoreMap);
    }

    /**
     * ✅ 점수 + 거리 + 겹침수 맵을 만든다(정렬은 mode에서 별도).
     * - totalScore = tagScore + distanceScore
     * - tagScore: overlapCount * 2
     * - distanceScore: 10 - floor(distanceKm / 2)
     */
    private ScoreResult buildScoreResult(Long beneficiaryId, List<Long> ids) {
        if (beneficiaryId == null) return new ScoreResult(ids == null ? List.of() : ids, Map.of());
        if (ids == null || ids.isEmpty()) return new ScoreResult(List.of(), Map.of());

        // 정렬 전에: lat/lng 없으면 CSV로 채워서 UPDATE
        ensureBeneficiaryGeo(beneficiaryId);
        ensureCareWorkersGeo(ids);

        // 1) 수급자 좌표
        LatLngDto b = mapper.selectBeneficiaryLatLng(beneficiaryId);
        Double bLat = b == null ? null : b.getLat();
        Double bLng = b == null ? null : b.getLng();
        log.info("[GEO] beneficiaryId={} lat={} lng={}", beneficiaryId, bLat, bLng);

        // 2) 태그 겹침수
        Map<Long, Integer> overlapMap = mapper.selectTagOverlapCounts(beneficiaryId, ids).stream()
                .collect(Collectors.toMap(
                        TagOverlapCountDto::getCareWorkerId,
                        dto -> dto.getOverlapCount() == null ? 0 : dto.getOverlapCount(),
                        (a, c) -> a
                ));

        // 3) 후보 좌표
        Map<Long, LatLngDto> careWorkerLatLngMap = mapper.selectCareWorkerLatLngByIds(ids).stream()
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

        // 4) 점수맵 생성
        Map<Long, ScoreDetail> scoreMap = new HashMap<>();
        for (Long id : ids) {
            int ov = overlapMap.getOrDefault(id, 0);

            double distRaw = distanceKmOrMax(bLat, bLng, careWorkerLatLngMap.get(id));
            boolean geoMissing = (distRaw == Double.MAX_VALUE);

            Double distKm = geoMissing ? null : distRaw;

            int tagScore = ov * 3;

            Integer distScoreView = geoMissing ? null : distanceScore(distRaw); // 표시용
            int distScoreForSum = geoMissing ? 0 : distScoreView;              // 합산용

            int totalView = tagScore + distScoreForSum;

            ScoreDetail sd = new ScoreDetail();
            sd.overlapCount = ov;
            sd.tagScore = tagScore;
            sd.distanceKm = distKm;
            sd.distanceScore = distScoreView;

            sd.totalScoreView = totalView;


            sd.totalScoreSort = geoMissing ? null : totalView;

            scoreMap.put(id, sd);
        }

        // 여기서는 ids 순서를 그대로 반환(정렬은 mode에서 수행)
        return new ScoreResult(new ArrayList<>(ids), scoreMap);
    }

    private void applyScores(List<CareWorkerCardDto> cards, Map<Long, ScoreDetail> scoreMap) {
        if (cards == null || cards.isEmpty()) return;
        if (scoreMap == null || scoreMap.isEmpty()) return;

        for (CareWorkerCardDto card : cards) {
            if (card == null || card.getCareWorkerId() == null) continue;

            ScoreDetail sd = scoreMap.get(card.getCareWorkerId());
            if (sd == null) continue;

            card.setOverlapCount(sd.overlapCount);
            card.setTagScore(sd.tagScore);
            card.setDistanceKm(sd.distanceKm);
            card.setDistanceScore(sd.distanceScore);

            card.setTotalScore(sd.totalScoreView);
        }
    }

    /**
     * 거리점수:
     * - 기본 10점
     * - 1km 멀어질 때마다 -1점  (floor(distanceKm / 2))
     * - 좌표 없어서 distance가 MAX면 0점 처리(정렬에서 뒤로 가도록)
     */
    private int distanceScore(double distanceKm) {
        if (distanceKm == Double.MAX_VALUE) return 0;

        int score = 10 - (int) Math.floor(distanceKm); // 1km당 -1
        return Math.max(score, 0);
    }

    // =========================
    // GEO (CSV 기반 자동 채움)
    // =========================
    private void ensureBeneficiaryGeo(Long beneficiaryId) {
        try {
            var geo = mapper.selectBeneficiaryGeoForUpdate(beneficiaryId);
            if (geo == null) return;

            if (geo.getLat() != null && geo.getLng() != null) {
                if (geo.getGeoReady() == null || geo.getGeoReady() == 0) {
                    mapper.updateBeneficiaryGeoReadyOnly(beneficiaryId);
                }
                return;
            }

            var parsed = AddressDongParser.parse(geo.getAddress());
            if (parsed.isEmpty()) {
                log.warn("[GEO-FAIL] beneficiaryId={} reason=parse-fail address={}", beneficiaryId, geo.getAddress());
                return;
            }

            var p = parsed.get();
            var found = dongCoordinateLoader.find(p.sido(), p.gu(), p.dong());
            if (found == null) {
                log.warn("[GEO-FAIL] beneficiaryId={} reason=csv-not-found sido={} gu={} dong={} address={}",
                        beneficiaryId, p.sido(), p.gu(), p.dong(), geo.getAddress());
                return;
            }

            int updated = mapper.updateBeneficiaryGeo(beneficiaryId, found.getLat(), found.getLng());
            log.info("[GEO-UPDATE] beneficiaryId={} updated={} lat={} lng={}",
                    beneficiaryId, updated, found.getLat(), found.getLng());

        } catch (Exception e) {
            log.warn("[GEO-ERR] beneficiaryId={} err={}", beneficiaryId, e.toString());
        }
    }

    private void ensureCareWorkersGeo(List<Long> careWorkerIds) {
        try {
            var list = mapper.selectCareWorkerGeoForUpdateByIds(careWorkerIds);
            if (list == null || list.isEmpty()) return;

            for (CareWorkerGeoDto cw : list) {
                if (cw == null || cw.getCareWorkerId() == null) continue;

                if (cw.getLat() != null && cw.getLng() != null) {
                    if (cw.getGeoReady() == null || cw.getGeoReady() == 0) {
                        mapper.updateCareWorkerEmployeeGeoReadyOnly(cw.getCareWorkerId());
                    }
                    continue;
                }

                var parsed = AddressDongParser.parse(cw.getAddress());
                if (parsed.isEmpty()) {
                    log.warn("[GEO-FAIL] careWorkerId={} reason=parse-fail address={}",
                            cw.getCareWorkerId(), cw.getAddress());
                    continue;
                }

                var p = parsed.get();
                var found = dongCoordinateLoader.find(p.sido(), p.gu(), p.dong());
                if (found == null) {
                    log.warn("[GEO-FAIL] careWorkerId={} reason=csv-not-found sido={} gu={} dong={} address={}",
                            cw.getCareWorkerId(), p.sido(), p.gu(), p.dong(), cw.getAddress());
                    continue;
                }

                int updated = mapper.updateCareWorkerEmployeeGeo(cw.getCareWorkerId(), found.getLat(), found.getLng());
                log.info("[GEO-UPDATE] careWorkerId={} updated={} lat={} lng={}",
                        cw.getCareWorkerId(), updated, found.getLat(), found.getLng());
            }
        } catch (Exception e) {
            log.warn("[GEO-ERR] careWorkers err={}", e.toString());
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

    // =========================
    // 내부 점수 객체들
    // =========================
    private static class ScoreDetail {
        Integer overlapCount;
        Integer tagScore;

        Double distanceKm;          // null 가능
        Integer distanceScore;      // 표시용(좌표 없으면 null)
        Integer totalScoreView;     // 표시용(좌표 없어도 tagScore는 반영)

        Integer totalScoreSort;     // 정렬용(좌표 없으면 null -> TOTAL 정렬에서 뒤로)
    }

    private static class ScoreResult {
        private final List<Long> sortedIds;
        private final Map<Long, ScoreDetail> scoreMap;

        private ScoreResult(List<Long> sortedIds, Map<Long, ScoreDetail> scoreMap) {
            this.sortedIds = sortedIds;
            this.scoreMap = scoreMap;
        }

        public List<Long> getSortedIds() {
            return sortedIds;
        }

        public Map<Long, ScoreDetail> getScoreMap() {
            return scoreMap;
        }
    }
}