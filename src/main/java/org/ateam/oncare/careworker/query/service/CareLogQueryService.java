package org.ateam.oncare.careworker.query.service;

import org.ateam.oncare.careworker.query.dto.CareLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CareLogListDto;
import org.ateam.oncare.careworker.query.mapper.CareLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareLogQueryService {

    private final CareLogMapper careLogMapper;

    public List<CareLogListDto> getCareLogList(Long employeeId) {
        return careLogMapper.selectCareLogList(employeeId);
    }

    public List<CareLogListDto> getCareLogListByBeneficiary(Long beneficiaryId) {
        return careLogMapper.selectCareLogListByBeneficiary(beneficiaryId);
    }

    public CareLogDetailDto getCareLogDetail(Long logId) {
        return careLogMapper.selectCareLogDetail(logId);
    }
}
