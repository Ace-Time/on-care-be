package org.ateam.oncare.employee.command.service;

import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.RequestAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO;

import java.util.Map;

public interface EmployeeService {
    ResponseLoginEmployeeDTO getEmployee(RequestLogin loginRequest);

    Map<Long, String> getAuthorityMasters();

    ResponseAuthorityDTO updateAuthority(RequestAuthorityDTO requestAuthorityDTO);

    void registerEmployee(org.ateam.oncare.employee.command.dto.EmployeeRegisterDto dto);
}
