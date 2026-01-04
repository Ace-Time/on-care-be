package org.ateam.oncare.counsel.command.repository;

import org.ateam.oncare.counsel.command.entity.PotentialStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PotentialStageRepository extends JpaRepository<PotentialStage,Long> {
    /**
     * 특정 잠재고객의 모든 단계 조회 (단계 순으로 정렬)
     */
    List<PotentialStage> findByPotentialCustomerIdOrderByStageAsc(Long potentialId);

    /**
     * 특정 잠재고객의 특정 단계 조회
     */
    Optional<PotentialStage> findByPotentialCustomerIdAndStage(Long potentialId, Integer stage);

}
