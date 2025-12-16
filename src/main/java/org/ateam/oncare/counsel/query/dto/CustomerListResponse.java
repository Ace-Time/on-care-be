package org.ateam.oncare.counsel.query.dto;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerListResponse {
    private BigInteger customerId;
    private String name;
    private String phone;
    private String customerCategoryName;
    private LocalDateTime consultDate;
    private int consultCount;
    private String guardianName;
    private String guardianPhone;
}
