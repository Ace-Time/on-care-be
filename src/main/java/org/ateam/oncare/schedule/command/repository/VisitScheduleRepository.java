package org.ateam.oncare.schedule.command.repository;

import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VisitScheduleRepository extends JpaRepository<VisitSchedule, Long> {

    @Query("""
        select (count(v) > 0)
        from VisitSchedule v
        where v.careWorkerId = :careWorkerId
          and v.vsId <> :vsId
          and v.startDt < :newEnd
          and v.endDt > :newStart
    """)
    boolean existsOverlapForCareWorker(
            @Param("careWorkerId") Long careWorkerId,
            @Param("vsId") Long vsId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );

    @Query("""
        select (count(v) > 0)
        from VisitSchedule v
        where v.beneficiaryId = :beneficiaryId
          and v.vsId <> :vsId
          and v.startDt < :newEnd
          and v.endDt > :newStart
    """)
    boolean existsOverlapForBeneficiary(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("vsId") Long vsId,
            @Param("newStart") LocalDateTime newStart,
            @Param("newEnd") LocalDateTime newEnd
    );
}