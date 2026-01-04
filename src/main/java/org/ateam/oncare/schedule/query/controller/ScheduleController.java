package org.ateam.oncare.schedule.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ScheduleDayItemDto;
import org.ateam.oncare.schedule.query.dto.ScheduleMonthCountDto;
import org.ateam.oncare.schedule.query.dto.SchedulePageResponse;
import org.ateam.oncare.schedule.query.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/range-counts")
    public List<ScheduleMonthCountDto> getRangeCounts(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) Long beneficiaryId,
            @RequestParam(required = false) Integer careWorkerId,
            @RequestParam(required = false) Integer serviceTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "ALL") String searchField
    ) {
        return scheduleService.getRangeCounts(
                start, end,
                beneficiaryId, careWorkerId, serviceTypeId,
                keyword, searchField
        );
    }

    @GetMapping("/day")
    public SchedulePageResponse<ScheduleDayItemDto> getDaySchedules(
            @RequestParam String date,
            @RequestParam(required = false) Long beneficiaryId,
            @RequestParam(required = false) Integer careWorkerId,
            @RequestParam(required = false) Integer serviceTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "ALL") String searchField,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return scheduleService.getDaySchedulesPage(
                date, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField, page, size
        );
    }

    @GetMapping("/detail")
    public ScheduleDayItemDto getScheduleDetail(
            @RequestParam Long matchingId,
            @RequestParam String date,
            @RequestParam(required = false) Integer serviceTypeId,
            @RequestParam(required = false) String startTime
    ) {
        return scheduleService.getScheduleDetail(matchingId, date, serviceTypeId, startTime);
    }
}