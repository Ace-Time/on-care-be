package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CreateTodoRequest;
import org.ateam.oncare.careworker.command.dto.UpdateTodoRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TodoCommandMapper {
    // 할 일 등록
    int insertTodo(
            @Param("careWorkerId") Long careWorkerId,
            @Param("request") CreateTodoRequest request);

    // 할 일 완료 체크
    int updateTodoComplete(
            @Param("employeeId") Long employeeId,
            @Param("todoId") Long todoId);

    // 할 일 완료 취소
    int updateTodoUncomplete(
            @Param("employeeId") Long employeeId,
            @Param("todoId") Long todoId);

    // 할 일 수정
    int updateTodo(
            @Param("employeeId") Long employeeId,
            @Param("todoId") Long todoId,
            @Param("request") UpdateTodoRequest request);

    // 할 일 삭제
    int deleteTodo(
            @Param("employeeId") Long employeeId,
            @Param("todoId") Long todoId);
}
