//package org.ateam.oncare.beneficiary.command.repository;
//
//import org.ateam.oncare.beneficiary.command.entity.CostOfBeneficiaryRecord;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//@Repository
//public interface CostOfBeneficiaryRecordRepository extends JpaRepository<CostOfBeneficiaryRecord, Long> {
//
//    /**
//     * 특정 방문 일정에 대한 비용 기록이 이미 존재하는지 확인
//     * (중복 저장 방지)
//     */
//    boolean existsByVisitScheduleId(Long visitScheduleId);
//
//    /**
//     * 특정 방문 일정의 비용 기록 조회
//     */
//    Optional<CostOfBeneficiaryRecord> findByVisitScheduleId(Long visitScheduleId);
//
//    /**
//     * 특정 수급자의 특정 월(YYYY-MM) 총 비용 계산
//     * cost_of_beneficiary 테이블 업데이트에 사용
//     */
//    @Query("""
//        SELECT COALESCE(SUM(cbr.calculatedAmount), 0)
//        FROM CostOfBeneficiaryRecord cbr
//        JOIN VisitSchedule vs ON vs.vsId = cbr.visitScheduleId
//        WHERE cbr.beneficiaryId = :beneficiaryId
//          AND DATE_FORMAT(vs.rfidEndTime, '%Y-%m') = :month
//    """)
//    BigDecimal calculateMonthlyTotal(
//            @Param("beneficiaryId") Long beneficiaryId,
//            @Param("month") String month
//    );
//}
