package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.BedsoreEvaluationLatestResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CognitiveEvaluationLatestResponse;
import org.ateam.oncare.beneficiary.query.dto.response.FallEvaluationLatestResponse;
import org.ateam.oncare.beneficiary.query.dto.response.NeedsEvaluationLatestResponse;

import java.util.List;

@Mapper
public interface BasicEvaluationQueryMapper {

    // FALL 최신 헤더 1건
    FallEvaluationLatestResponse selectFallLatestHeader(@Param("beneficiaryId") Long beneficiaryId);

    // FALL 답변 리스트 (evalId로)
    List<FallEvaluationLatestResponse.AnswerItem> selectFallAnswersByEvalId(@Param("evalId") Long evalId);

    // BEDSORE 최신 헤더 1건
    BedsoreEvaluationLatestResponse selectBedsoreLatestHeader(@Param("beneficiaryId") Long beneficiaryId);

    // BEDSORE 답변 리스트 (evalId로)
    List<BedsoreEvaluationLatestResponse.AnswerItem> selectBedsoreAnswersByEvalId(@Param("evalId") Long evalId);

    // COGNITIVE 최신 1건 (필요 필드만)
    CognitiveEvaluationLatestResponse selectCognitiveLatest(@Param("beneficiaryId") Long beneficiaryId);

    // NEEDS 최신 1건 (필요 필드만)
    NeedsEvaluationLatestResponse selectNeedsLatest(@Param("beneficiaryId") Long beneficiaryId);
}
