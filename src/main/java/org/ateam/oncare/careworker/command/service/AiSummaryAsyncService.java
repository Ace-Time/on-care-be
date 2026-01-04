package org.ateam.oncare.careworker.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.query.service.ai.CareLogMonthlyAiSummaryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSummaryAsyncService {

    private final CareLogMonthlyAiSummaryService aiSummaryService;

    /**
     * AI 요약 생성 비동기 메서드
     * 실패하더라도 예외를 던지지 않고 로그만 남김
     */
    @Async("aiSummaryExecutor")
    public void generateAiSummaryAsync(Long beneficiaryId, LocalDate serviceDate) {
        try {
            // LocalDate를 YYYY-MM 형식으로 변환
            String month = serviceDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            log.info("AI 요약 생성 시작 (비동기) - beneficiaryId: {}, month: {}", beneficiaryId, month);

            aiSummaryService.generateMonthlySummary(beneficiaryId, month);

            log.info("AI 요약 생성 완료 (비동기) - beneficiaryId: {}, month: {}", beneficiaryId, month);
        } catch (Exception e) {
            // AI 요약 생성 실패 시 로그만 남기고 계속 진행
            log.error("AI 요약 생성 실패 (비동기) - beneficiaryId: {}, serviceDate: {}, error: {}",
                    beneficiaryId, serviceDate, e.getMessage(), e);
        }
    }
}
