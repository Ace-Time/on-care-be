package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateCareLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCareLogRequest;
import org.ateam.oncare.careworker.command.service.CareLogCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/care-logs")
@RequiredArgsConstructor
public class CareLogCommandController {

    private final CareLogCommandService careLogCommandService;

    // 1. 요양일지 작성
    @PostMapping
    public ApiResponse<Void> createCareLog(
            @AuthenticationPrincipal EmployeeImpl employee,
            @RequestBody CreateCareLogRequest request) {
        careLogCommandService.createCareLog(employee.getId(), request);
        return ApiResponse.success(null);
    }

    // 2. 요양일지 수정
    @PatchMapping("/{logId}")
    public ApiResponse<Void> updateCareLog(
            @PathVariable Long logId,
            @RequestBody UpdateCareLogRequest request) {
        careLogCommandService.updateCareLog(logId, request);
        return ApiResponse.success(null);
    }

    // 3. 요양일지 삭제 (논리삭제)
    @DeleteMapping("/{logId}")
    public ApiResponse<Void> deleteCareLog(@PathVariable Long logId) {
        careLogCommandService.deleteCareLog(logId);
        return ApiResponse.success(null);
    }
}
