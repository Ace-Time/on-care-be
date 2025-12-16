package org.ateam.oncare.careworker.command.repository;

import org.ateam.oncare.careworker.command.entity.CareWorkerCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareWorkerCertificateRepository extends JpaRepository<CareWorkerCertificate, Long> {
    // 직원 수정 시 기존 자격증 삭제를 위해 필요 (careWorker.id 기준 삭제)
    void deleteAllByCareWorkerId(Long careWorkerId);

    // 요양보호사 ID로 자격증 목록 조회
    List<CareWorkerCertificate> findAllByCareWorkerId(Long careWorkerId);
}