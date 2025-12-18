package org.ateam.oncare.counsel.command.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistGeneralCounselRequest {
    private Integer counselCategoryId;
    private String summary;
    private String detail;
    private int guardianSt; // 본인이면 0, 보호자면 1
    private String followUp;
    private String followUpNecessary;
    private String churn;
    private String churnReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consultDate;
    private int employeeId;  // 상담사는 9~13번
    private int reservationChannelId;
    private BigInteger customerId;
    private String customerType;
}
