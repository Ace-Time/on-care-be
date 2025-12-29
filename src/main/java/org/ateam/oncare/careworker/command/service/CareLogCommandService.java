package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CareLogInfo;
import org.ateam.oncare.careworker.command.dto.CreateCareLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCareLogRequest;
import org.ateam.oncare.careworker.command.mapper.CareLogCommandMapper;
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
        int deleted = careLogCommandMapper.deleteCareLog(logId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 요양일지를 찾을 수 없습니다. logId: " + logId);
        }

        log.info("요양일지 삭제 완료 - logId: {}", logId);
    }
}
