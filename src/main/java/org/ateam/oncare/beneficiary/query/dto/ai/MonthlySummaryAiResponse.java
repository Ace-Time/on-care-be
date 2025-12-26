package org.ateam.oncare.beneficiary.query.dto.ai;

// AI 서버 응답

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryAiResponse {

    @JsonProperty("beneficiaryId")
    private Long beneficiaryId;

    @JsonProperty("month")
    private String month;

    @JsonProperty("summaryText")
    private String summaryText;

    @JsonProperty("meta")
    private Map<String, Object> meta;
}