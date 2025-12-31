package org.ateam.oncare.schedule.command.repository;

import org.ateam.oncare.beneficiary.command.entity.BeneficiarySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface BeneficiaryScheduleRepository extends JpaRepository<BeneficiarySchedule, Long> {

    boolean existsByBeneficiaryIdAndServiceTypeIdAndDayAndStartTimeAndEndTime(
            Long beneficiaryId,
            Integer serviceTypeId,
            Integer day,
            LocalTime startTime,
            LocalTime endTime
    );

    boolean existsByBeneficiaryIdAndServiceTypeIdAndDayAndStartTimeAndEndTimeAndIdNot(
            Long beneficiaryId,
            Integer serviceTypeId,
            Integer day,
            LocalTime startTime,
            LocalTime endTime,
            Long id
    );

    long countByBeneficiaryId(Long beneficiaryId);

    /**
     * (정책2) 같은 careWorker에게 매칭(Y)된 "다른 수급자"들의 희망시간과 겹치는지 체크
     */
    @Query("""
        select count(bs) > 0
        from Matching m
        join BeneficiarySchedule bs on bs.beneficiaryId = m.beneficiaryId
        where m.careWorkerId = :careWorkerId
          and m.status = 'Y'
          and bs.day = :day
          and m.beneficiaryId <> :beneficiaryId
          and not (bs.endTime <= :startTime or bs.startTime >= :endTime)
          and (:excludeId is null or bs.id <> :excludeId)
    """)
    boolean existsOverlapWithOtherBeneficiaries(
            @Param("careWorkerId") Long careWorkerId,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("day") Integer day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Long excludeId
    );
    List<BeneficiarySchedule> findByBeneficiaryId(Long beneficiaryId);
}