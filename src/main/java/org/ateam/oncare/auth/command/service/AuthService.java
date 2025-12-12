package org.ateam.oncare.auth.command.service;

import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseToken;

public interface AuthService {
    ResponseToken login(RequestLogin loginRequest);
}
