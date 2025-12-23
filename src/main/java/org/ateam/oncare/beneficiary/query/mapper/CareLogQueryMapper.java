package org.ateam.oncare.beneficiary.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.CareLogListResponse;

import java.util.List;

@Mapper
public interface CareLogQueryMapper {

    List<CareLogListResponse> selectCareLogList(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("month") String month // "YYYY-MM" (nullable)
    );

    CareLogDetailRow selectCareLogDetail(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("logId") Long logId
    );
}