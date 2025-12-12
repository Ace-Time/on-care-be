package org.ateam.oncare.employee.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

//    private final EmployeeRepository
//    private final JPAQueryFactory jpaQueryFactory

    @Override
    public ResponseLoginEmployeeDTO loginGetEmployee(RequestLogin loginRequest) {


        return null;
    }
}
