//package org.ateam.oncare.beneficiary.command.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.math.BigDecimal;
//
///**
// * 수급자별 방문 서비스 비용 상세 기록
// * 각 방문(visit_schedule)마다 계산된 비용을 개별적으로 저장
// */
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "cost_of_beneficiary_record")
//public class CostOfBeneficiaryRecord {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    /**
//     * 방문 일정 ID (visit_schedule.vs_id)
//     */
//    @Column(name = "visit_schedule_id", nullable = false)
//    private Long visitScheduleId;
//
//    /**
//     * 수급자 ID
//     */
//    @Column(name = "beneficiary_id", nullable = false)
//    private Long beneficiaryId;
//
//    /**
//     * 서비스 유형 ID (m_service_type.id)
//     */
//    @Column(name = "service_type_id", nullable = false)
//    private Long serviceTypeId;
//
//    /**
//     * 계산된 비용 금액
//     * 공식: ROUND(근무시간(분) / 60, 1) * 시급
//     */
//    @Column(name = "calculated_amount", nullable = false, precision = 10, scale = 2)
//    private BigDecimal calculatedAmount;
//}
