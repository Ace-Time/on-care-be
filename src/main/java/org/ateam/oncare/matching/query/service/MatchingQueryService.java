package org.ateam.oncare.matching.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.matching.query.dto.*;
import org.ateam.oncare.matching.query.mapper.MatchingQueryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingQueryService {

    private final MatchingQueryMapper mapper;

    /**
     * 최종 후보 요양보호사 ID 조회
     * 1) 시간 충돌 없는 요양보호사
     * 2) 수급자 요구 서비스 타입 "전부" 만족
     * 3) 위험요소 대응 자격증(승인) "전부" 만족
     */
    public List<Long> selectFinalCandidateCareWorkerIds(Long beneficiaryId) {

        var schedules = mapper.selectBeneficiarySchedules(beneficiaryId);
        log.info("[SCHEDULE] beneficiaryId={} count={}", beneficiaryId, schedules.size());

        if (schedules == null || schedules.isEmpty()) {
            log.warn("[STOP] No schedules for beneficiaryId={}", beneficiaryId);
            return List.of();
        }

        // (1) 시간 충돌 없는 요양보호사 교집합
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

        // (2) 서비스 타입 전부 만족
        Set<Long> serviceTypeSet = mapper.selectCareWorkerIdsByServiceType(beneficiaryId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.info("[SERVICE TYPE SET] count={}", serviceTypeSet.size());
        timeIntersect.retainAll(serviceTypeSet);
        if (timeIntersect.isEmpty()) return List.of();

        // (3) 위험요소 → 자격증 전부 만족
        Set<Long> riskCertSet = mapper.selectCareWorkerIdsByRiskCertificates(beneficiaryId).stream()
                .map(CareWorkerIdDto::getCareWorkerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.info("[RISK->CERT SET] count={}", riskCertSet.size());
        timeIntersect.retainAll(riskCertSet);

        var result = new ArrayList<>(timeIntersect);
        log.info("[FINAL] beneficiaryId={} finalCount={}", beneficiaryId, result.size());
        return result;
    }

    public List<BeneficiarySummaryDto> getBeneficiariesSummary() {
        var list = mapper.selectBeneficiariesSummary();
        log.info("[BENEFICIARY SUMMARY] count={}", list.size());
        return list;
    }

    public BeneficiaryDetailDto getBeneficiaryDetail(Long beneficiaryId) {
        var detail = mapper.selectBeneficiaryDetail(beneficiaryId);
        if (detail == null) {
            log.warn("[BENEFICIARY DETAIL] beneficiaryId={} NOT FOUND", beneficiaryId);
            return null;
        }
        log.info("[BENEFICIARY DETAIL] beneficiaryId={} name={}", beneficiaryId, detail.getName());
        return detail;
    }

    /**
     * 후보 요양보호사 카드 조회 (이름/성별/태그)
     * tags는 XML(resultMap+collection)에서 한 번에 채워지게 구성하는 것을 전제로 함
     */
    public List<CareWorkerCardDto> getCareWorkerCardsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();

        var list = mapper.selectCareWorkerCardsByIds(ids); // tags까지 포함해서 내려오게
        log.info("[CAREWORKER CARDS] requestedIds={} returned={}", ids.size(), list.size());
        return list;
    }

    /**
     * beneficiaryId → 후보 IDs 산출 → 카드 조회
     */
    public List<CareWorkerCardDto> getCandidateCareWorkers(Long beneficiaryId) {
        var ids = selectFinalCandidateCareWorkerIds(beneficiaryId);
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
}