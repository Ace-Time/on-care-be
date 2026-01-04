package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.BeneficiaryCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryCountRepository extends JpaRepository<BeneficiaryCount,Long> {
}
