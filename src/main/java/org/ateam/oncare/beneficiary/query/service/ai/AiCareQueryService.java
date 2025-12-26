package org.ateam.oncare.beneficiary.query.service.ai;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.AiCareSummaryResponse;
import org.ateam.oncare.beneficiary.query.mapper.AiCareQueryMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiCareQueryService {

    private final AiCareQueryMapper mapper;

    public AiCareSummaryResponse getLatestMonthlySummary(Long beneficiaryId, String month) {
        return mapper.selectLatestAiSummary(beneficiaryId, month);
    }
}