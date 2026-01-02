package org.ateam.oncare.payment.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.ateam.oncare.payment.command.dto.PaymentActionRequest;
import org.ateam.oncare.payment.command.dto.PaymentCreateRequest;
import org.ateam.oncare.payment.command.service.PaymentCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentCommandController {

    private final PaymentCommandService paymentCommandService;

    /**
     * 결재 기안
     * URL: POST /api/payments
     */
    @PostMapping
    public ResponseEntity<Void> createPayment(
            @RequestBody PaymentCreateRequest request,
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        Long drafterId = loginUser.getId();
        paymentCommandService.createPayment(drafterId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 결재 승인/반려 처리
     * URL: POST /api/payments/{id}/action
     */
    @PostMapping("/{id}/action")
    public ResponseEntity<Void> processPayment(
            @PathVariable Long id,
            @RequestBody PaymentActionRequest request,
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }

        Long approverId = loginUser.getId();
        paymentCommandService.processPayment(approverId, id, request);
        return ResponseEntity.ok().build();
    }
}
