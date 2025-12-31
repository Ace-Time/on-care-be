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
public class GeneralCounsel implements CounselHistoryRequired {
    // 신규 고객이면 반드시 잠재 고객 등록
    private String name;
    private String phone;

    // 상담 이력 등록
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consultDate;
    private String summary;
    private String detail;
    private int guardianSt; // 본인이면 0, 보호자면 1
    private String followUp;
    private String followUpNecessary;
    private String churn;
    private String churnReason;
    private int reservationChannelId;
    private BigInteger customerId;
    private String customerType;
    private int counselCategoryId; // (1: 가입, 2: 렌탈, 3: 문의, 4: 컴플레인, 5: 해지)
    private int employeeId;
}
