package org.ateam.oncare.schedule.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarDayItemDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarMonthCountDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarScheduleDetailDto;
import org.ateam.oncare.schedule.query.dto.SchedulePageResponse;
import org.ateam.oncare.schedule.query.service.ConfirmedCalendarScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/confirmed-calendar")
public class ConfirmedCalendarScheduleController {

    private final ConfirmedCalendarScheduleService confirmedCalendarScheduleService;

    @GetMapping("/range-counts")
    public List<ConfirmedCalendarMonthCountDto> rangeCounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) Long beneficiaryId,
            @RequestParam(required = false) Integer careWorkerId,
            @RequestParam(required = false) Integer serviceTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String searchField
    ) {
        return confirmedCalendarScheduleService.getConfirmedCalendarCounts(
                start, end, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField
        );
    }

    @GetMapping("/day-list")
    public SchedulePageResponse<ConfirmedCalendarDayItemDto> dayList(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long beneficiaryId,
            @RequestParam(required = false) Integer careWorkerId,
            @RequestParam(required = false) Integer serviceTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String searchField,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return confirmedCalendarScheduleService.getConfirmedCalendarDayPage(
                date, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField, page, size
        );
    }

    @GetMapping("/detail")
    public ConfirmedCalendarScheduleDetailDto detail(
            @RequestParam Integer vsId
    ) {
        return confirmedCalendarScheduleService.getConfirmedCalendarDetail(vsId);
    }
}