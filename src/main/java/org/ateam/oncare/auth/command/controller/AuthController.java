package org.ateam.oncare.auth.command.controller;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseTokenDTO;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.auth.command.service.AuthService;
import org.ateam.oncare.auth.security.CookieUtil;
import org.ateam.oncare.global.customannotation.annotation.ClientIp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseTokenDTO> login(@RequestBody RequestLogin loginRequest, @ClientIp String clientIp) {
        ResponseToken tokenResponse =  authService.login(loginRequest, clientIp);

        ResponseTokenDTO reponseBody = new ResponseTokenDTO(
                tokenResponse.getAccessToken(),
                tokenResponse.getTokenType());

        log.info("[로그인 인증] employeeId={}",tokenResponse.getEmployeeId());

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
    public ResponseEntity<ResponseTokenDTO> refreshToken(
                                @CookieValue(name = CookieUtil.REFRESH_COOKIE, required = false) String refreshToken,
                                ServletRequest request,
                                @ClientIp String clientIp) {
        log.debug("리프래시 컨트롤러");
        log.debug("리프래시 요청 refreshToken:{}",refreshToken);

        ResponseToken responseToken = authService.refreshToken(refreshToken,request,clientIp);

        ResponseTokenDTO reponseBody = new ResponseTokenDTO(
                responseToken.getAccessToken(),
                responseToken.getTokenType());

        log.info("[토근 재 발급] employeeId={}, old rt={}",responseToken.getEmployeeId(), refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseToken.getCookie().toString())
                .body(reponseBody);
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
