package org.ateam.oncare;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_care")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiCare {

    @Id
    @Column(name = "ai_id")
    private String aiId;

    @Column(name = "ai_content", nullable = false)
    private String aiContent;

    @Column(name = "ai_month")
    private LocalDate aiMonth;

    @Column(name = "ai_create_at")
    private LocalDateTime aiCreateAt;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId; // FK mapping target
}