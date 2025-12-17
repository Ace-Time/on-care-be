package org.ateam.oncare.careworker.query.service;

import org.ateam.oncare.careworker.query.dto.CounselingLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CounselingLogListDto;
import org.ateam.oncare.careworker.query.mapper.CounselingLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CounselingLogQueryService {

    private final CounselingLogMapper counselingLogMapper;

    public List<CounselingLogListDto> getCounselingLogList(Long caregiverId) {
        return counselingLogMapper.selectCounselingLogList(caregiverId);
    }

    public List<CounselingLogListDto> getCounselingLogListByBeneficiary(Long beneficiaryId) {
        return counselingLogMapper.selectCounselingLogListByBeneficiary(beneficiaryId);
    }

    public CounselingLogDetailDto getCounselingLogDetail(Long counselingId) {
        return counselingLogMapper.selectCounselingLogDetail(counselingId);
    }
}
