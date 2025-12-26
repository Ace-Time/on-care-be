package org.ateam.oncare.auth.command.service;

import jakarta.servlet.ServletRequest;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.global.eventType.MasterDataEvent;
import org.jspecify.annotations.Nullable;
import org.springframework.context.event.EventListener;

import java.util.Map;

public interface AuthService {
    ResponseToken login(RequestLogin loginRequest, String clientIp);

    Map<Long, String> getAuthorities();


    void updateAuthorityEvent(MasterDataEvent masterDataEvent);

    ResponseLoginEmployeeDTO mapStructTest(RequestLogin loginRequest);

    @Nullable ResponseToken refreshToken(String refreshToken, ServletRequest request,String clientIp);
}
