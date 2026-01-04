package org.ateam.oncare.counsel.command.dto;

import lombok.Data;

@Data
public class PotentialCustomerResponse {
    private Long customerId;  // 생성된 잠재고객 ID
    private String name;
    private String phone;
    private String customerType;  // "potential"
}
