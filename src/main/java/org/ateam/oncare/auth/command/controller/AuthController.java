package org.ateam.oncare.auth.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.auth.command.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<ResponseToken> login(@RequestBody RequestLogin loginRequest) {

        ResponseToken tokenResponse =  authService.login(loginRequest);
        return ResponseEntity.ok(null);
    }


}
