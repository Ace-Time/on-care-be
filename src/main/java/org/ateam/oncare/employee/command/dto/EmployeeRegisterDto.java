package org.ateam.oncare.employee.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRegisterDto {
    private Long id; // DB에서 생성된 ID를 받기 위함

    private String name;
    private String phone;
    private Long deptCode;
    private Long jobCode;
    private Long statusId;
    private LocalDate hireDate;
}
