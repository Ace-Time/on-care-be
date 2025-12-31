package org.ateam.oncare.payment.command.service;

import org.ateam.oncare.payment.command.dto.ElectronicPaymentCreate;

public interface ElectronicPaymentService {
    Long createPayment(ElectronicPaymentCreate request);
}
