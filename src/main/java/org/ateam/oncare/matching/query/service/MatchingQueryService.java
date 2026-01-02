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

    public List<BeneficiarySummaryDto> getBeneficiariesSummary(int page, int size, String keyword) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = safePage * safeSize;

        String q = (keyword == null) ? null : keyword.trim();
        if (q != null && q.isEmpty()) q = null;

        return mapper.selectBeneficiariesSummary(offset, safeSize, q);
    }

    public BeneficiaryPageResponse getBeneficiariesPage(int page, int size, String keyword) {
        int offset = Math.max(page, 0) * size;
        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        List<BeneficiarySummaryDto> list =
                mapper.selectBeneficiariesSummary(offset, size, q);

        long total = mapper.countBeneficiaries(q);

        return new BeneficiaryPageResponse(list, page, size, total);
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

    /**
     * ✅ (케이스1) 수급자 기준 추천: 필터링 -> 정렬 -> 카드
     */
    public List<CareWorkerCardDto> getCandidateCareWorkers(Long beneficiaryId) {
        var ids = selectFinalCandidateCareWorkerIds(beneficiaryId);
        ids = sortByTagThenDistance(beneficiaryId, ids);
        return getCareWorkerCardsByIds(ids);
    }

    public CareWorkerDetailDto getCareWorkerDetail(Long careWorkerId) {
        var detail = mapper.selectCareWorkerDetail(careWorkerId);
        if (detail == null) {
            log.warn("[CAREWORKER DETAIL] careWorkerId={} NOT FOUND", careWorkerId);
            return null;
        }
        log.info("[CAREWORKER DETAIL] careWorkerId={} name={}", careWorkerId, detail.getName());
        return detail;
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

    /**
     * ✅ (케이스3) 생성 기준 추천: 필터링 -> 정렬 -> 카드
     */
    public List<CareWorkerCardDto> getCreateVisitAvailableCareWorkers(
            Long beneficiaryId, Long serviceTypeId, String startDt, String endDt
    ) {
        var ids = selectFinalCandidateCareWorkerIdsForCreateVisit(beneficiaryId, serviceTypeId, startDt, endDt);
        ids = sortByTagThenDistance(beneficiaryId, ids);
        return getCareWorkerCardsByIds(ids);
    }

    /**
     * ✅ (케이스2) 방문일정 기준 추천: 필터링 -> 정렬 -> 카드
     */
    public List<CareWorkerCardDto> getVisitTimeAvailableCareWorkers(
            Long vsId, String startDt, String endDt
    ) {
        Long beneficiaryId = mapper.selectVisitScheduleBeneficiaryId(vsId);
        if (beneficiaryId == null) return List.of();

        var ids = selectFinalCandidateCareWorkerIdsByVisitSchedule(vsId, startDt, endDt);
        ids = sortByTagThenDistance(beneficiaryId, ids);
        return getCareWorkerCardsByIds(ids);
    }

    /**
     * ✅ 핵심 정렬:
     * 1) 태그 겹침 수 DESC
     * 2) (동률) 거리 ASC
     * 3) (완전동률) id ASC
     */
    private List<Long> sortByTagThenDistance(Long beneficiaryId, List<Long> ids) {
        if (beneficiaryId == null) return ids == null ? List.of() : ids;
        if (ids == null || ids.isEmpty()) return List.of();

        // ✅ 정렬 전에: lat/lng 없으면 CSV로 채워서 UPDATE
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

        log.info("[GEO] careWorkerLatLngMap.size={} / ids.size={}",
                careWorkerLatLngMap.size(), ids.size());

        // 4) 정렬
        List<Long> sorted = new ArrayList<>(ids);
        sorted.sort((a, c) -> {
            int aOverlap = overlapMap.getOrDefault(a, 0);
            int cOverlap = overlapMap.getOrDefault(c, 0);

            // 태그 겹침수 DESC
            int cmp1 = Integer.compare(cOverlap, aOverlap);
            if (cmp1 != 0) return cmp1;

            // 거리 ASC
            double aDist = distanceKmOrMax(bLat, bLng, careWorkerLatLngMap.get(a));
            double cDist = distanceKmOrMax(bLat, bLng, careWorkerLatLngMap.get(c));

            int cmp2 = Double.compare(aDist, cDist);
            if (cmp2 != 0) return cmp2;

            // 안정성
            return Long.compare(a, c);
        });

        // ✅ 검증용: 상위 10명만 overlap/거리 같이 출력
        for (int i = 0; i < Math.min(10, sorted.size()); i++) {
            Long id = sorted.get(i);
            int ov = overlapMap.getOrDefault(id, 0);
            double dist = distanceKmOrMax(bLat, bLng, careWorkerLatLngMap.get(id));
            log.info("[SORT-CHK] rank={} careWorkerId={} overlap={} distKm={}", i + 1, id, ov,
                    dist == Double.MAX_VALUE ? null : dist);
        }

        return sorted;
    }

    /**
     * ✅ beneficiary: lat/lng NULL이면 주소 기반(동/구)으로 CSV에서 찾아 UPDATE
     *    lat/lng 있으면 geo_ready만 1로 맞춰줌(선택)
     */
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

    /**
     * ✅ careworker(employee): lat/lng NULL이면 주소 기반(동/구)으로 CSV에서 찾아 UPDATE
     *    lat/lng 있으면 geo_ready만 1로 맞춰줌(선택)
     */
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
    public CareWorkerPageResponse getCandidateCareWorkersPage(
            Long beneficiaryId, int page, int size, String keyword
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        List<CareWorkerCardDto> all = getCandidateCareWorkers(beneficiaryId);

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
            Long vsId, String startDt, String endDt, int page
    ) {
        int safePage = Math.max(page, 0);
        int size = 3;

        List<CareWorkerCardDto> all = getVisitTimeAvailableCareWorkers(vsId, startDt, endDt);
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
            int page, int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        // ✅ 기존 로직 그대로 호출 (필터링/정렬 그대로)
        List<CareWorkerCardDto> all =
                getCreateVisitAvailableCareWorkers(beneficiaryId, serviceTypeId, startDt, endDt);

        if (all == null || all.isEmpty()) {
            return new CareWorkerPageResponse(List.of(), safePage, safeSize, 0);
        }

        long total = all.size();

        int from = safePage * safeSize;
        int to = Math.min(from + safeSize, all.size());

        List<CareWorkerCardDto> pageList =
                (from >= all.size()) ? List.of() : all.subList(from, to);

        return new CareWorkerPageResponse(pageList, safePage, safeSize, total);
    }
}