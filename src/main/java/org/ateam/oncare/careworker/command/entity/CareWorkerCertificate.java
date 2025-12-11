// 요양보호사 보유 자격증
package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Table(name = "care_worker_certificate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert // insert 시 null인 필드 제외 (default 값 적용을 위해)
public class CareWorkerCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_worker_id", nullable = false)
    private CareWorker careWorker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    @Column(name = "license_no", nullable = false)
    private String licenseNo;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    /*
     * 상태 (0:대기, 1:반려, 2:승인)
     * DB Default가 0이므로 @DynamicInsert 혹은 @ColumnDefault 사용 권장
     */
    @Column(name = "status", nullable = false)
    @ColumnDefault("0")
    private Integer status;

    @Builder
    public CareWorkerCertificate(Long careWorkerId, Long certificateId, String licenseNo,
                                 LocalDate issueDate, LocalDate expireDate, Integer status) {
        this.licenseNo = licenseNo;
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.status = status;
    }
}