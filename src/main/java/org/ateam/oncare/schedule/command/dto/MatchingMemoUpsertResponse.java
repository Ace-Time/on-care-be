package org.ateam.oncare.schedule.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchingMemoUpsertResponse {
    private Long matchingMemoId;
    private boolean created; // true면 신규 생성, false면 수정
}