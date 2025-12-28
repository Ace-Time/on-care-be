package org.ateam.oncare.beneficiary.query.mapper;

// AI요약 DB에서 조회(백엔드)

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.AiCareSummaryResponse;

@Mapper
public interface AiCareQueryMapper {

    // ✅ 특정 수급자+월의 "가장 최신" AI요약 1건
    AiCareSummaryResponse selectLatestAiSummary(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("month") String month // YYYY-MM
    );
}