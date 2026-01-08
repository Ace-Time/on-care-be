package org.ateam.oncare.config;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) //CorsConfigurationSource 설정을 찾아 적용(CORS 설정)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 사용하지 않음 JWT인증
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //security 인증전 filter를 가로채 JWT 인증 절차 진행

        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/**").permitAll() // 개발 기간동안 모든 요청 허용
//                .requestMatchers("/health", "/auth/**", "/employee/**").permitAll() // health, 인증 관련 요청만 인증 없이 허용
                .requestMatchers("/api/health", "/api/auth/**").permitAll() // health, 인증, API 요청 허용
                .requestMatchers("/api/actuator/**").permitAll() // 모니터링을 위한 endpoint
                .anyRequest().authenticated());

        return http.build();
    }

    // Bean을 추가하면 시큐리티가 자동으로 이 설정 적용(CROS 설정)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 특정 클라이언트만 허용 (예: 프론트엔드 주소)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:8080",
                "https://healthsystem.shop",
                "http://healthsystem.shop"    ));

        // 허용할 HTTP 메소드 (GET, POST 등)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 허용할 헤더 (Authorization 등) 모든 헤더 허용
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 자격 증명 허용 (쿠키, Authorization 헤더 등을 포함한 요청 허용 시 필수)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로(/**)에 대해 위 설정을 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
