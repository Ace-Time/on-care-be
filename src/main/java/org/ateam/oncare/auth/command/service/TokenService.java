package org.ateam.oncare.auth.command.service;

import jakarta.servlet.ServletRequest;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.springframework.security.core.Authentication;

public interface TokenService {
    ResponseToken generateToken(String clientIp, Authentication authentication, String name);

    ResponseToken verifyByRefreshToken(String refreshToken, ServletRequest request);
}
