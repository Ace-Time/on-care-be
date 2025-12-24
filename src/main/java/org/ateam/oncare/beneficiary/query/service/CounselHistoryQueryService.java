package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CounselHistoryDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CounselHistoryListResponse;
import org.ateam.oncare.beneficiary.query.mapper.CounselHistoryMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounselHistoryQueryService {

    private final CounselHistoryMapper counselHistoryMapper;

    public CounselHistoryListResponse getCounselHistoryList(Long beneficiaryId) {
        CounselHistoryListResponse res = new CounselHistoryListResponse();
        res.setBeneficiaryId(beneficiaryId);
        res.setItems(counselHistoryMapper.selectCounselHistoryList(beneficiaryId));
        return res;
    }

    public CounselHistoryDetailResponse getCounselHistoryDetail(Long beneficiaryId, Long counselHistoryId) {
        return counselHistoryMapper.selectCounselHistoryDetail(beneficiaryId, counselHistoryId);
    }
}