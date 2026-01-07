package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CareLogInfo;
import org.ateam.oncare.careworker.command.dto.CreateCareLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCareLogRequest;
import org.ateam.oncare.careworker.command.mapper.CareLogCommandMapper;
import org.ateam.oncare.config.customexception.DuplicateCareLogException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CareLogCommandService {

    private final CareLogCommandMapper careLogCommandMapper;
    private final AiSummaryAsyncService aiSummaryAsyncService;

    @Transactional
    public void createCareLog(Long employeeId, CreateCareLogRequest request) {
        if (request.getStartTime() != null && request.getEndTime() != null
                && request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        // vs_id가 없으면 DB에서 조회
        if (request.getVsId() == null) {
            Long resolvedVsId = careLogCommandMapper.selectVsIdByBeneficiaryAndDate(
                    request.getBeneficiaryId(), employeeId, request.getServiceDate());
            if (resolvedVsId != null) {
                request.setVsId(resolvedVsId);
            }
        }

        // 근무 일정(vs_id)에 대한 중복 체크
        if (request.getVsId() != null) {
            int existingCount = careLogCommandMapper.countCareLogsByVsId(request.getVsId());
            if (existingCount > 0) {
                throw new DuplicateCareLogException("해당 근무 일정에 이미 요양일지가 등록되어 있습니다. 한 근무 일정에는 하나의 요양일지만 작성할 수 있습니다.");
            }
        }

        log.info("요양일지 작성 시작 - employeeId: {}, beneficiaryId: {}", employeeId, request.getBeneficiaryId());
        int inserted = careLogCommandMapper.insertCareLog(employeeId, request);

        if (inserted == 0) {
            throw new IllegalStateException("요양일지 작성에 실패했습니다.");
        }

        log.info("요양일지 작성 완료");

        // AI 요약 생성 (비동기 처리)
        aiSummaryAsyncService.generateAiSummaryAsync(request.getBeneficiaryId(), request.getServiceDate());
    }

    @Transactional
    public void updateCareLog(Long logId, UpdateCareLogRequest request) {
        if (request.getStartTime() != null && request.getEndTime() != null
                && request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        log.info("요양일지 수정 시작 - logId: {}", logId);

        // 수정 전 요양일지 정보 조회 (beneficiaryId, serviceDate)
        CareLogInfo careLogInfo = careLogCommandMapper.selectCareLogInfo(logId);
        if (careLogInfo == null) {
            throw new IllegalArgumentException("해당 요양일지를 찾을 수 없습니다. logId: " + logId);
        }

        int updated = careLogCommandMapper.updateCareLog(logId, request);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 요양일지를 찾을 수 없습니다. logId: " + logId);
        }

        log.info("요양일지 수정 완료 - logId: {}", logId);

        // AI 요약 생성 (비동기 처리, serviceDate가 수정되었으면 새 날짜, 아니면 기존 날짜 사용)
        LocalDate targetDate = request.getServiceDate() != null
                ? request.getServiceDate()
                : careLogInfo.getServiceDate();
        aiSummaryAsyncService.generateAiSummaryAsync(careLogInfo.getBeneficiaryId(), targetDate);
    }

    @Transactional
    public void deleteCareLog(Long logId) {
        log.info("요양일지 삭제 시작 - logId: {}", logId);

        // 삭제 전 요양일지 정보 조회 (beneficiaryId, serviceDate)
        CareLogInfo careLogInfo = careLogCommandMapper.selectCareLogInfo(logId);
        if (careLogInfo == null) {
            throw new IllegalArgumentException("해당 요양일지를 찾을 수 없습니다. logId: " + logId);
        }

        int deleted = careLogCommandMapper.deleteCareLog(logId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 요양일지를 찾을 수 없습니다. logId: " + logId);
        }

        log.info("요양일지 삭제 완료 - logId: {}", logId);

        // AI 요약 생성 (비동기 처리)
        aiSummaryAsyncService.generateAiSummaryAsync(careLogInfo.getBeneficiaryId(), careLogInfo.getServiceDate());
    }
}