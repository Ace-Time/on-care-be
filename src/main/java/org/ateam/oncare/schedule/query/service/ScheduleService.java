package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ScheduleDayItemDto;
import org.ateam.oncare.schedule.query.dto.ScheduleMonthCountDto;
import org.ateam.oncare.schedule.query.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;

    public List<ScheduleMonthCountDto> getRangeCounts(
            String start, String end,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return scheduleMapper.selectRangeCounts(
                startDate, endDate,
                beneficiaryId, careWorkerId, serviceTypeId,
                keyword, normalizeSearchField(searchField)
        );
    }

    public List<ScheduleDayItemDto> getDaySchedules(
            String date,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        LocalDate day = LocalDate.parse(date);

        return scheduleMapper.selectDaySchedules(
                day,
                beneficiaryId, careWorkerId, serviceTypeId,
                keyword, normalizeSearchField(searchField)
        );
    }

    public ScheduleDayItemDto getScheduleDetail(
            Long matchingId,
            String date,
            Integer serviceTypeId,
            String startTime
    ) {
        LocalDate day = LocalDate.parse(date);
        LocalTime st = (startTime == null || startTime.isBlank()) ? null : LocalTime.parse(startTime);

        return scheduleMapper.selectScheduleDetail(
                matchingId,
                day,
                serviceTypeId,
                st
        );
    }

    private String normalizeSearchField(String v) {
        if (v == null) return "ALL";
        String upper = v.trim().toUpperCase();
        return switch (upper) {
            case "ALL", "BENEFICIARY", "CAREWORKER", "SERVICE" -> upper;
            default -> "ALL";
        };
    }
}