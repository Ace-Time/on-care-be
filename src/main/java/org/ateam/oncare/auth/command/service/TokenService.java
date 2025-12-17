package org.ateam.oncare.auth.command.service;

import jakarta.servlet.ServletRequest;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.springframework.security.core.Authentication;

public interface TokenService {
    ResponseToken generateToken(String clientIp, EmployeeImpl employee);

    ResponseToken verifyByRefreshToken(String refreshToken, ServletRequest request , String clientIp);
}
