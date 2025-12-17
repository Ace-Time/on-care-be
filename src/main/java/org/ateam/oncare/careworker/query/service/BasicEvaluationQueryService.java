package org.ateam.oncare.careworker.query.service;

import org.ateam.oncare.careworker.query.dto.BasicEvaluationDetailDto;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationListDto;
import org.ateam.oncare.careworker.query.mapper.BasicEvaluationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicEvaluationQueryService {

    private final BasicEvaluationMapper basicEvaluationMapper;

    public List<BasicEvaluationListDto> getBasicEvaluationListByType(Long caregiverId, String evalType, Integer year) {
        return basicEvaluationMapper.selectBasicEvaluationListByType(caregiverId, evalType, year);
    }

    public List<BasicEvaluationListDto> getBasicEvaluationListByBeneficiaryAndType(Long beneficiaryId, String evalType, Integer year) {
        return basicEvaluationMapper.selectBasicEvaluationListByBeneficiaryAndType(beneficiaryId, evalType, year);
    }

    public BasicEvaluationDetailDto getBasicEvaluationDetail(Long evalId) {
        return basicEvaluationMapper.selectBasicEvaluationDetail(evalId);
    }

}
