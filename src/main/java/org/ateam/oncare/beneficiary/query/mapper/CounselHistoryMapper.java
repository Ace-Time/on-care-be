package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.CounselHistoryDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CounselHistoryListResponse;

import java.util.List;

@Mapper
public interface CounselHistoryMapper {
    List<CounselHistoryListResponse.Item> selectCounselHistoryList(@Param("beneficiaryId") Long beneficiaryId);

    CounselHistoryDetailResponse selectCounselHistoryDetail(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("counselHistoryId") Long counselHistoryId
    );

}