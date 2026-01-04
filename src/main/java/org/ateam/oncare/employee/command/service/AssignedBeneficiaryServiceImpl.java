package org.ateam.oncare.employee.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.AssignedBeneficiaryDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignedBeneficiaryServiceImpl implements AssignedBeneficiaryService {

        private final org.ateam.oncare.employee.query.mapper.EmployeeMapper employeeMapper;

        @Override
        public List<AssignedBeneficiaryDTO> getAssignedBeneficiaries(Long careWorkerId) {
                // MyBatis Mapper를 사용하여 담당 수급자 목록을 한 번에 조회합니다.
                // XML query: selectAssignedBeneficiaries
                return employeeMapper.selectAssignedBeneficiaries(careWorkerId);
        }
}