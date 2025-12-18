package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarDayItemDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarMonthCountDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarScheduleDetailDto;
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
}