// 기초평가 템플릿

package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "eval_templates", uniqueConstraints = {
        @UniqueConstraint(name = "uk_eval_templates_type_version", columnNames = {"eval_type", "version"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvalTemplates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "eval_type", nullable = false, length = 20)
    private String evalType;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "template_json", columnDefinition = "json", nullable = false)
    private String templateJson;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}