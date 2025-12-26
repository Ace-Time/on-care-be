package org.ateam.oncare.beneficiary.query.dto.response;

// AI요약 DB에서 조회(백엔드)

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AiCareSummaryResponse {
    private Long aiId;
    private Long beneficiaryId;
    private String month;              // ai_month
    private String summaryText;        // ai_content

    private Long lastLogId;            // ai_last_log_id
    private Long logsCount;            // ai_logs_count
    private String lastServiceDate;    // ai_last_service_date (YYYY-MM-DD)
    private String createdAt;          // ai_create_at (YYYY-MM-DD HH:mm:ss)

    private Long inputTokens;          // 요청 토큰
    private Long outputTokens;         // 응답 토큰
    private Long totalTokens;          // 요청 + 응답 총 토큰

}