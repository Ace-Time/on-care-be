package org.ateam.oncare.employee.command.service;

import org.ateam.oncare.employee.command.dto.AssignedBeneficiaryDTO;

import java.util.List;

public interface AssignedBeneficiaryService {

    /**
     * 특정 요양보호사에게 배정된(매칭된) 수급자 목록을 조회합니다.
     * 
     * @param careWorkerId 요양보호사 ID
     * @return 배정된 수급자 정보 목록
     */
    List<AssignedBeneficiaryDTO> getAssignedBeneficiaries(Long careWorkerId);
}