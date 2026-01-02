package org.ateam.oncare.employee.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paper_type", length = 255)
    private String paperType;

    @Column(name = "mime_type", length = 255)
    private String mimeType;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "original_file_name", length = 255)
    private String originalFileName;

    @Column(name = "re_file_name", length = 255)
    private String reFileName;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "employee_form_category_id", nullable = false)
    private Long employeeFormCategoryId;

}