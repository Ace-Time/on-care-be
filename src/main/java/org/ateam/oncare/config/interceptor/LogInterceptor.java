package org.ateam.oncare.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class LogInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    // 요처 ~ 응답까지 지정 시간 이상 시간이 소요 될 경우 log를 남기기위한 객체
    private static final Logger perfLog = LoggerFactory.getLogger("PERFORMANCE_WARN");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = request.getRemoteAddr();

        // 요청 시작 시간 저장 요청 처리시간 측정을 위함
        request.setAttribute("startTime", System.currentTimeMillis());

        // 1. 고유 ID 생성 (UUID 앞 8자리만 써도 충분히 식별 가능)
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        // 2. MDC에 저장 (Key 이름: "traceId")
        MDC.put("traceId", traceId);

        // 3. 시간 측정 시작
        request.setAttribute("startTime", System.currentTimeMillis());

        // (선택) 요청 시작 로그 남기기
        logger.info("[START] {} | Request incoming",clientIp);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 요청 끝난 시간 계산
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // 응답 결과, 응답시간을 남기기 위한 포맷
        // 로그 포맷: [Status] [소요시간ms] [HTTP Method] [URI] [client 접속 경로] [client 정보]
        String message = String.format("Status: %d | %d[ms] | %s | uri: %s | Referer : %s | User-Agent : %s",
                response.getStatus(),
                executionTime,
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader("Referer"),   // 우리 사이트까지 오기까지의 client 경로
                request.getHeader("User-Agent") // 클라이언트 접속 브라우저 정보
        );

        // 1초 이상 응답이 지연 될 경우 별도로 로그파일에 기록
        if (executionTime >= 1000) {
            perfLog.warn(message);
            logger.warn( "[SLOW] " + message);
        } else {
            logger.info("[END] " + message ); // 끝났음을 명시
        }

        // ★ 중요: 쓰레드 풀을 쓰기 때문에 다 쓴 뒤에는 반드시 지워야 함!
        // 안 지우면 다음 요청이 이 쓰레드를 재사용할 때 이전 ID가 남아있을 수 있음.
        MDC.clear();
    }

} 