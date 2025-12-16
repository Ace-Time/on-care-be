package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HomeTodoDto {
    private Long todoId;
    private String content;
    private boolean isCompleted;
}