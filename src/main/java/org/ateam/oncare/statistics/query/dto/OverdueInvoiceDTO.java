package org.ateam.oncare.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueInvoiceDTO {
    private Long invoiceId;
    private String billingMonth;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Integer totalCount;
    private Integer ownCount;
    private String paymentStatus;
    private String isSent;
    private Long beneficiaryId;
    private String beneficiaryName;
    private String phone;
    private String address;
}
