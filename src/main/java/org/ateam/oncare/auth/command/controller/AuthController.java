package org.ateam.oncare.auth.command.controller;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseLoginDTO;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.auth.command.service.AuthService;
import org.ateam.oncare.auth.security.CookieUtil;
import org.ateam.oncare.global.customannotation.annotation.ClientIp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@RequestBody RequestLogin loginRequest, @ClientIp String clientIp) {
        ResponseToken tokenResponse =  authService.login(loginRequest, clientIp);

        ResponseLoginDTO reponseBody = new ResponseLoginDTO(
                tokenResponse.getAccessToken(),
                tokenResponse.getTokenType());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenResponse.getCookie().toString())
                .body(reponseBody);
    }


    @GetMapping("/authorities")
    public ResponseEntity<Map<Long,String>> getAuthorities() {

        Map<Long,String> authorities =  authService.getAuthorities();
        return ResponseEntity.ok(authorities);
    }


    @PostMapping("/refresh")
    public ResponseEntity<ResponseToken> refreshToken(
                                @CookieValue(name = CookieUtil.REFRESH_COOKIE, required = false) String refreshToken,
                                ServletRequest request) {
        log.debug("리프래시 컨트롤러");
        log.debug("리프래시 요청 refreshToken:{}",refreshToken);
        return ResponseEntity.ok(authService.refreshToken(refreshToken,request));
    }

    /**
     * modelMapper와 mapStruct 차이 비교를 위한 테스트 코드 추 후 삭제 예정
     * @param loginRequest
     * @return
     */
    @GetMapping("/mapStruct")
    public ResponseEntity<ResponseLoginEmployeeDTO> mapStructTest(@RequestBody RequestLogin loginRequest) {

        ResponseLoginEmployeeDTO responseDTO =  authService.mapStructTest(loginRequest);
        return ResponseEntity.ok(responseDTO);
    }

}
