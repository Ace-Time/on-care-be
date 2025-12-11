package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dept_name")
    private String deptName;

}