package org.ateam.oncare.payment.command.repository;

import org.ateam.oncare.payment.command.entity.ElectronicPaymentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectronicPaymentProcessRepository extends JpaRepository<ElectronicPaymentProcess, Integer> {
    List<ElectronicPaymentProcess> findByElectronicPaymentIdOrderByStepOrderAsc(Integer electronicPaymentId);

    // 특정 문서에서 내 결재 순서 찾기 (현재 대기중인 것)
    Optional<ElectronicPaymentProcess> findByElectronicPaymentIdAndEmployeeIdAndStatus(Integer electronicPaymentId,
            Long employeeId, Integer status);

    // 특정 문서의 특정 순서 찾기
    Optional<ElectronicPaymentProcess> findByElectronicPaymentIdAndStepOrder(Integer electronicPaymentId,
            Integer stepOrder);
}
