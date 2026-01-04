package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.ExpirationOfCareLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpirationOfCareLevelRepository extends JpaRepository<ExpirationOfCareLevel,Long> {
}
