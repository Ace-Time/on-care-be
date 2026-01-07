package org.ateam.oncare.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ateam.oncare.config.logutil.LogMaskingUtils;
import org.ateam.oncare.global.util.ClientIpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class LogInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    // 요처 ~ 응답까지 지정 시간 이상 시간이 소요 될 경우 log를 남기기위한 객체
    private static final Logger perfLog = LoggerFactory.getLogger("PERFORMANCE_WARN");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = ClientIpUtils.getClientIp(request);

        // 고유 ID 생성 (UUID 앞 8자리만 써도 충분히 식별 가능)
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        // MDC에 저장 (Key 이름: "traceId") Thread 전용 저장소(쓰레드 유지되는동안 유지됨)
        MDC.put("traceId", traceId);

        // 시간 측정 시작
        request.setAttribute("startTime", System.currentTimeMillis());

        // 요청 시작 로그 남기기
        logger.info("[START] endPoint : [{}] {} | clientIP: {} ",request.getMethod() ,request.getRequestURI(),clientIp);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 1. 소요 시간 계산
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // 2. Client IP 추출 (ALB 고려)
        String clientIP = ClientIpUtils.getClientIp(request);
        int status = response.getStatus();

        // 3. 로그 메시지 빌더 생성
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(String.format("endPoint : [%s] %s | clientIP: %s | %d[ms] | Status: %d | Referer : %s | User-Agent : %s",
                request.getMethod(),
                request.getRequestURI(),
                clientIP,
                executionTime,
                status,
                request.getHeader("Referer"),   // 우리 사이트까지 오기까지의 client 경로
                request.getHeader("User-Agent") // 클라이언트 접속 브라우저 정보
        ));

        // 4. Request Body 읽기 (Wrapper인지 확인)
        ContentCachingRequestWrapper cachingRequest =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (cachingRequest != null) {
            try {
                byte[] content = cachingRequest.getContentAsByteArray();
                if (content.length > 0) {
                    String body = new String(content, StandardCharsets.UTF_8);
                    // 마스킹 처리 후 로그 추가
                    logBuilder.append("\nReqBody: ").append(LogMaskingUtils.maskingSensitiveData(body));
                }

                // Query String(파라미터)이 있다면 추가
                String queryString = request.getQueryString();
                if (queryString != null && !queryString.isEmpty()) {
                    logBuilder.append("\nQueryParams: ").append(queryString);
                }
            } catch (Exception e) {
                logBuilder.append("\nReqBody: [Read Error]");
            }
        }

        // 5. Response Body 읽기
        ContentCachingResponseWrapper cachingResponse =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (cachingResponse != null) {
            try {
                byte[] content = cachingResponse.getContentAsByteArray();
                if (content.length > 0) {
                    String body = new String(content, StandardCharsets.UTF_8);

                    // 민감정보 마스킹 수행
                    String maskedBody = LogMaskingUtils.maskingSensitiveData(body);

                    // 1000자 제한 로직 적용
                    if (maskedBody.length() > 1000) {
                        maskedBody = maskedBody.substring(0, 1000) + "... [Truncated]";
                    }

                    // 응답은 너무 길면 1000자만 자르는 등의 로직 추가 가능
                    logBuilder.append("\nResBody: ").append(LogMaskingUtils.maskingSensitiveData(maskedBody));
                }
            } catch (Exception e) {
                logBuilder.append("\nResBody: [Read Error]");
            }
        }

        // 6. 로깅 수행 (에러/성공 분기)
        if (status >= 400 || ex != null) {
            logger.error("[ERROR] " + logBuilder.toString(), ex);
        } else if (executionTime > 1000) {
            logger.warn("[SLOW] " + logBuilder.toString());
        } else {
            logger.info("[ END ] " + logBuilder.toString());
        }

        MDC.clear();
    }


} 