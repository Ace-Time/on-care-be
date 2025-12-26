package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodoRequest {
    private Long id;  // 생성된 ID (MyBatis가 자동으로 설정)
    private String todo;
    private LocalDate todoDate;
    private String type;
    private Long beneficiaryId;
}
