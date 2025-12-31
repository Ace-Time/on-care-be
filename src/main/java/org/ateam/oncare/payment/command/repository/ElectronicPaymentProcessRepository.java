package org.ateam.oncare.payment.command.repository;

import org.ateam.oncare.payment.command.entity.ElectronicPaymentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectronicPaymentProcessRepository extends JpaRepository<ElectronicPaymentProcess, Integer> {
}
