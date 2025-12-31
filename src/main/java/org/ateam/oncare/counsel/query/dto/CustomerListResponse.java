package org.ateam.oncare.counsel.query.dto;

import lombok.*;
import org.ateam.oncare.counsel.command.dto.StageData;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

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
    private String customerType;

    private List<StageData> stages;
}
