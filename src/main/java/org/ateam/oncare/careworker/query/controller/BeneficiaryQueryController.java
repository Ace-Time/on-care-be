package org.ateam.oncare.careworker.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.MyBeneficiaryDto;
import org.ateam.oncare.careworker.query.service.BeneficiaryQueryService;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 요양보호사의 배정 수급자 조회 API
 */
@RestController
@RequestMapping("/api/careworker/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryQueryController {

    private final BeneficiaryQueryService beneficiaryQueryService;

    /**
     * 로그인한 요양보호사에게 배정된 수급자 목록 조회
     * GET /api/careworker/beneficiaries
     */
    @GetMapping
    public ApiResponse<List<MyBeneficiaryDto>> getAssignedBeneficiaries(
            @AuthenticationPrincipal EmployeeImpl employee) {
        List<MyBeneficiaryDto> beneficiaries = beneficiaryQueryService.getAssignedBeneficiaries(employee.getId());
        return ApiResponse.success(beneficiaries);
    }

    /**
     * 로그인한 요양보호사에게 배정된 특정 수급자 상세 조회
     * GET /api/careworker/beneficiaries/{beneficiaryId}
     */
    @GetMapping("/{beneficiaryId}")
    public ApiResponse<MyBeneficiaryDto> getAssignedBeneficiaryDetail(
            @AuthenticationPrincipal EmployeeImpl employee,
            @PathVariable Long beneficiaryId) {
        MyBeneficiaryDto beneficiary = beneficiaryQueryService.getAssignedBeneficiaryDetail(employee.getId(),
                beneficiaryId);

        if (beneficiary == null) {
            return ApiResponse.error(404, "배정되지 않은 수급자이거나 존재하지 않는 수급자입니다.");
        }

        return ApiResponse.success(beneficiary);
    }
}
