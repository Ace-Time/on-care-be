package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.beneficiary.command.service.BeneficiaryCostService;
import org.ateam.oncare.careworker.command.dto.CompleteVisitRequest;
import org.ateam.oncare.careworker.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.dto.StartVisitRequest;
import org.ateam.oncare.careworker.command.dto.UpdateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.mapper.CareLogCommandMapper;
import org.ateam.oncare.careworker.command.mapper.VisitScheduleCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitScheduleCommandService {

    private final VisitScheduleCommandMapper visitScheduleCommandMapper;
    private final CareLogCommandMapper careLogCommandMapper;
    private final BeneficiaryCostService beneficiaryCostService;

    @Transactional
    public void startVisit(Long vsId) {
        log.info("서비스 시작 - vsId: {}", vsId);
        int updated = visitScheduleCommandMapper.updateVisitStatusToInProgress(vsId);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없거나 이미 시작되었습니다.");
        }

        log.info("서비스 시작 완료 - vsId: {} (현재 시간 자동 기록)", vsId);
    }

    @Transactional
    public void completeVisit(Long vsId) {
        log.info("서비스 종료 - vsId: {}", vsId);
        int updated = visitScheduleCommandMapper.updateVisitStatusToCompleted(vsId);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없거나 이미 완료되었습니다.");
        }

        log.info("서비스 종료 완료 - vsId: {} (현재 시간 자동 기록)", vsId);

        // RFID 출퇴근 완료 후 서비스 비용 계산 및 월별 누적
        try {
            beneficiaryCostService.accumulateCostForCompletedVisit(vsId);
        } catch (Exception e) {
            log.error("비용 계산 중 오류 발생 - vsId: {}", vsId, e);
            // 비용 계산 실패해도 서비스 완료는 성공으로 처리 (보상 트랜잭션 필요 시 추가)
        }
    }

    @Transactional
    public void createVisitSchedule(Long careWorkerId, CreateVisitScheduleRequest request) {
        if (request.getStartDt() != null && request.getEndDt() != null
                && request.getStartDt().isAfter(request.getEndDt())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        log.info("방문 요양 일정 작성 시작 - careWorkerId: {}, beneficiaryId: {}", careWorkerId, request.getBeneficiaryId());
        int inserted = visitScheduleCommandMapper.insertVisitSchedule(careWorkerId, request);

        if (inserted == 0) {
            throw new IllegalStateException("방문 요양 일정 작성에 실패했습니다.");
        }

        log.info("방문 요양 일정 작성 완료");
    }

    @Transactional
    public void updateVisitSchedule(Long vsId, UpdateVisitScheduleRequest request) {
        if (request.getStartDt() != null && request.getEndDt() != null
                && request.getStartDt().isAfter(request.getEndDt())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        log.info("방문 요양 일정 수정 시작 - vsId: {}", vsId);
        int updated = visitScheduleCommandMapper.updateVisitSchedule(vsId, request);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 방문 요양 일정을 찾을 수 없습니다. vsId: " + vsId);
        }

        log.info("방문 요양 일정 수정 완료 - vsId: {}", vsId);
    }

    @Transactional
    public void deleteVisitSchedule(Long vsId) {
        log.info("방문 요양 일정 삭제 시작 - vsId: {}", vsId);

        // 1. 관련된 요양일지 먼저 삭제 (논리삭제)
        int careLogsDeleted = careLogCommandMapper.deleteCareLogsByVsId(vsId);
        log.info("관련 요양일지 삭제 완료 - vsId: {}, 삭제된 개수: {}", vsId, careLogsDeleted);

        // 2. 방문 일정 삭제 (물리삭제)
        int deleted = visitScheduleCommandMapper.deleteVisitSchedule(vsId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 방문 요양 일정을 찾을 수 없습니다. vsId: " + vsId);
        }

        log.info("방문 요양 일정 삭제 완료 - vsId: {}", vsId);
    }
}
