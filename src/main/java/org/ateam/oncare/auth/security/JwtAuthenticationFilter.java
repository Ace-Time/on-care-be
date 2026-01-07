package org.ateam.oncare.auth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && tokenProvider.validateAccessToken(jwt)) {
                Claims claims = tokenProvider.getClaimsFromAT(jwt);
                String username = claims.getSubject();

                // 추 후 Redis 적용시 Redis에서 토큰 유효성 검사 후 인증 예정 현재는 기능 중지
                // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                List<String> authoritiesStr = claims.get("auth", List.class);

                // 토큰에서 권환 추출
                Collection<? extends GrantedAuthority> authorities = authoritiesStr.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                EmployeeImpl principal = new EmployeeImpl(username, "", authorities);
                principal.setDetails(
                        claims.get("id", Long.class),
                        username,
                        claims.get("username", String.class),
                        null, // phone info not in token
                        claims.get("jobname", String.class));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
                        null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context: " +  ex.getMessage());
//            throw new ServletException(ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
