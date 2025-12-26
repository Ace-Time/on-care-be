package org.ateam.oncare.config.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

@RestControllerAdvice // 1. 모든 컨트롤러의 예외를 감시하는 "중앙 통제실" 선언
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 2. IO 작업 등 특정 예외를 잡고 싶을 때
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        // ★ 여기서 로그를 한 번만 남기면 됩니다. (MDC traceId도 자동으로 같이 찍힘)
        log.error("IO 작업 중 오류 발생: ", e.getMessage());

        // 클라이언트에게 줄 응답 설정
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("파일 처리 중 오류가 발생했습니다.");
    }

    /**
     * 3. 그 외 예상치 못한 모든 예외(Exception)를 잡을 때 (Safety Net)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {
        log.error("알 수 없는 시스템 오류 발생: ", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류입니다. 관리자에게 문의하세요.");
    }

    /**
     * 4. [추가] 존재하지 않는 리소스(404) 요청은 에러 로그를 남기지 않음
     * (브라우저의 자동 요청이나 잘못된 URL 호출 시 시스템 에러로 오해하지 않기 위함)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handle404(NoResourceFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("잘못된 주소입니다.");
    }

    /**
     * 5. [비즈니스 규칙 위반] 상태상 허용되지 않는 요청
     * 예: 일정 겹침, 진행/완료 일정 수정/삭제 시도 등
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        // 비즈니스 로직에서 의도적으로 막은 경우이므로 error 로그는 남기지 않음
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(e.getMessage());       // 서비스에서 던진 메시지를 그대로 전달
    }

    /**
     * 6. [잘못된 요청] 파라미터 누락, 값 오류 등
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        // 클라이언트 요청 문제이므로 warn 수준 로그
        log.warn("잘못된 요청: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(e.getMessage());
    }
}
