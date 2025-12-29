package org.ateam.oncare.beneficiary.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.command.entity.CostOfBeneficiary;
import org.ateam.oncare.beneficiary.command.entity.CostOfBeneficiaryRecord;
import org.ateam.oncare.beneficiary.command.repository.CostOfBeneficiaryRecordRepository;
import org.ateam.oncare.beneficiary.command.repository.CostOfBeneficiaryRepository;
import org.ateam.oncare.careworker.command.entity.MServiceType;
import org.ateam.oncare.careworker.command.repository.MServiceTypeRepository;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeneficiaryCostService {

    private final CostOfBeneficiaryRepository costOfBeneficiaryRepository;
    private final CostOfBeneficiaryRecordRepository costOfBeneficiaryRecordRepository;
    private final MServiceTypeRepository mServiceTypeRepository;
    private final VisitScheduleRepository visitScheduleRepository;

    /**
     * RFID 출퇴근 완료 시 비용 계산 및 누적
     * 1. cost_of_beneficiary_record에 개별 방문 비용 기록 저장
     * 2. cost_of_beneficiary에 월별 누적 금액 업데이트
     *
     * @param vsId 방문 일정 ID
     */
    @Transactional
    public void accumulateCostForCompletedVisit(Long vsId) {
        log.info("비용 계산 및 누적 시작 - vsId: {}", vsId);

        // 0. 중복 저장 방지 - 이미 기록이 있으면 건너뜀
        if (costOfBeneficiaryRecordRepository.existsByVisitScheduleId(vsId)) {
            log.info("이미 비용이 계산되어 저장된 방문입니다. 건너뜀 - vsId: {}", vsId);
            return;
        }

        // 1. 방문 일정 조회
        VisitSchedule visitSchedule = visitScheduleRepository.findById(vsId)
                .orElseThrow(() -> new IllegalArgumentException("방문 일정을 찾을 수 없습니다. vsId: " + vsId));

        // 2. RFID 시간 확인
        LocalDateTime rfidStartTime = visitSchedule.getRfidStartTime();
        LocalDateTime rfidEndTime = visitSchedule.getRfidEndTime();

        if (rfidStartTime == null || rfidEndTime == null) {
            log.warn("RFID 시간이 기록되지 않았습니다. 비용 계산을 건너뜁니다. vsId: {}", vsId);
            return;
        }

        // 3. 서비스 유형 및 단가 조회
        MServiceType serviceType = mServiceTypeRepository.findById(visitSchedule.getServiceTypeId())
                .orElseThrow(() -> new IllegalArgumentException("서비스 유형을 찾을 수 없습니다. serviceTypeId: " + visitSchedule.getServiceTypeId()));

        // 4. 근무 시간 계산 (분 단위)
        long workMinutes = Duration.between(rfidStartTime, rfidEndTime).toMinutes();
        log.info("근무 시간: {} 분", workMinutes);

        // 5. 비용 계산
        // 공식: ROUND(근무분 / 60, 1) * 시급
        BigDecimal hourlyRate = BigDecimal.valueOf(serviceType.getMoney());
        BigDecimal workHours = BigDecimal.valueOf(workMinutes)
                .divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_UP);
        BigDecimal calculatedCost = workHours.multiply(hourlyRate)
                .setScale(2, RoundingMode.HALF_UP);

        log.info("계산된 비용: {} 원 (시급: {}, 근무시간: {}h)", calculatedCost, hourlyRate, workHours);

        // 6. 개별 방문 비용 기록 저장 (cost_of_beneficiary_record)
        CostOfBeneficiaryRecord record = CostOfBeneficiaryRecord.builder()
                .visitScheduleId(vsId)
                .beneficiaryId(visitSchedule.getBeneficiaryId())
                .serviceTypeId(visitSchedule.getServiceTypeId())
                .calculatedAmount(calculatedCost)
                .build();

        costOfBeneficiaryRecordRepository.save(record);
        log.info("개별 비용 기록 저장 완료 - recordId: {}", record.getId());

        // 7. 월별 누적 금액 재계산 및 업데이트
        updateMonthlyCost(visitSchedule.getBeneficiaryId(), rfidEndTime);
    }

    /**
     * 특정 수급자의 특정 월 누적 비용 재계산 및 업데이트
     * cost_of_beneficiary_record의 모든 기록을 합산하여 정확한 월별 합계 계산
     *
     * @param beneficiaryId 수급자 ID
     * @param dateTime 기준 날짜/시간 (월 추출용)
     */
    private void updateMonthlyCost(Long beneficiaryId, LocalDateTime dateTime) {
        String month = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // cost_of_beneficiary_record에서 해당 월의 총 비용 계산
        BigDecimal monthlyTotal = costOfBeneficiaryRecordRepository
                .calculateMonthlyTotal(beneficiaryId, month);

        log.info("월별 총 비용 계산 완료 - 수급자ID: {}, 월: {}, 총액: {}", beneficiaryId, month, monthlyTotal);

        // cost_of_beneficiary 테이블 업데이트
        CostOfBeneficiary costOfBeneficiary = costOfBeneficiaryRepository
                .findByBeneficiaryIdAndMonth(beneficiaryId, month)
                .orElseGet(() -> {
                    CostOfBeneficiary newCost = new CostOfBeneficiary();
                    newCost.setBeneficiaryId(beneficiaryId);
                    newCost.setMonth(month);
                    newCost.setMonthlyAmount(BigDecimal.ZERO);
                    return newCost;
                });

        BigDecimal previousAmount = costOfBeneficiary.getMonthlyAmount();
        costOfBeneficiary.setMonthlyAmount(monthlyTotal);
        costOfBeneficiaryRepository.save(costOfBeneficiary);

        log.info("월별 누적 금액 업데이트 완료 - 수급자ID: {}, 월: {}, 이전: {}, 갱신: {}",
                beneficiaryId, month, previousAmount, monthlyTotal);
    }
}
