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
public class RegistSubscriptionCounselRequest {
    // counsel_history 부분
    private String counselCategoryName;
    private String detail;
    private String followUp;
    private String followUpNecessary;
    private String churn;
    private String churnReason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime consultDate;
    private int employeeId;
    private int reservationChannelId;
    private BigInteger customerId;
    private String customerType;
    private String htmlCode;
}
