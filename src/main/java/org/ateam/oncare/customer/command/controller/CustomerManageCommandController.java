package org.ateam.oncare.customer.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.customer.command.dto.CustomerManageCommandDTO;
import org.ateam.oncare.customer.command.service.CustomerManageCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customer-manage")
@RequiredArgsConstructor
public class CustomerManageCommandController {

    private final CustomerManageCommandService customerManageCommandService;

    /**
     * 이탈위험 - 상담 요청 알림 발송
     * POST /api/customer-manage/beneficiaries/{beneficiaryId}/churn-risk/notify
     */
    @PostMapping("/beneficiaries/{beneficiaryId}/churn-risk/notify")
    public ResponseEntity<CustomerManageCommandDTO.CommandResponse> sendChurnRiskNotification(
            @PathVariable Long beneficiaryId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer counselorId = Integer.parseInt(userDetails.getUsername());
        log.info("이탈위험 알림 요청 - beneficiaryId: {}, counselorId: {}", beneficiaryId, counselorId);

        CustomerManageCommandDTO.CommandResponse response = customerManageCommandService.sendChurnRiskNotification(beneficiaryId, counselorId);
        return ResponseEntity.ok(response);
    }

    /**
     * 불만상담 - 후속조치 등록 및 알림 발송
     * POST /api/customer-manage/complaint/follow-up
     */
    @PostMapping("/complaint/follow-up")
    public ResponseEntity<CustomerManageCommandDTO.CommandResponse> registerComplaintFollowUp(
            @RequestBody CustomerManageCommandDTO.FollowUpRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer counselorId = Integer.parseInt(userDetails.getUsername());
        log.info("불만상담 후속조치 요청 - beneficiaryId: {}, counselId: {}", request.getBeneficiaryId(), request.getCounselId());

        CustomerManageCommandDTO.CommandResponse response = customerManageCommandService.sendComplaintFollowUpNotification(request, counselorId);
        return ResponseEntity.ok(response);
    }

    /**
     * 해지상담 - 해지 등록
     * POST /api/customer-manage/termination
     */
    @PostMapping("/termination")
    public ResponseEntity<CustomerManageCommandDTO.CommandResponse> registerTermination(
            @RequestBody CustomerManageCommandDTO.TerminationRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer counselorId = Integer.parseInt(userDetails.getUsername());
        log.info("해지 등록 요청 - beneficiaryId: {}, plannedDate: {}", request.getBeneficiaryId(), request.getPlannedTerminationDate());

        CustomerManageCommandDTO.CommandResponse response = customerManageCommandService.registerTermination(request, counselorId);
        return ResponseEntity.ok(response);
    }

    /**
     * 렌탈상담 - 요양보호사 알림 발송
     * POST /api/customer-manage/beneficiaries/{beneficiaryId}/rental/notify
     */
    @PostMapping("/beneficiaries/{beneficiaryId}/rental/notify")
    public ResponseEntity<CustomerManageCommandDTO.CommandResponse> sendRentalNotification(
            @PathVariable Long beneficiaryId
    ) {
        log.info("렌탈 알림 요청 - beneficiaryId: {}", beneficiaryId);

        CustomerManageCommandDTO.CommandResponse response = customerManageCommandService.sendRentalNotification(beneficiaryId);
        return ResponseEntity.ok(response);
    }
}
