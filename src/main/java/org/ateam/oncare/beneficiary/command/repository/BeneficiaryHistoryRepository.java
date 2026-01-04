package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.BeneficiaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryHistoryRepository extends JpaRepository<BeneficiaryHistory, Long> {
}
