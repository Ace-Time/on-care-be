package org.ateam.oncare.payment.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ElectronicPaymentCreate {
    private Integer employeeId; // 기안자 ID
    private Integer categoryId; // 결재 유형 ID
    private List<Integer> approverIds; // 결재선에 포함된 결재자 ID 목록 (순서대로)
}
