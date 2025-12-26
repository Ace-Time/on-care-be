package org.ateam.oncare.beneficiary.query.dto.response;

// 요양일지 일지 상세


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CareLogDetailResponse {

    private Long logId;
    private Long beneficiaryId;

    private String recordedAt;
    private String serviceDate;
    private String startTime;
    private String endTime;
    private String serviceType;

    private Integer careWorkerId;
    private String careWorkerName;

    private Physical physical;
    private List<String> cognitive;
    private Status status;

    private String specialNote;

    @Getter @Setter
    public static class Physical {
        private List<String> meal;
        private List<String> excretion;
        private List<String> hygiene;
        private List<String> mobility;
    }

    @Getter @Setter
    public static class Status {
        private List<String> health;
        private List<String> mood;
        private List<String> sleep;
        private List<String> etc;
    }
}