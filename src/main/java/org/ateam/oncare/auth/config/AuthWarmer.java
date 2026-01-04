package org.ateam.oncare.auth.config;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthWarmer implements CommandLineRunner {

    private final AuthService authService;


    // 프로세스 구동시 실행됨 (초기 셋팅이 필요 할 경우)
    @Override
    public void run(String... args) throws Exception {
        // 캐싱을 사용할 예정으로 변수로 반환 받지 않음
        authService.getAuthorities();
    }
}