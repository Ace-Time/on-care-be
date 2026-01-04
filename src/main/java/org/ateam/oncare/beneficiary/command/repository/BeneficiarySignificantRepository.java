package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.BeneficiarySignificant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiarySignificantRepository extends JpaRepository<BeneficiarySignificant, Long> {
    void deleteByBeneficiaryIdAndSignificantId(Long beneficiaryId, Long significantId);
}
