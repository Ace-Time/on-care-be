package org.ateam.oncare.schedule.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "matching_memo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_matching_memo",
                        columnNames = {"matching_id", "memo_date"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_memo_id")   // 🔥 핵심
    private Long matchingMemoId;

    @Column(name = "matching_id", nullable = false)
    private Long matchingId;

    @Column(name = "memo_date", nullable = false)
    private LocalDate memoDate;

    @Column(name = "content", length = 2000)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}