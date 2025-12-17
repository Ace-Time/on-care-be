package org.ateam.oncare.auth.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey atKey;
    private final SecretKey rtKey;
    private final JwtConfig jwtConfig;

    public record RefreshTokenInfo(String token, Date expiredAt, Date issuedAt) {
    }

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.atKey = Keys.hmacShaKeyFor(jwtConfig.getAtSecret().getBytes(StandardCharsets.UTF_8));
        this.rtKey = Keys.hmacShaKeyFor(jwtConfig.getRtSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(EmployeeImpl employee) {

        // 인증 후 EmployeeUserDetailService에서 return한 EmployeeImpl Get
//        EmployeeImpl userDetails = (EmployeeImpl) authentication.getPrincipal();

        List<String> roles = employee.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtConfig.getAccessTokenValidity());
        Claims claims = Jwts.claims().setSubject(employee.getUsername());
        claims.put("auth", roles);
        claims.put("id", employee.getId());
        claims.put("username", employee.getName());
//        claims.put("email", userDetails.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(atKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public RefreshTokenInfo generateRefreshToken(String email, String jti) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshTokenValidity());

        String token = Jwts.builder()
                .setSubject(email)
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(rtKey, SignatureAlgorithm.HS512)
                .compact();
        return new RefreshTokenInfo(token, expiryDate, now);
    }

    public String getUsernameFromAT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(atKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getUsernameFromRT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(rtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public Claims getClaimsFromAT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(atKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }


    public Claims getClaimsFromRT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(rtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(atKey)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token - This is expected for refresh token validation");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(rtKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Refresh token has expired");
        } catch (JwtException e) {
            log.error("Invalid refresh token");
        }
        return false;
    }
}