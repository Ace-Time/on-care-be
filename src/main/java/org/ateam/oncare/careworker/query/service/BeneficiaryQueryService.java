package org.ateam.oncare.careworker.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.query.dto.MyBeneficiaryDto;
import org.ateam.oncare.careworker.query.mapper.BeneficiaryQueryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 요양보호사의 배정 수급자 조회 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BeneficiaryQueryService {

    private final BeneficiaryQueryMapper beneficiaryQueryMapper;

    /**
     * 로그인한 요양보호사에게 배정된 수급자 목록 조회
     * @param employeeId 요양보호사 직원 ID
     * @return 배정된 수급자 목록
     */
    public List<MyBeneficiaryDto> getAssignedBeneficiaries(Long employeeId) {
        return beneficiaryQueryMapper.selectAssignedBeneficiaries(employeeId);
    }

    /**
     * 로그인한 요양보호사에게 배정된 특정 수급자 상세 조회
     * @param employeeId 요양보호사 직원 ID
     * @param beneficiaryId 수급자 ID
     * @return 수급자 상세 정보
     */
    public MyBeneficiaryDto getAssignedBeneficiaryDetail(Long employeeId, Long beneficiaryId) {
        return beneficiaryQueryMapper.selectAssignedBeneficiaryDetail(employeeId, beneficiaryId);
    }
}
