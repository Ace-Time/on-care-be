package org.ateam.oncare.payment.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.payment.command.dto.ElectronicPaymentCreate;
import org.ateam.oncare.payment.command.service.ElectronicPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ElectronicPaymentCommandController {
    private final ElectronicPaymentService electronicPaymentService;

    // 결재 문서 생성 (기안)
    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody ElectronicPaymentCreate request) {
        Long paymentId = electronicPaymentService.createPayment(request);
        return ResponseEntity.ok("결재 문서가 생성되었습니다. ID: " + paymentId);
    }
}
