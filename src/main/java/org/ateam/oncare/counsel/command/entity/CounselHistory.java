package org.ateam.oncare.counsel.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "counsel_history")
public class CounselHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "consult_date")
    private LocalDateTime consultDate;

    @Column(name = "summary", length = 2000)
    private String summary;

    @Column(name = "detail", nullable = false, length = 2000)
    private String detail;

    @Column(name = "guardian_st")
    private Integer guardianSt; // 0: 본인, 1: 보호자

    @Column(name = "follow_up", length = 2000)
    private String followUp;

    @Column(name = "follow_up_necessary", nullable = false, length = 1)
    private String followUpNecessary; // "Y" or "N"

    // --- 외래키 ID 직접 매핑 (연관 관계 제거) ---

    // 예약 경로 ID (Common 도메인)
    @Column(name = "m_reservation_channel_id")
    private Integer reservationChannelId;

    // 상담 카테고리 ID (Common 도메인)
    @Column(name = "counsel_category_id", nullable = false)
    private Integer counselCategoryId;

    // 잠재 고객 ID (Nullable)
    @Column(name = "potential_id")
    private Long potentialId;

    // 수급자 ID (Nullable)
    @Column(name = "beneficiary_id")
    private Long beneficiaryId;
}