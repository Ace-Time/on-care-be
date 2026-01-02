package org.ateam.oncare.counsel.command.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionResponse implements CounselHistoryResponse {
    private BigInteger counselHistoryId;
    private int counselCategoryId;
    private String detail;
    private String summary;
    private String followUp;
    private String followUpNecessary;
    private String churn;
    private String churnReason;
    private int counselorId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime consultDate;
    private int reservationChannelId;
    private BigInteger beneficiaryId;
    private BigInteger potentialId;

    // 가입 상담 관리 내용
    private Map<Integer,StageData> stageData;
}
