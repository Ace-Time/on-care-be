package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CareWorkerInfoRepository extends JpaRepository<CareWorker, Long> {
    // 직원 ID로 요양보호사 정보 찾기
    Optional<CareWorker> findByEmployeeId(Long employeeId);
}