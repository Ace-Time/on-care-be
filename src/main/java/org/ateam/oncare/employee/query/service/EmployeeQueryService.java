package org.ateam.oncare.employee.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.query.dto.EmployeeDetailDTO;
import org.ateam.oncare.employee.query.dto.EmployeeListDTO;
import org.ateam.oncare.employee.query.dto.EmployeeSearchCondition;
import org.ateam.oncare.employee.query.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 전용 트랜잭션
public class EmployeeQueryService {

    private final EmployeeMapper employeeMapper;

    // 목록 조회
    public List<EmployeeListDTO> getEmployeeList(EmployeeSearchCondition condition) {
        return employeeMapper.selectEmployeeList(condition);
    }

    // 상세 조회
    public EmployeeDetailDTO getEmployeeDetail(Long id) {
        // 1. 기본 정보 조회
        EmployeeDetailDTO detail = employeeMapper.selectEmployeeDetail(id);

        if (detail != null) {
            // 2. 경력 리스트 조회 및 세팅
            List<EmployeeDetailDTO.CareerDTO> careers = employeeMapper.selectEmployeeCareers(id);
            detail.setCareers(careers);
        }

        return detail;
    }
}