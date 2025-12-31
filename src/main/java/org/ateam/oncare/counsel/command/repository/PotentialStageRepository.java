package org.ateam.oncare.counsel.command.repository;

import org.ateam.oncare.counsel.command.entity.PotentialStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PotentialStageRepository extends JpaRepository<PotentialStage,Long> {
    List<PotentialStage> findAllByPotentialCustomerId(Long potentialId);

    Optional<PotentialStage> findByPotentialCustomerIdAndStage(long potentialCustomerId, int stage);
}
