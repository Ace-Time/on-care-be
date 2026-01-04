package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.BeneficiaryCareLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryCareLevelRepository extends JpaRepository<BeneficiaryCareLevel,Long> {
}
