package org.ateam.oncare.schedule.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.ScheduleDayItemDto;
import org.ateam.oncare.schedule.query.dto.ScheduleMonthCountDto;
import org.ateam.oncare.schedule.query.service.ScheduleQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleQueryController {

    private final ScheduleQueryService scheduleQueryService;

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
        return scheduleQueryService.getRangeCounts(
                start, end,
                beneficiaryId, careWorkerId, serviceTypeId,
                keyword, searchField
        );
    }

    @GetMapping("/day")
    public List<ScheduleDayItemDto> getDaySchedules(
            @RequestParam String date,
            @RequestParam(required = false) Long beneficiaryId,
            @RequestParam(required = false) Integer careWorkerId,
            @RequestParam(required = false) Integer serviceTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "ALL") String searchField
    ) {
        return scheduleQueryService.getDaySchedules(
                date,
                beneficiaryId, careWorkerId, serviceTypeId,
                keyword, searchField
        );
    }
}