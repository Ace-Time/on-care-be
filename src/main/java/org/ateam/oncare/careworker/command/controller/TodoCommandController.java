package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateTodoRequest;
import org.ateam.oncare.careworker.command.dto.UpdateTodoRequest;
import org.ateam.oncare.careworker.command.service.TodoCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoCommandController {

    private final TodoCommandService todoCommandService;

    // 테스트용: 로그인한 유저 대신 ID를 1로 고정합니다.
    private final Long TEST_CAREGIVER_ID = 1L;

    // 1. 할 일 등록
    @PostMapping
    public ApiResponse<Long> createTodo(@RequestBody CreateTodoRequest request) {
        Long todoId = todoCommandService.createTodo(TEST_CAREGIVER_ID, request);
        return ApiResponse.success(todoId);
    }

    // 2. 할 일 수정
    @PatchMapping("/{todoId}")
    public ApiResponse<Void> updateTodo(
            @PathVariable Long todoId,
            @RequestBody UpdateTodoRequest request) {
        todoCommandService.updateTodo(todoId, request);
        return ApiResponse.success(null);
    }

    // 3. 할 일 완료 체크
    @PatchMapping("/{todoId}/complete")
    public ApiResponse<Void> completeTodo(@PathVariable Long todoId) {
        todoCommandService.completeTodo(todoId);
        return ApiResponse.success(null);
    }

    // 4. 할 일 완료 취소
    @PatchMapping("/{todoId}/uncomplete")
    public ApiResponse<Void> uncompleteTodo(@PathVariable Long todoId) {
        todoCommandService.uncompleteTodo(todoId);
        return ApiResponse.success(null);
    }

    // 5. 할 일 삭제
    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodo(@PathVariable Long todoId) {
        todoCommandService.deleteTodo(todoId);
        return ApiResponse.success(null);
    }
}
