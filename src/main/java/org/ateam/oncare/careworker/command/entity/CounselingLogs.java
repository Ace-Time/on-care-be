// 상담 일지
package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "counseling_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounselingLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counseling_id")
    private Long counselingId;

    @Column(name = "counseling_date", nullable = false)
    private LocalDate counselingDate;

    @Column(name = "counseling_type", nullable = false, length = 50)
    private String counselingType;

    @Column(name = "satisfaction", nullable = false, length = 20)
    private String satisfaction;

    @Column(name = "visit_purpose", nullable = false)
    private String visitPurpose;

    @Column(name = "attendees", nullable = false, length = 100)
    private String attendees;

    @Lob
    @Column(name = "discussion_content", nullable = false)
    private String discussionContent;

    @Lob
    @Column(name = "agreement_content", nullable = false)
    private String agreementContent;

    @Column(name = "next_visit_date")
    private LocalDate nextVisitDate;

    @Column(name = "counselor_sign_url", nullable = false)
    private String counselorSignUrl;

    @Column(name = "guardian_sign_url", nullable = false)
    private String guardianSignUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "care_worker_id", nullable = false)
    private Long careworkerId;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft = false;  // 임시저장 여부
}