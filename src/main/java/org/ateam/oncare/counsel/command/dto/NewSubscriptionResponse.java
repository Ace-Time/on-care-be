package org.ateam.oncare.counsel.command.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewSubscriptionResponse implements CounselHistoryResponse {
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
}
