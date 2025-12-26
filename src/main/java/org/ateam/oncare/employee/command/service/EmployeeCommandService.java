package org.ateam.oncare.employee.command.service;

import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;

public interface EmployeeCommandService {

    /**
     * 직원 등록
     * 
     * @param dto 등록할 직원 정보
     * @return 등록된 직원의 ID
     */
    Integer registerEmployee(EmployeeRequestDTO dto);

    /**
     * 직원 정보 수정
     * 
     * @param id  수정할 직원의 ID
     * @param dto 수정할 정보
     */
    void updateEmployee(Integer id, EmployeeRequestDTO dto);
}