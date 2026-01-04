package org.ateam.oncare.schedule.command.repository;

import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.entity.VisitSchedule.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    @Query("""
        select max(v.endDt)
        from VisitSchedule v
    """)
    LocalDateTime findGlobalMaxEndDt();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update VisitSchedule v
           set v.careWorkerId = :newCareWorkerId
         where v.beneficiaryId = :beneficiaryId
           and v.startDt >= :from
           and v.startDt <  :toExclusive
           and v.visitStatus = :status
    """)
    int bulkUpdateCareWorkerId(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("from") LocalDateTime from,
            @Param("toExclusive") LocalDateTime toExclusive,
            @Param("status") VisitStatus status,
            @Param("newCareWorkerId") Long newCareWorkerId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from VisitSchedule v
         where v.beneficiaryId = :beneficiaryId
           and v.startDt >= :from
           and v.startDt <  :toExclusive
           and v.visitStatus = :status
    """)
    int bulkDeleteByBeneficiaryAndRangeAndStatus(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("from") LocalDateTime from,
            @Param("toExclusive") LocalDateTime toExclusive,
            @Param("status") VisitStatus status
    );

    // (추가) 이미 동일 일정이 있으면 생성 스킵용
    boolean existsByBeneficiaryIdAndServiceTypeIdAndStartDtAndEndDtAndVisitStatus(
            Long beneficiaryId,
            Integer serviceTypeId,
            LocalDateTime startDt,
            LocalDateTime endDt,
            VisitStatus visitStatus
    );

    // (추가) beneficiary + 기간 + status로 기존 일정 조회 (있으면 careWorkerId 세팅용)
    List<VisitSchedule> findByBeneficiaryIdAndStartDtGreaterThanEqualAndStartDtLessThanAndVisitStatus(
            Long beneficiaryId,
            LocalDateTime from,
            LocalDateTime toExclusive,
            VisitStatus visitStatus
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
    delete from visit_schedule
     where beneficiary_id = :beneficiaryId
       and visit_status = 'SCHEDULED'
       and start_dt >= :from
       and start_dt <  :toExclusive
       and service_type_id = :serviceTypeId
       and weekday(start_dt) = :weekday0
       and time(start_dt) = :startTime
       and time(end_dt)   = :endTime
""", nativeQuery = true)
    int deleteScheduledByPatternInRange(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("serviceTypeId") Long serviceTypeId,
            @Param("weekday0") int weekday0, // 월=0 ... 일=6
            @Param("startTime") java.sql.Time startTime,
            @Param("endTime") java.sql.Time endTime,
            @Param("from") LocalDateTime from,
            @Param("toExclusive") LocalDateTime toExclusive
    );

    @Query("""
select (count(v) > 0)
from VisitSchedule v
where v.beneficiaryId = :beneficiaryId
  and v.visitStatus = org.ateam.oncare.schedule.command.entity.VisitSchedule.VisitStatus.SCHEDULED
  and v.serviceTypeId = :serviceTypeId
  and v.startDt = :startDt
  and v.endDt = :endDt
""")
    boolean existsScheduledExact(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("serviceTypeId") Long serviceTypeId,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );
}