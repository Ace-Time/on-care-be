package org.ateam.oncare.auth.command.service;

import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.global.eventType.MasterDataEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;

public interface AuthService {
    ResponseToken login(RequestLogin loginRequest);

    Map<Long, String> getAuthorities();


    void updateAuthorityEvent(MasterDataEvent masterDataEvent);

    ResponseLoginEmployeeDTO mapStructTest(RequestLogin loginRequest);
}
