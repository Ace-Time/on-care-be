package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDetailDto {

    // 할 일 기본 정보
    private Long todoId;
    private String content;
    private LocalDate todoDate;
    private String type;
    private Boolean isCompleted;
    private LocalDateTime createdAt;

    // 관련 수급자 정보
    private Long beneficiaryId;
    private String beneficiaryName;
    private String beneficiaryAddress;
    private String beneficiaryPhone;
    private String careLevel;
}
