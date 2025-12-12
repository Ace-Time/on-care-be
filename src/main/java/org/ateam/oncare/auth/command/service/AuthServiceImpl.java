package org.ateam.oncare.auth.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.employee.command.service.EmployeeService;
import org.ateam.oncare.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseToken login(RequestLogin loginRequest) {

        ResponseLoginEmployeeDTO employee = modelMapper.map(employeeService.loginGetEmployee(loginRequest), ResponseLoginEmployeeDTO.class);

        return null;
    }
}
