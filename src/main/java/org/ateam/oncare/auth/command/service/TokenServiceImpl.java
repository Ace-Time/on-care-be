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
import org.ateam.oncare.auth.security.CookieUtil;
import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;

    @Override
    @Transactional
    public ResponseToken generateToken(String clientIp, Authentication authentication, String name) {
        EmployeeImpl employee = (EmployeeImpl)authentication.getPrincipal();

        // 리프레시 jti 생성(리프레시 토큰 식별 id)
        String jti = UUID.randomUUID().toString();

        //AT 생성
        String atToken = jwtTokenProvider.generateAccessToken(authentication);
        //RT 생성
        JwtTokenProvider.RefreshTokenInfo refreshTokenInfo = jwtTokenProvider.generateRefreshToken(name,jti);

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

        ResponseToken responseToken = new ResponseToken(atToken, cookie,"Bearer");

        return responseToken;
    }

    @Override
    public ResponseToken verifyByRefreshToken(String refreshToken, ServletRequest request) {
        /* 설명. refresh token 전용 검증 */
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromRT(refreshToken);
        Claims claims = jwtTokenProvider.getClaimsFromRT(refreshToken);
        RefreshToken e = refreshTokenRepository.findByJti(claims.getId());
        log.debug("rt : {}" , e);

        if (e.getRevoked() == 1) {
            log.warn("RT 실패: revoked 토큰. jti={}", e.getJti());
            throw new RuntimeException("Token revoked");
        }     // 이미 폐기된 토큰 → 재사용 시도

//        if (!e.getEmployeeId().equals(userId)){
//            throw new RuntimeException("User mismatch"); // 사용자 불일치
//        }

        if (e.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            log.warn("RT 실패: 만료된 RT. jti={}", e.getJti());
            throw new RuntimeException("Expired"); // 만료
        }

//        if (!sha256(refreshRaw).equals(e.getTokenHash()))                      // 원문 해시 비교(위조/다른 원본)
//        {
//            throw new RuntimeException("Hash mismatch");
//        }
//
//        // (옵션) 디바이스 바인딩 체크: 발급 당시 deviceFp와 다르면 추가 인증/거부 정책 가능
//        if (e.getDeviceFp() != null && deviceFp != null && !e.getDeviceFp().equals(deviceFp)) {
//            log.warn("RT 실패: 디바이 정보 오류. getDeviceFp= {} , deviceFp = {}", e.getDeviceFp(), deviceFp);
//            throw new RuntimeException("Device fingerprint changed");
//        }
//
//        e.setLastUsedAt(LocalDateTime.now());                                // 사용 흔적 업데이트
//        return repo.save(e);


        return null;
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
