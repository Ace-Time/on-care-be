package org.ateam.oncare.matching.command.repository;

import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByBeneficiaryIdAndStatus(Long beneficiaryId, String status);
}