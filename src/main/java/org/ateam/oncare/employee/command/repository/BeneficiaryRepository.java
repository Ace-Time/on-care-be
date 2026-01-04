package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    boolean existsByPotentialCustomerId(Long potentialCustomerId);
}
