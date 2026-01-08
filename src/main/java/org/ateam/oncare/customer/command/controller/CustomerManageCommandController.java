package org.ateam.oncare.customer.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.customer.command.dto.CustomerManageCommandDTO;
import org.ateam.oncare.customer.command.dto.notifyRequest;
import org.ateam.oncare.customer.command.dto.notifyResponse;
import org.ateam.oncare.customer.command.service.CustomerManageCommandService;
import org.ateam.oncare.customer.command.service.NotifyFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customer-manage")
@RequiredArgsConstructor
public class CustomerManageCommandController {

    private final NotifyFacadeService notifyFacadeService;

    @PostMapping("/notify/churn-risk/{beneficiaryId}")
    public ResponseEntity<notifyResponse> notifyChurnRisk(@PathVariable("beneficiaryId") Long beneficiaryId) {
        return notifyFacadeService.notifyChurnRisk(beneficiaryId);
    }

    @PostMapping("/notify/complain/{beneficiaryId}")
    public ResponseEntity<notifyResponse> notifyComplain(@PathVariable("beneficiaryId") Long beneficiaryId) {
        return notifyFacadeService.notifyComplain(beneficiaryId);
    }

    @PostMapping("/notify/termination/{beneficiaryId}")
    public ResponseEntity<notifyResponse> notifyTermination(@PathVariable("beneficiaryId") Long beneficiaryId) {
        return notifyFacadeService.notifyTermination(beneficiaryId);
    }

    @PostMapping("/notify/expiration/{beneficiaryId}")
    public ResponseEntity<notifyResponse> notifyExpiration(@PathVariable("beneficiaryId") Long beneficiaryId) {
        return notifyFacadeService.notifyExpiration(beneficiaryId);
    }

    @PostMapping("/notify/rental/{beneficiaryId}")
    public ResponseEntity<notifyResponse> notifyRental(@PathVariable("beneficiaryId") Long beneficiaryId) {
        return notifyFacadeService.notifyRental(beneficiaryId);
    }


}
