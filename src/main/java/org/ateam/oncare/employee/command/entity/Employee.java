package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;
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
    private String email;

    @Column(name = "phone", nullable = false, length = 14)
    private String phone;

    @Column(name = "emergency_number", length = 14)
    private String emergencyNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "dept_code")
    private Long deptCode;

    @Column(name = "job_code", nullable = false)
    private Long jobCode;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

}