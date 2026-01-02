package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.CounselingLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CounselingLogListDto;
import org.ateam.oncare.careworker.query.service.CounselingLogQueryService;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counseling-logs")
@RequiredArgsConstructor
public class CounselingLogQueryController {

    private final CounselingLogQueryService counselingLogQueryService;

    // 1. 방문상담 목록 조회 (요양보호사별)
    @GetMapping
    public ApiResponse<List<CounselingLogListDto>> getCounselingLogList(
            @AuthenticationPrincipal EmployeeImpl employee) {
        List<CounselingLogListDto> data = counselingLogQueryService.getCounselingLogList(employee.getId());
        return ApiResponse.success(data);
    }

    // 2. 방문상담 목록 조회 (수급자별)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<List<CounselingLogListDto>> getCounselingLogListByBeneficiary(@PathVariable Long beneficiaryId) {
        List<CounselingLogListDto> data = counselingLogQueryService.getCounselingLogListByBeneficiary(beneficiaryId);
        return ApiResponse.success(data);
    }

    // 3. 방문상담 상세 조회
    @GetMapping("/{counselingId}")
    public ApiResponse<CounselingLogDetailDto> getCounselingLogDetail(@PathVariable Long counselingId) {
        CounselingLogDetailDto data = counselingLogQueryService.getCounselingLogDetail(counselingId);
        return ApiResponse.success(data);
    }
}
