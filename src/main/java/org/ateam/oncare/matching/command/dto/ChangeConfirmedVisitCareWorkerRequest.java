package org.ateam.oncare.matching.command.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeConfirmedVisitCareWorkerRequest {
    private Long careWorkerId;
}