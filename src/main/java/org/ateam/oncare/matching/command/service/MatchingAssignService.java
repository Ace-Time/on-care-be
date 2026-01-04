package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.matching.command.repository.MatchingRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.BeneficiaryScheduleRepository;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingAssignService {

    private final MatchingRepository matchingRepository;
    private final VisitScheduleRepository visitScheduleRepository;
    private final BeneficiaryScheduleRepository beneficiaryScheduleRepository;

    private final NotificationCommandService notificationCommandService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final CareWorkerRepository careWorkerRepository;

    private static final String TEMPLATE_MATCHING_ASSIGNED = "MATCHING_ASSIGNED";
    private static final String TEMPLATE_MATCHING_UNASSIGNED = "MATCHING_CANCELED";

    public void assign(Long beneficiaryId, Long careWorkerId, LocalDate effectiveDate) {
        if (beneficiaryId == null || careWorkerId == null || effectiveDate == null) {
            throw new IllegalArgumentException("beneficiaryId/careWorkerId/effectiveDate는 필수입니다.");
        }
        if (effectiveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("적용일은 오늘 이후(오늘 포함)만 가능합니다.");
        }

        Optional<Matching> before = matchingRepository.findByBeneficiaryIdAndStatus(beneficiaryId, "Y");

        before.ifPresent(existing -> {
            if (!existing.getCareWorkerId().equals(careWorkerId)) {
                notifyMatching(existing.getCareWorkerId(), beneficiaryId, TEMPLATE_MATCHING_UNASSIGNED);
            }
        });

        upsertMatching(beneficiaryId, careWorkerId, effectiveDate);

        notifyMatching(careWorkerId, beneficiaryId, TEMPLATE_MATCHING_ASSIGNED);

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

                    notifyMatching(existing.getCareWorkerId(), beneficiaryId, TEMPLATE_MATCHING_UNASSIGNED);
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

    private void notifyMatching(Long careWorkerId, Long beneficiaryId, String templateType) {
        Long receiverEmployeeId = resolveEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = notificationTemplateRepository
                .findByTemplateTypeAndIsActive(templateType, 1)
                .stream()
                .findFirst()
                .orElseThrow();

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);

        String title = apply(template.getTitle(), vars);
        String content = apply(template.getContent(), vars);

        notificationCommandService.sendCustom(
                receiverEmployeeId,
                title,
                content,
                template.getTemplateType(),
                template.getSeverity()
        );
    }

    private Long resolveEmployeeId(Long careWorkerId) {
        CareWorker cw = careWorkerRepository.findById(careWorkerId).orElseThrow();
        return Long.valueOf(cw.getEmployeeId());
    }

    private String resolveBeneficiaryName(Long beneficiaryId) {
        Beneficiary b = beneficiaryRepository.findById(beneficiaryId).orElseThrow();
        return b.getName();
    }

    private String apply(String template, Map<String, String> vars) {
        String result = template;
        for (var e : vars.entrySet()) {
            result = result.replace("{" + e.getKey() + "}", e.getValue());
        }
        return result;
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
            Integer serviceTypeIdInt = bs.getServiceTypeId();
            Long serviceTypeIdLong = serviceTypeIdInt != null ? serviceTypeIdInt.longValue() : null;
            if (day == null || st == null || et == null || serviceTypeIdLong == null) continue;

            DayOfWeek targetDow = mapDayToDayOfWeek(day);
            LocalDate first = startDate;
            while (first.getDayOfWeek() != targetDow) first = first.plusDays(1);

            for (LocalDate d = first; d.isBefore(endDateExclusive); d = d.plusWeeks(1)) {
                LocalDateTime sdt = LocalDateTime.of(d, st);
                LocalDateTime edt = LocalDateTime.of(d, et);
                if (sdt.isBefore(from) || !sdt.isBefore(toExclusive)) continue;

                boolean exists = visitScheduleRepository
                        .existsByBeneficiaryIdAndServiceTypeIdAndStartDtAndEndDtAndVisitStatus(
                                beneficiaryId, serviceTypeIdInt, sdt, edt, VisitSchedule.VisitStatus.SCHEDULED
                        );
                if (exists) continue;

                VisitSchedule v = new VisitSchedule();
                v.setBeneficiaryId(beneficiaryId);
                v.setCareWorkerId(careWorkerId);
                v.setServiceTypeId(serviceTypeIdLong);
                v.setStartDt(sdt);
                v.setEndDt(edt);
                v.setVisitStatus(VisitSchedule.VisitStatus.SCHEDULED);
                v.setIsLogWritten(false);

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
            default -> throw new IllegalArgumentException();
        };
    }

    private void upsertMatching(Long beneficiaryId, Long careWorkerId, LocalDate eff) {
        Optional<Matching> opt = matchingRepository.findByBeneficiaryIdAndStatus(beneficiaryId, "Y");

        opt.ifPresentOrElse(existing -> {
            if (existing.getCareWorkerId().equals(careWorkerId)) return;

            existing.setStatus("N");
            existing.setEndDate(eff.minusDays(1));
            existing.setReason("요양보호사 변경");
            matchingRepository.save(existing);

            Matching next = new Matching();
            next.setBeneficiaryId(beneficiaryId);
            next.setCareWorkerId(careWorkerId);
            next.setStartDate(eff);
            next.setStatus("Y");
            matchingRepository.save(next);
        }, () -> {
            Matching next = new Matching();
            next.setBeneficiaryId(beneficiaryId);
            next.setCareWorkerId(careWorkerId);
            next.setStartDate(eff);
            next.setStatus("Y");
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