package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ScheduleDayItemDto;
import org.ateam.oncare.schedule.query.dto.ScheduleMonthCountDto;
import org.ateam.oncare.schedule.query.mapper.ScheduleQueryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleQueryMapper scheduleQueryMapper;

    public List<ScheduleMonthCountDto> getRangeCounts(
            String start, String end,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return scheduleQueryMapper.selectRangeCounts(
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

        return scheduleQueryMapper.selectDaySchedules(
                day,
                beneficiaryId, careWorkerId, serviceTypeId,
                keyword, normalizeSearchField(searchField)
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