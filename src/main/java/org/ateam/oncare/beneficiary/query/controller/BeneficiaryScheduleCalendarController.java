package org.ateam.oncare.beneficiary.query.controller;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.ScheduleCalendarResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryScheduleCalendarService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryScheduleCalendarController {

    private final BeneficiaryScheduleCalendarService service;

    /**
     * ✅ 월 캘린더 조회 (visit_schedule만)
     * GET /api/beneficiaries/{beneficiaryId}/schedules/calendar?year=2025&month=12
     */
    @GetMapping("/{beneficiaryId}/schedules/calendar")
    public ScheduleCalendarResponse getCalendar(
            @PathVariable Long beneficiaryId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return service.getMonthlyCalendar(beneficiaryId, year, month);
    }
}