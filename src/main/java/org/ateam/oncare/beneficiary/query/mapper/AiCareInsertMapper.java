package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiCareInsertMapper {

    /**
     * ai_care 테이블에 "이력"으로 INSERT만 한다.
     */
    int insertAiCare(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("aiMonth") String aiMonth,                 // YYYY-MM
            @Param("aiContent") String aiContent,             // 요약 텍스트
            @Param("aiLastLogId") Long aiLastLogId,           // 마지막 반영 log_id (max)
            @Param("aiLogsCount") Long aiLogsCount,           // 반영된 로그 개수
            @Param("aiLastServiceDate") String aiLastServiceDate, // YYYY-MM-DD

            // 토큰 추가
            @Param("aiInputTokens") Long aiInputTokens,
            @Param("aiOutputTokens") Long aiOutputTokens,
            @Param("aiTotalTokens") Long aiTotalTokens
    );
}