package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateTodoRequest;
import org.ateam.oncare.careworker.command.dto.UpdateTodoRequest;
import org.ateam.oncare.careworker.command.service.TodoCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoCommandController {

    private final TodoCommandService todoCommandService;

    // 1. 할 일 등록
    @PostMapping
    public ApiResponse<Long> createTodo(
            @AuthenticationPrincipal EmployeeImpl employee,
            @RequestBody CreateTodoRequest request) {
        Long todoId = todoCommandService.createTodo(employee.getId(), request);
        return ApiResponse.success(todoId);
    }

    // 2. 할 일 수정
    @PatchMapping("/{todoId}")
    public ApiResponse<Void> updateTodo(
            @AuthenticationPrincipal EmployeeImpl employee,
            @PathVariable Long todoId,
            @RequestBody UpdateTodoRequest request) {
        todoCommandService.updateTodo(employee.getId(), todoId, request);
        return ApiResponse.success(null);
    }

    // 3. 할 일 완료 체크
    @PatchMapping("/{todoId}/complete")
    public ApiResponse<Void> completeTodo(
            @AuthenticationPrincipal EmployeeImpl employee,
            @PathVariable Long todoId) {
        todoCommandService.completeTodo(employee.getId(), todoId);
        return ApiResponse.success(null);
    }

    // 4. 할 일 완료 취소
    @PatchMapping("/{todoId}/uncomplete")
    public ApiResponse<Void> uncompleteTodo(
            @AuthenticationPrincipal EmployeeImpl employee,
            @PathVariable Long todoId) {
        todoCommandService.uncompleteTodo(employee.getId(), todoId);
        return ApiResponse.success(null);
    }

    // 5. 할 일 삭제
    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodo(
            @AuthenticationPrincipal EmployeeImpl employee,
            @PathVariable Long todoId) {
        todoCommandService.deleteTodo(employee.getId(), todoId);
        return ApiResponse.success(null);
    }
}
