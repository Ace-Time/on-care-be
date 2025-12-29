package org.ateam.oncare.employee.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.employee.command.dto.AssignedBeneficiaryDTO;
import org.ateam.oncare.employee.command.repository.AssignedBeneficiaryRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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