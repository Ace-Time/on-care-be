package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CounselingDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CounselingListResponse;
import org.ateam.oncare.beneficiary.query.mapper.CounselingLogsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CounselingLogsQueryService {

    private final CounselingLogsMapper mapper;

    @Transactional(readOnly = true)
    public CounselingListResponse getCounselingList(Long beneficiaryId) {
        CounselingListResponse res = new CounselingListResponse();
        res.setBeneficiaryId(beneficiaryId);
        res.setItems(mapper.selectCounselingList(beneficiaryId));
        return res;
    }

    @Transactional(readOnly = true)
    public CounselingDetailResponse getCounselingDetail(Long beneficiaryId, Long counselingId) {
        return mapper.selectCounselingDetail(beneficiaryId, counselingId);
    }
}