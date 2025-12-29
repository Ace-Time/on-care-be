package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarDayItemDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarMonthCountDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarScheduleDetailDto;
import org.ateam.oncare.schedule.query.dto.SchedulePageResponse;
import org.ateam.oncare.schedule.query.mapper.ConfirmedCalendarScheduleMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfirmedCalendarScheduleService {

    private final ConfirmedCalendarScheduleMapper confirmedCalendarScheduleMapper;

    public List<ConfirmedCalendarMonthCountDto> getConfirmedCalendarCounts(
            LocalDate start, LocalDate end,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        return confirmedCalendarScheduleMapper.selectRangeCounts(
                start, end, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField
        );
    }

    public List<ConfirmedCalendarDayItemDto> getConfirmedCalendarDayList(
            LocalDate date,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        return confirmedCalendarScheduleMapper.selectDayList(
                date, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField
        );
    }

    public ConfirmedCalendarScheduleDetailDto getConfirmedCalendarDetail(Integer vsId) {
        return confirmedCalendarScheduleMapper.selectDetailByVsId(vsId);
    }
    public SchedulePageResponse<ConfirmedCalendarDayItemDto> getConfirmedCalendarDayPage(
            LocalDate date,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField,
            int page, int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = safePage * safeSize;

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        String sf = (searchField == null || searchField.trim().isEmpty()) ? "ALL" : searchField.trim().toUpperCase();

        List<ConfirmedCalendarDayItemDto> list =
                confirmedCalendarScheduleMapper.selectDayListPaged(
                        date, beneficiaryId, careWorkerId, serviceTypeId, q, sf, offset, safeSize
                );

        long total =
                confirmedCalendarScheduleMapper.countDayList(
                        date, beneficiaryId, careWorkerId, serviceTypeId, q, sf
                );

        return new SchedulePageResponse<>(list, safePage, safeSize, total);
    }
}