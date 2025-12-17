package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.CalendarScheduleDto;
import org.ateam.oncare.careworker.query.dto.PersonalTypeDto;
import org.ateam.oncare.careworker.query.dto.ScheduleDetailDto;
import org.ateam.oncare.careworker.query.service.ScheduleQueryService;
import org.ateam.oncare.employee.query.dto.VisitScheduleVO;
import org.ateam.oncare.employee.query.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleQueryController {

    private final ScheduleQueryService scheduleQueryService;
    private final EmployeeQueryService employeeQueryService;

    // ⚠️ 테스트용: 로그인한 유저 대신 ID를 1로 고정합니다.
    private final Long TEST_CAREGIVER_ID = 1L;

    // 캘린더 일정 조회 (월간/주간)
    // 1. 일별 조회 (오늘)
    // GET /api/schedules?startDate=2025-12-16&endDate=2025-12-16
    // 2. 주별 조회 (이번 주: 월요일~일요일)
    // GET /api/schedules?startDate=2025-12-16&endDate=2025-12-22
    // 3. 월별 조회 (12월 전체)
    // GET /api/schedules?startDate=2025-12-01&endDate=2025-12-31
    @GetMapping
    public List<CalendarScheduleDto> getSchedules(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return scheduleQueryService.getSchedules(TEST_CAREGIVER_ID, startDate, endDate);
    }

    // 일정 상세 조회
    // 요청 예시: GET /api/schedules/101
    @GetMapping("/{scheduleId}")
    public ScheduleDetailDto getScheduleDetail(
            @PathVariable Long scheduleId) {
        return scheduleQueryService.getScheduleDetail(scheduleId, TEST_CAREGIVER_ID);
    }

    // 직원(요양보호사)별 일정 조회
    // GET /api/schedules/employees/{id}
    @GetMapping("/employees/{employeeId}")
    public List<VisitScheduleVO> getEmployeeSchedules(@PathVariable Integer employeeId) {
        return employeeQueryService.getEmployeeSchedules(employeeId);
    }

    // 개인 일정 유형 목록 조회 (드롭다운용)
    // 요청 예시: GET /api/schedules/personal-types
    @GetMapping("/personal-types")
    public ApiResponse<List<PersonalTypeDto>> getPersonalTypes() {
        List<PersonalTypeDto> types = scheduleQueryService.getPersonalTypes();
        return ApiResponse.success(types);
    }
}