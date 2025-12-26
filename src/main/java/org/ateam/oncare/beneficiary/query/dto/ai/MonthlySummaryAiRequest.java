package org.ateam.oncare.beneficiary.query.dto.ai;

// AI서버 요청 DTO (Spring->Fast API)

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlySummaryAiRequest {

    @JsonProperty("beneficiaryId")
    private Long beneficiaryId;

    @JsonProperty("month")
    private String month; // YYYY-MM

    @JsonProperty("logs")
    private List<DailyLogForSummary> logs = new ArrayList<>();

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyLogForSummary {

        @JsonProperty("date")
        private String date; // YYYY-MM-DD (service_date)

        @JsonProperty("physical_meal")
        private List<String> physicalMeal = new ArrayList<>();

        @JsonProperty("physical_excretion")
        private List<String> physicalExcretion = new ArrayList<>();

        @JsonProperty("physical_hygiene")
        private List<String> physicalHygiene = new ArrayList<>();

        @JsonProperty("physical_mobility")
        private List<String> physicalMobility = new ArrayList<>();

        @JsonProperty("cognitive")
        private List<String> cognitive = new ArrayList<>();

        @JsonProperty("status_health")
        private List<String> statusHealth = new ArrayList<>();

        @JsonProperty("status_mood")
        private List<String> statusMood = new ArrayList<>();

        @JsonProperty("status_sleep")
        private List<String> statusSleep = new ArrayList<>();

        @JsonProperty("special_note")
        private String specialNote; // 길면 잘라서
    }
}