package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.BasicEvaluationDetailDto;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BasicEvaluationMapper {
    // 기초평가 목록 조회 (평가 유형별)
    List<BasicEvaluationListDto> selectBasicEvaluationListByType(
            @Param("caregiverId") Long caregiverId,
            @Param("evalType") String evalType,
            @Param("year") Integer year);

    // 기초평가 목록 조회 (수급자별 + 평가 유형별)
    List<BasicEvaluationListDto> selectBasicEvaluationListByBeneficiaryAndType(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("evalType") String evalType,
            @Param("year") Integer year);

    // 기초평가 상세 조회
    BasicEvaluationDetailDto selectBasicEvaluationDetail(@Param("evalId") Long evalId);

}
