package org.ateam.oncare.auth.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.employee.command.service.EmployeeService;
import org.ateam.oncare.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseToken login(RequestLogin loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUseremail(),
                        loginRequest.getPassword()
                )
        );

        log.debug("authentication:{}",authentication);

        ResponseLoginEmployeeDTO employee = modelMapper.map(employeeService.loginGetEmployee(loginRequest), ResponseLoginEmployeeDTO.class);

        return null;
    }

    @Override
    @Cacheable(value="masterData" , key="'m_authorities'")
    public Map<Long, String> getAuthorities() {
        Map<Long, String> getAuthorities = employeeService.getAuthorityMasters();
        log.debug("getAuthorities: getAuthorities={}", getAuthorities);
        return getAuthorities;
    }
}
