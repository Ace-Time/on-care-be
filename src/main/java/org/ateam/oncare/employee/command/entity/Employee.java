package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "pw", length = 255)
    private String pw;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "gender", nullable = false, length = 1)
    private String gender;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "email", length = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Column(name = "phone", nullable = false, length = 14)
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @Column(name = "emergency_number", length = 14)
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "비상연락처 형식이 올바르지 않습니다.")
    private String emergencyNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "dept_code")
    private Integer deptCode;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "geo_ready", nullable = false)
    private Boolean geoReady = false;
    // false: 아직 좌표 없음
    // true : 좌표 계산 완료


    @Column(name = "job_code", nullable = false)
    private Long jobCode;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

}