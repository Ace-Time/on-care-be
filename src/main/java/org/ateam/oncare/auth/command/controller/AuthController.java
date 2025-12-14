package org.ateam.oncare.auth.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.auth.command.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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


    @GetMapping("/authorities")
    public ResponseEntity<Map<Long,String>> getAuthorities() {

        Map<Long,String> authorities =  authService.getAuthorities();
        return ResponseEntity.ok(authorities);
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
