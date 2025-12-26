package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateCounselingLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCounselingLogRequest;
import org.ateam.oncare.careworker.command.service.CounselingLogCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/counseling-logs")
@RequiredArgsConstructor
public class CounselingLogCommandController {

    private final CounselingLogCommandService counselingLogCommandService;

    // 1. 방문상담 작성
    @PostMapping
    public ApiResponse<Void> createCounselingLog(
            @RequestHeader("Care-Worker-Id") Long careWorkerId,
            @RequestBody CreateCounselingLogRequest request) {
        counselingLogCommandService.createCounselingLog(careWorkerId, request);
        return ApiResponse.success(null);
    }

    // 2. 방문상담 수정
    @PatchMapping("/{counselingId}")
    public ApiResponse<Void> updateCounselingLog(
            @PathVariable Long counselingId,
            @RequestBody UpdateCounselingLogRequest request) {
        counselingLogCommandService.updateCounselingLog(counselingId, request);
        return ApiResponse.success(null);
    }

    // 3. 방문상담 삭제
    @DeleteMapping("/{counselingId}")
    public ApiResponse<Void> deleteCounselingLog(@PathVariable Long counselingId) {
        counselingLogCommandService.deleteCounselingLog(counselingId);
        return ApiResponse.success(null);
    }
}
