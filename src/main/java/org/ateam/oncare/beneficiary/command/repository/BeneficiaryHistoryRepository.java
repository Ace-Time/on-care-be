package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.BeneficiaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeneficiaryHistoryRepository extends JpaRepository<BeneficiaryHistory, Long> {
    Optional<BeneficiaryHistory> findByBeneficiaryId(Long beneficiaryId);
}
