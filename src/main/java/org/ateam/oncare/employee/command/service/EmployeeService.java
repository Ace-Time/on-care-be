package org.ateam.oncare.employee.command.service;

import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.RequestAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO;

import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO; // 추가

import java.util.Map;

public interface EmployeeService {
    ResponseLoginEmployeeDTO getEmployee(RequestLogin loginRequest);

    Map<Long, String> getAuthorityMasters();

    ResponseAuthorityDTO updateAuthority(RequestAuthorityDTO requestAuthorityDTO);

    // 통합된 등록/수정 메서드 (EmployeeRequestDTO 사용)
    Integer registerEmployee(EmployeeRequestDTO dto);

    void updateEmployee(Integer id, EmployeeRequestDTO dto);
}
