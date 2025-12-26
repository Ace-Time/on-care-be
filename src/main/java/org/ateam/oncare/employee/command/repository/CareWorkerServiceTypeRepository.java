package org.ateam.oncare.employee.command.repository;


import org.ateam.oncare.careworker.command.entity.CareWorkerServiceType;
import org.ateam.oncare.careworker.command.entity.CareWorkerServiceTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareWorkerServiceTypeRepository extends JpaRepository<CareWorkerServiceType, CareWorkerServiceTypeId> {
    // 수정 시 기존 서비스 유형 삭제를 위해
    void deleteAllByCareWorkerId(Long careWorkerId);
}