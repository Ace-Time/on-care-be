package org.ateam.oncare.payment.command.service;

import org.ateam.oncare.payment.command.dto.PaymentActionRequest;
import org.ateam.oncare.payment.command.dto.PaymentCreateRequest;

public interface PaymentCommandService {
    // 결재 기안
    void createPayment(Long drafterId, PaymentCreateRequest request);

    // 결재 승인/반려 처리
    void processPayment(Long approverId, Long paymentId, PaymentActionRequest request);
}
