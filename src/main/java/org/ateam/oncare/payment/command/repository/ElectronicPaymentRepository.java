package org.ateam.oncare.payment.command.repository;

import org.ateam.oncare.payment.command.entity.ElectronicPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectronicPaymentRepository extends JpaRepository<ElectronicPayment, Integer> {
}
