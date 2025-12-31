package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.repository.MatchingRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.BeneficiaryScheduleRepository;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingAssignService {

    private final MatchingRepository matchingRepository;
    private final VisitScheduleRepository visitScheduleRepository;
    private final BeneficiaryScheduleRepository beneficiaryScheduleRepository;

    public void assign(Long beneficiaryId, Long careWorkerId, LocalDate effectiveDate) {
        if (beneficiaryId == null || careWorkerId == null || effectiveDate == null) {
            throw new IllegalArgumentException("beneficiaryId/careWorkerId/effectiveDate는 필수입니다.");
        }
        if (effectiveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("적용일은 오늘 이후(오늘 포함)만 가능합니다.");
        }

        upsertMatching(beneficiaryId, careWorkerId, effectiveDate);

        LocalDateTime from = effectiveDate.atStartOfDay();
        LocalDateTime toExclusive = resolveGlobalRangeEndExclusiveToMonthEnd();
        if (toExclusive == null || !from.isBefore(toExclusive)) return;

        createVisitSchedulesFromBeneficiarySchedules(beneficiaryId, careWorkerId, from, toExclusive);

        visitScheduleRepository.bulkUpdateCareWorkerId(
                beneficiaryId,
                from,
                toExclusive,
                VisitSchedule.VisitStatus.SCHEDULED,
                careWorkerId
        );
    }

    public void unassign(Long beneficiaryId, LocalDate effectiveDate) {
        if (beneficiaryId == null || effectiveDate == null) {
            throw new IllegalArgumentException("beneficiaryId/effectiveDate는 필수입니다.");
        }
        if (effectiveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("적용일은 오늘 이후(오늘 포함)만 가능합니다.");
        }

        matchingRepository.findByBeneficiaryIdAndStatus(beneficiaryId, "Y")
                .ifPresent(existing -> {
                    existing.setStatus("N");
                    existing.setEndDate(effectiveDate.minusDays(1));
                    existing.setReason("매칭 해제");
                    matchingRepository.save(existing);
                });

        LocalDateTime from = effectiveDate.atStartOfDay();
        LocalDateTime toExclusive = resolveGlobalRangeEndExclusiveToMonthEnd();
        if (toExclusive == null || !from.isBefore(toExclusive)) return;

        visitScheduleRepository.bulkDeleteByBeneficiaryAndRangeAndStatus(
                beneficiaryId,
                from,
                toExclusive,
                VisitSchedule.VisitStatus.SCHEDULED
        );
    }

    private void createVisitSchedulesFromBeneficiarySchedules(
            Long beneficiaryId,
            Long careWorkerId,
            LocalDateTime from,
            LocalDateTime toExclusive
    ) {
        var templates = beneficiaryScheduleRepository.findByBeneficiaryId(beneficiaryId);
        if (templates == null || templates.isEmpty()) return;

        LocalDate startDate = from.toLocalDate();
        LocalDate endDateExclusive = toExclusive.toLocalDate();

        List<VisitSchedule> toSave = new ArrayList<>();

        for (var bs : templates) {
            Integer day = bs.getDay();
            LocalTime st = bs.getStartTime();
            LocalTime et = bs.getEndTime();

            Integer serviceTypeIdInt = bs.getServiceTypeId();   // exists 체크용
            Long serviceTypeIdLong = (serviceTypeIdInt != null) ? serviceTypeIdInt.longValue() : null; // 저장용

            if (day == null || st == null || et == null || serviceTypeIdInt == null || serviceTypeIdLong == null) continue;

            DayOfWeek targetDow = mapDayToDayOfWeek(day);

            LocalDate first = startDate;
            while (first.getDayOfWeek() != targetDow) first = first.plusDays(1);

            for (LocalDate d = first; d.isBefore(endDateExclusive); d = d.plusWeeks(1)) {
                LocalDateTime sdt = LocalDateTime.of(d, st);
                LocalDateTime edt = LocalDateTime.of(d, et);

                if (sdt.isBefore(from)) continue;
                if (!sdt.isBefore(toExclusive)) break;

                boolean exists = visitScheduleRepository
                        .existsByBeneficiaryIdAndServiceTypeIdAndStartDtAndEndDtAndVisitStatus(
                                beneficiaryId,
                                serviceTypeIdInt,
                                sdt,
                                edt,
                                VisitSchedule.VisitStatus.SCHEDULED
                        );
                if (exists) continue;

                VisitSchedule v = new VisitSchedule();
                v.setBeneficiaryId(beneficiaryId);
                v.setCareWorkerId(careWorkerId);
                v.setServiceTypeId(serviceTypeIdLong);
                v.setStartDt(sdt);
                v.setEndDt(edt);
                v.setVisitStatus(VisitSchedule.VisitStatus.SCHEDULED);
                v.setRfidStartTime(null);
                v.setRfidEndTime(null);
                v.setIsLogWritten(false);
                v.setNote(null);

                toSave.add(v);
            }
        }

        if (!toSave.isEmpty()) {
            visitScheduleRepository.saveAll(toSave);
        }
    }

    private DayOfWeek mapDayToDayOfWeek(int day) {
        return switch (day) {
            case 1 -> DayOfWeek.MONDAY;
            case 2 -> DayOfWeek.TUESDAY;
            case 3 -> DayOfWeek.WEDNESDAY;
            case 4 -> DayOfWeek.THURSDAY;
            case 5 -> DayOfWeek.FRIDAY;
            case 6 -> DayOfWeek.SATURDAY;
            case 7 -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("day는 1~7 이어야 합니다: " + day);
        };
    }

    private void upsertMatching(Long beneficiaryId, Long careWorkerId, LocalDate eff) {
        Optional<Matching> opt = matchingRepository.findByBeneficiaryIdAndStatus(beneficiaryId, "Y");

        opt.ifPresentOrElse(existing -> {
            if (existing.getCareWorkerId() != null && existing.getCareWorkerId().equals(careWorkerId)) {
                return;
            }

            existing.setStatus("N");
            existing.setEndDate(eff.minusDays(1));
            existing.setReason("요양보호사 변경");
            matchingRepository.save(existing);

            Matching next = new Matching();
            next.setBeneficiaryId(beneficiaryId);
            next.setCareWorkerId(careWorkerId);
            next.setStartDate(eff);
            next.setEndDate(null);
            next.setStatus("Y");
            next.setReason(null);
            matchingRepository.save(next);

        }, () -> {
            Matching next = new Matching();
            next.setBeneficiaryId(beneficiaryId);
            next.setCareWorkerId(careWorkerId);
            next.setStartDate(eff);
            next.setEndDate(null);
            next.setStatus("Y");
            next.setReason(null);
            matchingRepository.save(next);
        });
    }

    private LocalDateTime resolveGlobalRangeEndExclusiveToMonthEnd() {
        LocalDateTime globalMaxEndDt = visitScheduleRepository.findGlobalMaxEndDt();
        if (globalMaxEndDt == null) return null;

        YearMonth ym = YearMonth.from(globalMaxEndDt.toLocalDate());
        return ym.plusMonths(1).atDay(1).atStartOfDay();
    }
}