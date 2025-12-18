package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CreateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.dto.UpdateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.mapper.BasicEvaluationCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicEvaluationCommandService {

    private final BasicEvaluationCommandMapper basicEvaluationCommandMapper;

    @Transactional
    public void createBasicEvaluation(Long careWorkerId, CreateBasicEvaluationRequest request) {
        log.info("기초평가 작성 시작 - evalType: {}, beneficiaryId: {}", request.getEvalType(), request.getBeneficiaryId());
        int inserted = basicEvaluationCommandMapper.insertBasicEvaluation(careWorkerId, request);

        if (inserted == 0) {
            throw new IllegalStateException("기초평가 작성에 실패했습니다.");
        }

        log.info("기초평가 작성 완료");
    }

    @Transactional
    public void updateBasicEvaluation(Long evalId, UpdateBasicEvaluationRequest request) {
        log.info("기초평가 수정 시작 - evalId: {}", evalId);
        int updated = basicEvaluationCommandMapper.updateBasicEvaluation(evalId, request);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 기초평가를 찾을 수 없습니다. evalId: " + evalId);
        }

        log.info("기초평가 수정 완료 - evalId: {}", evalId);
    }

    @Transactional
    public void deleteBasicEvaluation(Long evalId) {
        log.info("기초평가 삭제 시작 - evalId: {}", evalId);
        int deleted = basicEvaluationCommandMapper.deleteBasicEvaluation(evalId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 기초평가를 찾을 수 없습니다. evalId: " + evalId);
        }

        log.info("기초평가 삭제 완료 - evalId: {}", evalId);
    }
}
