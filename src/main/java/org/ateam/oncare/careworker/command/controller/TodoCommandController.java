package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.command.dto.CreateTodoRequest;
import org.ateam.oncare.careworker.command.dto.UpdateTodoRequest;
import org.ateam.oncare.careworker.command.service.TodoCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoCommandController {

    private final TodoCommandService todoCommandService;
    private final JwtTokenProvider jwtTokenProvider;

    // JWT 토큰에서 사용자 ID 추출
    private Long getEmployeeIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromAT(token);
            return claims.get("id", Long.class);
        }
        return 1L; // fallback
    }

    // 1. 할 일 등록
    @PostMapping
    public ApiResponse<Long> createTodo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateTodoRequest request) {
        Long careWorkerId = getEmployeeIdFromToken(authHeader);
        Long todoId = todoCommandService.createTodo(careWorkerId, request);
        return ApiResponse.success(todoId);
    }

    // 2. 할 일 수정
    @PatchMapping("/{todoId}")
    public ApiResponse<Void> updateTodo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long todoId,
            @RequestBody UpdateTodoRequest request) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        todoCommandService.updateTodo(employeeId, todoId, request);
        return ApiResponse.success(null);
    }

    // 3. 할 일 완료 체크
    @PatchMapping("/{todoId}/complete")
    public ApiResponse<Void> completeTodo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long todoId) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        todoCommandService.completeTodo(employeeId, todoId);
        return ApiResponse.success(null);
    }

    // 4. 할 일 완료 취소
    @PatchMapping("/{todoId}/uncomplete")
    public ApiResponse<Void> uncompleteTodo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long todoId) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        todoCommandService.uncompleteTodo(employeeId, todoId);
        return ApiResponse.success(null);
    }

    // 5. 할 일 삭제
    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long todoId) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        todoCommandService.deleteTodo(employeeId, todoId);
        return ApiResponse.success(null);
    }
}
