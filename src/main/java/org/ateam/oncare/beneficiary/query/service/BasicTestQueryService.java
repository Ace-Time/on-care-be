package org.ateam.oncare.beneficiary.query.service;

// 기초평가

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.*;
import org.ateam.oncare.beneficiary.query.mapper.BasicEvaluationQueryMapper;
import org.ateam.oncare.beneficiary.query.dto.response.ApiOptionalResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicTestQueryService {

    private final BasicEvaluationQueryMapper mapper;

    @Transactional(readOnly = true)
    public ApiOptionalResponse<FallEvaluationLatestResponse> getFallLatest(Long beneficiaryId) {
        FallEvaluationLatestResponse header = mapper.selectFallLatestHeader(beneficiaryId);
        if (header == null) {
            return ApiOptionalResponse.empty("낙상 평가 결과가 없습니다.");
        }

        List<FallEvaluationLatestResponse.AnswerItem> answers =
                mapper.selectFallAnswersByEvalId(header.getEvalId());
        header.setAnswers(answers);

        return ApiOptionalResponse.of(header);
    }

    @Transactional(readOnly = true)
    public ApiOptionalResponse<BedsoreEvaluationLatestResponse> getBedsoreLatest(Long beneficiaryId) {
        BedsoreEvaluationLatestResponse header = mapper.selectBedsoreLatestHeader(beneficiaryId);
        if (header == null) {
            return ApiOptionalResponse.empty("욕창 평가 결과가 없습니다.");
        }

        List<BedsoreEvaluationLatestResponse.AnswerItem> answers =
                mapper.selectBedsoreAnswersByEvalId(header.getEvalId());
        header.setAnswers(answers);

        return ApiOptionalResponse.of(header);
    }

    @Transactional(readOnly = true)
    public ApiOptionalResponse<CognitiveEvaluationLatestResponse> getCognitiveLatest(Long beneficiaryId) {
        CognitiveEvaluationLatestResponse result = mapper.selectCognitiveLatest(beneficiaryId);
        if (result == null) {
            return ApiOptionalResponse.empty("인지기능 평가 결과가 없습니다.");
        }
        return ApiOptionalResponse.of(result);
    }

    @Transactional(readOnly = true)
    public ApiOptionalResponse<NeedsEvaluationLatestResponse> getNeedsLatest(Long beneficiaryId) {
        NeedsEvaluationLatestResponse result = mapper.selectNeedsLatest(beneficiaryId);
        if (result == null) {
            return ApiOptionalResponse.empty("욕구사정 평가 결과가 없습니다.");
        }
        return ApiOptionalResponse.of(result);
    }
}
