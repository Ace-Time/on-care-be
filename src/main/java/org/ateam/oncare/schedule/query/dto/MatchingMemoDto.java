package org.ateam.oncare.schedule.query.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingMemoDto {
    private Long matchingMemoId;
    private Long matchingId;
    private String content;
    private LocalDate memoDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}