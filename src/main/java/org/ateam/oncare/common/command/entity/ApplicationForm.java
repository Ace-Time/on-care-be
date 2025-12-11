package org.ateam.oncare.common.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "form_category_id", nullable = false)
    private Long formCategoryId;

}