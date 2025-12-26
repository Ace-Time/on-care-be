package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ai_care")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "ai_last_log_id", nullable = false)
    private Long aiLastLogId;

    @Column(name = "ai_logs_count", nullable = false)
    private Long aiLogsCount;

    @Column(name = "ai_last_service_date", nullable = false)
    private Date aiLastServiceDate;

    @Column(name = "ai_input_tokens", nullable = false)
    private Long aiInputTokens;

    @Column(name = "ai_output_tokens", nullable = false)
    private Long aiOutputTokens;

    @Column(name = "ai_total_tokens", nullable = false)
    private Long aiTotalTokens;
}