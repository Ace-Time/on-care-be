package org.ateam.oncare.careproduct.command.dto;

import lombok.*;
import org.ateam.oncare.careproduct.command.enums.ProductHistoryStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseProductHistroyDTO {
    private long id;
    private String productId;
    private String productName;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProductHistoryStatus status;
    private String content;
    private int employeeId;
    private String employeeName;
    private long beneficiaryId;
    private String beneficiaryName;
    private LocalDateTime createAt;
}
