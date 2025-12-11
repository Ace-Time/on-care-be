package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert // insert 시 status 기본값(0) 적용을 위해 사용
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK: CareWorkerCertificate ID (Raw ID 매핑)
    // 연관 관계 매핑(@ManyToOne)을 원하시면 아래 'Tip'을 참고해주세요.
    @Column(name = "care_worker_certificate_id", nullable = false)
    private Long careWorkerCertificateId;

    @Column(name = "edu_name", nullable = false)
    private String eduName;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "edu_date", nullable = false)
    private LocalDate eduDate;

    @Column(name = "next_edu_date", nullable = false)
    private LocalDate nextEduDate;

    // Boolean 타입 매핑 (TINYINT(1) <-> boolean)
    @Column(name = "is_overdue", nullable = false)
    private Boolean isOverdue;

    /**
     * 상태 (0:대기, 1:반려, 2:승인)
     */
    @Column(name = "status", nullable = false)
    @ColumnDefault("0")
    private Integer status;

    @Builder
    public Education(Long careWorkerCertificateId, String eduName, String institution,
                     LocalDate eduDate, LocalDate nextEduDate, Boolean isOverdue, Integer status) {
        this.careWorkerCertificateId = careWorkerCertificateId;
        this.eduName = eduName;
        this.institution = institution;
        this.eduDate = eduDate;
        this.nextEduDate = nextEduDate;
        this.isOverdue = isOverdue;
        this.status = status;
    }
}