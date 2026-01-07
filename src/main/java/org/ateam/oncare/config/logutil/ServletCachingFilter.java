package org.ateam.oncare.config.logutil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public class ServletCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. 요청과 응답을 캐싱 래퍼로 감쌈
        // (이미 감싸져 있다면 이중으로 감싸지 않도록 체크하는 것이 좋음)
        HttpServletRequest wrappingRequest = request;
        HttpServletResponse wrappingResponse = response;

        if (!(request instanceof ContentCachingRequestWrapper)) {
            wrappingRequest = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            wrappingResponse = new ContentCachingResponseWrapper(response);
        }

        // 2. 다음 필터(Security 등) 및 컨트롤러 실행
        filterChain.doFilter(wrappingRequest, wrappingResponse);

        // 3. 응답 본문을 다시 클라이언트로 복사
        // 이걸 안 하면 클라이언트는 빈 응답을 받게 됨
        if (wrappingResponse instanceof ContentCachingResponseWrapper) {
            ((ContentCachingResponseWrapper) wrappingResponse).copyBodyToResponse();
        }
    }
}