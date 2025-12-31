package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignedBeneficiaryRepository extends JpaRepository<Matching, Long> {
    // 요양보호사 ID와 상태(Y)로 조회
    List<Matching> findByCareWorkerIdAndStatus(Long careWorkerId, String status);
}