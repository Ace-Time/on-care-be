package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_form_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFormCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

}