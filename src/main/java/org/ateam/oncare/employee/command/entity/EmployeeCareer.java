package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_career")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCareer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "work_period", nullable = false, length = 50)
    private String workPeriod;

    @Column(name = "task", length = 255)
    private String task;

}