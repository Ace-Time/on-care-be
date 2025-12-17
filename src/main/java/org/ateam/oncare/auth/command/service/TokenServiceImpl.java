package org.ateam.oncare.auth.command.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.auth.command.entity.RefreshToken;
import org.ateam.oncare.auth.command.repository.RefreshTokenRepository;
import org.ateam.oncare.auth.query.dto.RefreshTokenOfEmployeeDTO;
import org.ateam.oncare.auth.query.service.AuthQueryService;
import org.ateam.oncare.auth.security.CookieUtil;
import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.ateam.oncare.employee.command.entity.QAuthority;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;
    private final AuthQueryService authQueryService;

    @Override
    @Transactional
    public ResponseToken generateToken(String clientIp, EmployeeImpl employee) {

        // 리프레시 jti 생성(리프레시 토큰 식별 id)
        String jti = UUID.randomUUID().toString();

        //AT 생성
        String atToken = jwtTokenProvider.generateAccessToken(employee);
        //RT 생성
        JwtTokenProvider.RefreshTokenInfo refreshTokenInfo =
                jwtTokenProvider.generateRefreshToken(employee.getEmail(),jti);

        RefreshToken refreshTokenEntity =
                this.saveRefreshToken(clientIp, employee.getId(), refreshTokenInfo, jti,"");

        log.debug("atToken:{}",atToken);
        log.debug("refreshTokenInfo:{},{}",refreshTokenInfo.token(),refreshTokenInfo.expiredAt());
        log.debug("LocalDateTime:{}", LocalDateTime.now());
        log.debug("refreshTokenEntity:{}", refreshTokenEntity);

        refreshTokenRepository.save(refreshTokenEntity);

        // 쿠키로 리프레시 전달
        ResponseCookie cookie =
                cookieUtil.createRefreshCookie(refreshTokenInfo.token());

        ResponseToken responseToken = new ResponseToken(atToken, cookie,"Bearer", employee.getId());

        return responseToken;
    }

    @Override
    public ResponseToken verifyByRefreshToken(String refreshToken, ServletRequest request , String clientIp) {
        /* 설명. refresh token 전용 검증 */
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Claims claims = jwtTokenProvider.getClaimsFromRT(refreshToken);
        String email = claims.getSubject();
        String jti = claims.getId();

        RefreshTokenOfEmployeeDTO rtOfEmployee = authQueryService.selectRefreshTokenOfEmployee(jti);

        log.debug("rt : {}" , rtOfEmployee);

        if (rtOfEmployee.getRevoked() == 1) {
            log.warn("RT 실패: revoked 토큰. jti={}, userId={}, useremail={}"
                    , rtOfEmployee.getJti(),rtOfEmployee.getEmployeeId(),rtOfEmployee.getEmployeeId());
            throw new RuntimeException("Token revoked");
        }     // 이미 폐기된 토큰 → 재사용 시도

        if (!rtOfEmployee.getEmail().equals(email)){
            // 토큰 조작의심되어 토큰 폐기
            refreshTokenRepository.revokeTorken(jti);
            log.warn("RT 실패: User mismatch. employeeId={}",rtOfEmployee.getEmployeeId());
            throw new RuntimeException("User mismatch"); // 사용자 불일치
        }

        if (rtOfEmployee.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            log.warn("RT 실패: 만료된 RT. jti={}", rtOfEmployee.getJti());
            throw new RuntimeException("Expired"); // 만료
        }

        if (!DigestUtils.sha256Hex(refreshToken).equals(rtOfEmployee.getTokenHash())) // 원문 해시 비교(위조/다른 원본)
        {
            // 토큰 조작의심되어 토큰 폐기
            refreshTokenRepository.revokeTorken(jti);
            log.warn("RT 실패: 토큰 조작 감지. jti={}, userId={}, useremail={}, token={}"
                , rtOfEmployee.getJti(),rtOfEmployee.getEmployeeId(),rtOfEmployee.getEmployeeId(), refreshToken);
            throw new RuntimeException("Hash mismatch");
        } // token 조작

        // 사용된 rt 폐기
        long count = refreshTokenRepository.revokeTorken(jti);

        List<SimpleGrantedAuthority> authorities = rtOfEmployee.getAuthorities().stream()
                .filter(Objects::nonNull) // null 방지
                .map(SimpleGrantedAuthority::new)// String -> GrantedAuthority 변환
                .collect(Collectors.toList());

        //새로운 토큰 생성
        EmployeeImpl employeeImpl = new EmployeeImpl(rtOfEmployee.getEmail(),"",authorities);

        employeeImpl.setDetails(
                rtOfEmployee.getEmployeeId(),
                rtOfEmployee.getEmail(),
                rtOfEmployee.getName(),
                rtOfEmployee.getPhone()
        );

        //새로운 RT, AT 생성
        ResponseToken responseToken = this.generateToken(clientIp, employeeImpl);
        log.debug("벌크 연산 결과 count : {}" , count);

        return responseToken;
    }


    private RefreshToken saveRefreshToken(
                                    String clientIp,
                                    Integer employeeId,
                                    JwtTokenProvider.RefreshTokenInfo refreshToken,
                                    String jti,
                                    String device) {
        LocalDateTime issuedAt = refreshToken.issuedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime expiresAt = refreshToken.expiredAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return RefreshToken.builder()
                            .employeeId(employeeId)
                            .ip(clientIp)
                            .tokenHash(DigestUtils.sha256Hex(refreshToken.token()))
                            .jti(jti)
                            .issuedAt(issuedAt)
                            .expiresAt(expiresAt)
                            .revoked(0)
                            .build();
    }

}
