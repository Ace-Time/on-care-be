package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianRepository extends JpaRepository<Guardian,Long> {
}
