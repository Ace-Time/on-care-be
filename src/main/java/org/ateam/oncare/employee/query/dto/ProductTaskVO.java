package org.ateam.oncare.employee.query.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductTaskVO {
    private Long id; // 입출고 ID
    private Integer status; // 0: 회수, 1: 수령
    private LocalDate expectedDate; // 예정일 (DATE 타입)
    private String isConfirmed; // 확정 여부 (N, Y)
    private String productName; // 물품명 (product_id로 조인해서 가져옴)
    private String beneficiaryName; // 수급자명 (beneficiary_id로 조인해서 가져옴)
}
