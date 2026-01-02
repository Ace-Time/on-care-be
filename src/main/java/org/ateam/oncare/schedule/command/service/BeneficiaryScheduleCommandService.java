package org.ateam.oncare.schedule.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.entity.BeneficiarySchedule;
import org.ateam.oncare.matching.command.repository.MatchingRepository;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleCreateRequest;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleResponse;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleUpdateRequest;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.BeneficiaryScheduleRepository;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BeneficiaryScheduleCommandService {

    private final BeneficiaryScheduleRepository beneficiaryScheduleRepository;
    private final MatchingRepository matchingRepository;
    private final VisitScheduleRepository visitScheduleRepository;

    public BeneficiaryScheduleResponse create(BeneficiaryScheduleCreateRequest req) {
        validateBasic(req.getDay(), req.getStartTime(), req.getEndTime());

        boolean duplicated = beneficiaryScheduleRepository
                .existsByBeneficiaryIdAndServiceTypeIdAndDayAndStartTimeAndEndTime(
                        req.getBeneficiaryId(),
                        req.getServiceTypeId(),
                        req.getDay(),
                        req.getStartTime(),
                        req.getEndTime()
                );

        if (duplicated) {
            throw new IllegalStateException("이미 동일한 희망 시간이 등록되어 있습니다.");
        }

        Matching active = matchingRepository
                .findByBeneficiaryIdAndStatus(req.getBeneficiaryId(), "Y")
                .orElse(null);

        if (active != null) {
            Long careWorkerId = active.getCareWorkerId();

            boolean overlap = beneficiaryScheduleRepository.existsOverlapWithOtherBeneficiaries(
                    careWorkerId,
                    req.getBeneficiaryId(),
                    req.getDay(),
                    req.getStartTime(),
                    req.getEndTime(),
                    null
            );

            if (overlap) {
                throw new IllegalStateException("추가/수정된 시간대에 담당 요양보호사의 일정이 존재합니다.");
            }
        }

        BeneficiarySchedule bs = new BeneficiarySchedule();
        bs.setBeneficiaryId(req.getBeneficiaryId());
        bs.setServiceTypeId(req.getServiceTypeId());
        bs.setDay(req.getDay());
        bs.setStartTime(req.getStartTime());
        bs.setEndTime(req.getEndTime());

        BeneficiarySchedule saved = beneficiaryScheduleRepository.save(bs);

        if (active != null) {
            LocalDate effectiveDate = normalizeEffectiveDate(req.getEffectiveDate());
            syncVisitSchedulesOnCreate(saved, active.getCareWorkerId(), effectiveDate);
        }

        return toResponse(saved);
    }

    public BeneficiaryScheduleResponse update(Long id, BeneficiaryScheduleUpdateRequest req) {
        validateBasic(req.getDay(), req.getStartTime(), req.getEndTime());

        BeneficiarySchedule bs = beneficiaryScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("희망 주기를 찾을 수 없습니다. id=" + id));

        Integer oldServiceTypeId = bs.getServiceTypeId();
        Integer oldDay = bs.getDay();
        LocalTime oldStart = bs.getStartTime();
        LocalTime oldEnd = bs.getEndTime();

        boolean duplicated = beneficiaryScheduleRepository
                .existsByBeneficiaryIdAndServiceTypeIdAndDayAndStartTimeAndEndTimeAndIdNot(
                        bs.getBeneficiaryId(),
                        req.getServiceTypeId(),
                        req.getDay(),
                        req.getStartTime(),
                        req.getEndTime(),
                        id
                );

        if (duplicated) {
            throw new IllegalStateException("이미 동일한 희망 시간이 등록되어 있습니다.");
        }

        Matching active = matchingRepository
                .findByBeneficiaryIdAndStatus(bs.getBeneficiaryId(), "Y")
                .orElse(null);

        if (active != null) {
            Long careWorkerId = active.getCareWorkerId();

            boolean overlap = beneficiaryScheduleRepository.existsOverlapWithOtherBeneficiaries(
                    careWorkerId,
                    bs.getBeneficiaryId(),
                    req.getDay(),
                    req.getStartTime(),
                    req.getEndTime(),
                    id
            );

            if (overlap) {
                throw new IllegalStateException("추가/수정된 시간대에 담당 요양보호사의 일정이 존재합니다.");
            }
        }

        bs.setServiceTypeId(req.getServiceTypeId());
        bs.setDay(req.getDay());
        bs.setStartTime(req.getStartTime());
        bs.setEndTime(req.getEndTime());

        if (active != null) {
            LocalDate effectiveDate = normalizeEffectiveDate(req.getEffectiveDate());
            syncVisitSchedulesOnUpdate(
                    bs.getBeneficiaryId(),
                    active.getCareWorkerId(),
                    oldServiceTypeId, oldDay, oldStart, oldEnd,
                    bs.getServiceTypeId(), bs.getDay(), bs.getStartTime(), bs.getEndTime(),
                    effectiveDate
            );
        }

        return toResponse(bs);
    }

    public void delete(Long id, LocalDate effectiveDate) {
        BeneficiarySchedule bs = beneficiaryScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("희망 주기를 찾을 수 없습니다. id=" + id));

        long cnt = beneficiaryScheduleRepository.countByBeneficiaryId(bs.getBeneficiaryId());
        if (cnt <= 1) {
            throw new IllegalStateException("희망 시간은 최소 1개 이상 유지되어야 합니다.");
        }

        Matching active = matchingRepository
                .findByBeneficiaryIdAndStatus(bs.getBeneficiaryId(), "Y")
                .orElse(null);

        if (active != null) {
            LocalDate eff = normalizeEffectiveDate(effectiveDate);
            syncVisitSchedulesOnDelete(
                    bs.getBeneficiaryId(),
                    bs.getServiceTypeId(),
                    bs.getDay(),
                    bs.getStartTime(),
                    bs.getEndTime(),
                    eff
            );
        }

        beneficiaryScheduleRepository.deleteById(id);
    }

    private LocalDate normalizeEffectiveDate(LocalDate effectiveDate) {
        LocalDate today = LocalDate.now();
        LocalDate eff = (effectiveDate == null) ? today : effectiveDate;
        if (eff.isBefore(today)) {
            throw new IllegalArgumentException("적용일은 오늘 이후(오늘 포함)만 가능합니다.");
        }
        return eff;
    }

    private void syncVisitSchedulesOnCreate(BeneficiarySchedule created, Long careWorkerId, LocalDate effectiveDate) {
        Long beneficiaryId = created.getBeneficiaryId();
        LocalDateTime from = effectiveDate.atStartOfDay();
        LocalDateTime toExclusive = resolveRangeEndExclusiveOrMonthEnd(from);
        if (toExclusive == null || !from.isBefore(toExclusive)) return;

        createVisitSchedulesForOneTemplate(created, careWorkerId, from, toExclusive);

        visitScheduleRepository.bulkUpdateCareWorkerId(
                beneficiaryId,
                from,
                toExclusive,
                VisitSchedule.VisitStatus.SCHEDULED,
                careWorkerId
        );
    }

    private void syncVisitSchedulesOnUpdate(
            Long beneficiaryId,
            Long careWorkerId,
            Integer oldServiceTypeId,
            Integer oldDay,
            LocalTime oldStart,
            LocalTime oldEnd,
            Integer newServiceTypeId,
            Integer newDay,
            LocalTime newStart,
            LocalTime newEnd,
            LocalDate effectiveDate
    ) {
        LocalDateTime from = effectiveDate.atStartOfDay();
        LocalDateTime toExclusive = resolveRangeEndExclusiveOrMonthEnd(from);
        if (toExclusive == null || !from.isBefore(toExclusive)) return;

        if (oldServiceTypeId != null && oldDay != null && oldStart != null && oldEnd != null) {
            deletePatternScheduledInRange(
                    beneficiaryId,
                    oldServiceTypeId.longValue(),
                    oldDay,
                    oldStart,
                    oldEnd,
                    from,
                    toExclusive
            );
        }

        BeneficiarySchedule temp = new BeneficiarySchedule();
        temp.setBeneficiaryId(beneficiaryId);
        temp.setServiceTypeId(newServiceTypeId);
        temp.setDay(newDay);
        temp.setStartTime(newStart);
        temp.setEndTime(newEnd);

        createVisitSchedulesForOneTemplate(temp, careWorkerId, from, toExclusive);

        visitScheduleRepository.bulkUpdateCareWorkerId(
                beneficiaryId,
                from,
                toExclusive,
                VisitSchedule.VisitStatus.SCHEDULED,
                careWorkerId
        );
    }

    private void syncVisitSchedulesOnDelete(
            Long beneficiaryId,
            Integer serviceTypeId,
            Integer day,
            LocalTime start,
            LocalTime end,
            LocalDate effectiveDate
    ) {
        LocalDateTime from = effectiveDate.atStartOfDay();
        LocalDateTime toExclusive = resolveRangeEndExclusiveOrMonthEnd(from);
        if (toExclusive == null || !from.isBefore(toExclusive)) return;

        if (serviceTypeId == null || day == null || start == null || end == null) return;

        deletePatternScheduledInRange(
                beneficiaryId,
                serviceTypeId.longValue(),
                day,
                start,
                end,
                from,
                toExclusive
        );
    }

    private void createVisitSchedulesForOneTemplate(
            BeneficiarySchedule bs,
            Long careWorkerId,
            LocalDateTime from,
            LocalDateTime toExclusive
    ) {
        if (bs.getBeneficiaryId() == null || bs.getServiceTypeId() == null || bs.getDay() == null
                || bs.getStartTime() == null || bs.getEndTime() == null) return;

        Long beneficiaryId = bs.getBeneficiaryId();
        Long serviceTypeId = bs.getServiceTypeId().longValue();
        int day = bs.getDay();
        LocalTime st = bs.getStartTime();
        LocalTime et = bs.getEndTime();

        DayOfWeek targetDow = mapDayToDayOfWeek(day);

        LocalDate startDate = from.toLocalDate();
        LocalDate endDateExclusive = toExclusive.toLocalDate();

        LocalDate first = startDate;
        while (first.getDayOfWeek() != targetDow) first = first.plusDays(1);

        List<VisitSchedule> toSave = new ArrayList<>();

        for (LocalDate d = first; d.isBefore(endDateExclusive); d = d.plusWeeks(1)) {
            LocalDateTime sdt = LocalDateTime.of(d, st);
            LocalDateTime edt = LocalDateTime.of(d, et);

            if (sdt.isBefore(from)) continue;
            if (!sdt.isBefore(toExclusive)) break;

            if (visitScheduleRepository.existsScheduledExact(beneficiaryId, serviceTypeId, sdt, edt)) {
                continue;
            }

            VisitSchedule v = new VisitSchedule();
            v.setBeneficiaryId(beneficiaryId);
            v.setCareWorkerId(careWorkerId);
            v.setServiceTypeId(serviceTypeId);
            v.setStartDt(sdt);
            v.setEndDt(edt);
            v.setVisitStatus(VisitSchedule.VisitStatus.SCHEDULED);
            v.setRfidStartTime(null);
            v.setRfidEndTime(null);
            v.setIsLogWritten(false);
            v.setNote(null);

            toSave.add(v);
        }

        if (!toSave.isEmpty()) {
            visitScheduleRepository.saveAll(toSave);
        }
    }

    private void deletePatternScheduledInRange(
            Long beneficiaryId,
            Long serviceTypeId,
            Integer day,
            LocalTime start,
            LocalTime end,
            LocalDateTime from,
            LocalDateTime toExclusive
    ) {
        int weekday0 = (day - 1);
        visitScheduleRepository.deleteScheduledByPatternInRange(
                beneficiaryId,
                serviceTypeId,
                weekday0,
                Time.valueOf(start),
                Time.valueOf(end),
                from,
                toExclusive
        );
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

    private LocalDateTime resolveRangeEndExclusiveOrMonthEnd(LocalDateTime from) {
        LocalDateTime globalMaxEndDt = visitScheduleRepository.findGlobalMaxEndDt();
        if (globalMaxEndDt == null) {
            YearMonth ym = YearMonth.from(from.toLocalDate());
            return ym.plusMonths(1).atDay(1).atStartOfDay();
        }
        YearMonth ym = YearMonth.from(globalMaxEndDt.toLocalDate());
        return ym.plusMonths(1).atDay(1).atStartOfDay();
    }

    private void validateBasic(Integer day, LocalTime startTime, LocalTime endTime) {
        if (day == null || day < 1 || day > 7) {
            throw new IllegalArgumentException("day는 1~7이어야 합니다.");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime/endTime은 필수입니다.");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("startTime은 endTime보다 빨라야 합니다.");
        }
    }

    private BeneficiaryScheduleResponse toResponse(BeneficiarySchedule bs) {
        return new BeneficiaryScheduleResponse(
                bs.getId(),
                bs.getBeneficiaryId(),
                bs.getServiceTypeId(),
                bs.getDay(),
                bs.getStartTime(),
                bs.getEndTime()
        );
    }
}