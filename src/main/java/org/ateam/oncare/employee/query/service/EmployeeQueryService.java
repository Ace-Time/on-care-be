package org.ateam.oncare.employee.query.service;

import org.ateam.oncare.employee.query.dto.EmployeeDetailDTO;
import org.ateam.oncare.employee.query.dto.EmployeeListDTO;
import org.ateam.oncare.employee.query.dto.EmployeeSearchCondition;
import java.util.List;

public interface EmployeeQueryService {

    /**
     * 직원 목록 조회 (검색 조건 포함)
     * @param condition 검색 조건 (키워드, 직군, 상태)
     * @return 직원 목록 리스트
     */
    List<EmployeeListDTO> getEmployeeList(EmployeeSearchCondition condition);

    /**
     * 직원 상세 조회 (기본 정보 + 경력 정보 포함)
     * @param id 직원 ID
     * @return 직원 상세 정보 DTO
     */
    EmployeeDetailDTO getEmployeeDetail(Long id);
}