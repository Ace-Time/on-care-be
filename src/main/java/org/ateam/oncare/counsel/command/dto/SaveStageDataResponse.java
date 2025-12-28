package org.ateam.oncare.counsel.command.dto;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaveStageDataResponse {
    private int stage;    // 1, 2, 3, 4
    private String processStatus;  // P or F
    private LocalDateTime processTime;
    private LocalDateTime month;
    private String htmlCode;
    private BigInteger potentialId;
}
