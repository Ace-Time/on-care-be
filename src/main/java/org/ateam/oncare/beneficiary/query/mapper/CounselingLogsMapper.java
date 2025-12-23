package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.beneficiary.query.dto.response.CounselingDetailResponse;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.CounselingListResponse;

import java.util.List;

@Mapper
public interface CounselingLogsMapper {

    List<CounselingListResponse.Item> selectCounselingList(@Param("beneficiaryId") Long beneficiaryId);

    CounselingDetailResponse selectCounselingDetail(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("counselingId") Long counselingId
    );
}