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
@Transactional(readOnly = true) // 조회 전용 최적화
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeListDTO> getEmployeeList(EmployeeSearchCondition condition) {
        return employeeMapper.selectEmployeeList(condition);
    }

    @Override
    public EmployeeDetailDTO getEmployeeDetail(Long id) {
        // 1. 기본 정보 조회
        EmployeeDetailDTO detail = employeeMapper.selectEmployeeDetail(id);

        // 직원이 존재할 경우에만 경력 정보 추가 조회
        if (detail != null) {
            // 2. 경력 정보 조회 (MyBatis Mapper 호출)
            List<EmployeeDetailDTO.CareerDTO> careers = employeeMapper.selectEmployeeCareers(id);
            // 3. DTO에 경력 리스트 주입
            detail.setCareers(careers);
        }

        return detail;
    }
}