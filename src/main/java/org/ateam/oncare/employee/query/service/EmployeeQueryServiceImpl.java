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
        // 1. 직원 기본 상세 정보 조회
        EmployeeDetailDTO detail = employeeMapper.selectEmployeeDetail(id);

        if (detail != null) {
            // 2. 경력 리스트 조회 및 세팅
            detail.setCareers(employeeMapper.selectEmployeeCareers(id));

            // ★ [여기가 빠져있는지 확인하세요!] ★
            // 자격증 리스트를 조회해서 DTO에 넣어줘야 합니다.
            detail.setCertificates(employeeMapper.selectEmployeeCertificates(id));

            // 서비스 유형 (방문요양, 방문목욕 등)
            detail.setServiceTypes(employeeMapper.selectServiceTypes(id));

            // 보수 교육 이력
            detail.setEducations(employeeMapper.selectEducations(id));
        }
        return detail;


    }
}