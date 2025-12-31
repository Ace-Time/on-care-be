package org.ateam.oncare.counsel.command.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.ateam.oncare.counsel.command.entity.CounselHistory;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeneralCounselResponse {
    private BigInteger counselHistoryId;
    private Integer counselCategoryId;
    private String detail;
    private String summary;
    private int guardianSt;
    private String followUp;
    private String followUpNecessary;
    private String churn;
    private String churnReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consultDate;
    private int reservationChannelId;
    private BigInteger potentialId;
    private BigInteger beneficiaryId;

    public static GeneralCounselResponse from(CounselHistory entity) {
        return GeneralCounselResponse.builder()
                .counselHistoryId(BigInteger.valueOf(entity.getId())) // 저장된 ID 매핑
                .consultDate(entity.getConsultDate())
                .summary(entity.getSummary())
                .detail(entity.getDetail())
                .guardianSt(entity.getGuardianSt() != null ? entity.getGuardianSt() : 0)
                .followUp(entity.getFollowUp())
                .followUpNecessary(entity.getFollowUpNecessary())
                .churn(entity.getChurn())
                .churnReason(entity.getChurnReason())
                .reservationChannelId(entity.getReservationChannelId() != null ? entity.getReservationChannelId() : 0)
                .counselCategoryId(entity.getCounselCategoryId())

                // 수급자/잠재고객 ID 매핑 (null 체크 포함)
                .beneficiaryId(entity.getBeneficiaryId() != null ? BigInteger.valueOf(entity.getBeneficiaryId()) : null)
                .potentialId(entity.getPotentialId() != null ? BigInteger.valueOf(entity.getPotentialId()) : null)
                .build();
    }
}
