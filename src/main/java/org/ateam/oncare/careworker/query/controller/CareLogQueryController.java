package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.CareLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CareLogListDto;
import org.ateam.oncare.careworker.query.service.CareLogQueryService;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/care-logs")
@RequiredArgsConstructor
public class CareLogQueryController {

    private final CareLogQueryService careLogQueryService;

    // 1. 요양일지 목록 조회 (요양보호사별)
    @GetMapping
    public ApiResponse<List<CareLogListDto>> getCareLogList(
            @AuthenticationPrincipal EmployeeImpl employee) {
        List<CareLogListDto> data = careLogQueryService.getCareLogList(employee.getId());
        return ApiResponse.success(data);
    }

    // 2. 요양일지 목록 조회 (수급자별)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<List<CareLogListDto>> getCareLogListByBeneficiary(@PathVariable Long beneficiaryId) {
        List<CareLogListDto> data = careLogQueryService.getCareLogListByBeneficiary(beneficiaryId);
        return ApiResponse.success(data);
    }

    // 3. 요양일지 상세 조회
    @GetMapping("/{logId}")
    public ApiResponse<CareLogDetailDto> getCareLogDetail(@PathVariable Long logId) {
        CareLogDetailDto data = careLogQueryService.getCareLogDetail(logId);
        return ApiResponse.success(data);
    }
}
