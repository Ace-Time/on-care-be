package org.ateam.oncare.payment.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentActionRequest {
    private String action; // "APPROVE" (승인) or "REJECT" (반려)
    private String comment; // 승인/반려 의견
}
