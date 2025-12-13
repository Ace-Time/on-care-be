package org.ateam.oncare.employee.command.service;

import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO;

import java.util.Map;

public interface EmployeeService {
    ResponseLoginEmployeeDTO loginGetEmployee(RequestLogin loginRequest);

    Map<Long, String> getAuthorityMasters();
}
