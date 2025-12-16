package org.ateam.oncare.schedule.command.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MatchingMemoUpsertRequest {

    @NotNull
    private Long matchingId;

    @NotNull
    private LocalDate memoDate;

    @Size(max = 2000)
    private String content;
}