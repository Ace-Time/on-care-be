package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities_of_employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AuthoritiesOfEmployee.PK.class) // 복합키 식별자 클래스 지정
public class AuthoritiesOfEmployee {

    @Id // 복합키 구성요소 1
    @Column(name = "authority_code", nullable = false)
    private Long authorityCode;

    @Id // 복합키 구성요소 2
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    // 복합키 처리를 위한 내부 static 클래스 (별도 파일로 분리해도 됨)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        private Long authorityCode;
        private Integer employeeId;
    }

}