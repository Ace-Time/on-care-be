package org.ateam.oncare.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
//@Slf4j
public class PerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger("repoPerformance");

    // 'Repository'로 끝나는 모든 빈의 메서드 지정
    @Around("execution(* *..*Repository.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = joinPoint.proceed(); // 실제 메서드 실행

        stopWatch.stop();

        long totalTime = stopWatch.getTotalTimeMillis();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // 300ms(0.3초) 이상 걸리는 것만 로그 찍기
        if (totalTime > 300) {
            log.warn("⏱️ [Slow Query] {}.{} took {} ms", className, methodName, totalTime);
        } else {
            log.info("✅ {}.{} took {} ms", className, methodName, totalTime);
        }


        return proceed;
    }
}