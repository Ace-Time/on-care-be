package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CreateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.dto.UpdateBasicEvaluationRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BasicEvaluationCommandMapper {
        // 기초평가 작성
        int insertBasicEvaluation(
                        @Param("careWorkerId") Long careWorkerId,
                        @Param("request") CreateBasicEvaluationRequest request);

        // 기초평가 수정
        int updateBasicEvaluation(
                        @Param("evalId") Long evalId,
                        @Param("request") UpdateBasicEvaluationRequest request);

        // 기초평가 삭제
        int deleteBasicEvaluation(@Param("evalId") Long evalId);

        // 최신 템플릿 ID 조회
        Long findLatestTemplateIdByEvalType(@Param("evalType") String evalType);
}
