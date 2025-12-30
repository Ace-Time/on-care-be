package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.careworker.command.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    // 특정 자격증 ID 목록에 포함된 모든 교육 이력 삭제
    @Modifying
    @Query("DELETE FROM Education e WHERE e.careWorkerCertificateId IN :certIds")
    void deleteAllByCareWorkerCertificateIdIn(@Param("certIds") List<Long> certIds);

    // 다음 교육일(만료일)로 검색
    List<Education> findByNextEduDate(java.time.LocalDate nextEduDate);
}