package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CreateTodoRequest;
import org.ateam.oncare.careworker.command.dto.UpdateTodoRequest;
import org.ateam.oncare.careworker.command.mapper.TodoCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoCommandService {

    private final TodoCommandMapper todoCommandMapper;

    @Transactional
    public Long createTodo(Long careWorkerId, CreateTodoRequest request) {
        int inserted = todoCommandMapper.insertTodo(careWorkerId, request);

        if (inserted == 0) {
            throw new IllegalStateException("할 일 등록에 실패했습니다.");
        }

        return request.getId();  // 생성된 ID 반환
    }

    @Transactional
    public void completeTodo(Long todoId) {
        log.info("할 일 완료 처리 시작 - todoId: {}", todoId);
        int updated = todoCommandMapper.updateTodoComplete(todoId);
        log.info("업데이트된 행 수: {}", updated);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 할 일을 찾을 수 없습니다. todoId: " + todoId);
        }

        log.info("할 일 완료 처리 완료 - todoId: {}", todoId);
    }

    @Transactional
    public void uncompleteTodo(Long todoId) {
        log.info("할 일 완료 취소 시작 - todoId: {}", todoId);
        int updated = todoCommandMapper.updateTodoUncomplete(todoId);
        log.info("업데이트된 행 수: {}", updated);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 할 일을 찾을 수 없습니다. todoId: " + todoId);
        }

        log.info("할 일 완료 취소 완료 - todoId: {}", todoId);
    }

    @Transactional
    public void updateTodo(Long todoId, UpdateTodoRequest request) {
        log.info("할 일 수정 시작 - todoId: {}", todoId);
        int updated = todoCommandMapper.updateTodo(todoId, request);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 할 일을 찾을 수 없습니다. todoId: " + todoId);
        }

        log.info("할 일 수정 완료 - todoId: {}", todoId);
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        int deleted = todoCommandMapper.deleteTodo(todoId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 할 일을 찾을 수 없습니다.");
        }
    }
}
