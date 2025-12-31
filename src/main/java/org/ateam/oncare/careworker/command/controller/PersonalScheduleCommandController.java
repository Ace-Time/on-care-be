package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.dto.UpdatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.service.PersonalScheduleCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-schedules")
@RequiredArgsConstructor
public class PersonalScheduleCommandController {

    private final PersonalScheduleCommandService personalScheduleCommandService;

    // 1. 개인 일정 작성
    @PostMapping
    public ApiResponse<Void> createPersonalSchedule(
            @AuthenticationPrincipal EmployeeImpl employee,
            @RequestBody CreatePersonalScheduleRequest request) {
        personalScheduleCommandService.createPersonalSchedule(employee.getId(), request);
        return ApiResponse.success(null);
    }

    // 2. 개인 일정 수정
    @PatchMapping("/{psId}")
    public ApiResponse<Void> updatePersonalSchedule(
            @PathVariable Long psId,
            @RequestBody UpdatePersonalScheduleRequest request) {
        personalScheduleCommandService.updatePersonalSchedule(psId, request);
        return ApiResponse.success(null);
    }

    // 3. 개인 일정 삭제
    @DeleteMapping("/{psId}")
    public ApiResponse<Void> deletePersonalSchedule(@PathVariable Long psId) {
        personalScheduleCommandService.deletePersonalSchedule(psId);
        return ApiResponse.success(null);
    }
}
