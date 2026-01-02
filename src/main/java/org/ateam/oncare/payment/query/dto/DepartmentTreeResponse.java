package org.ateam.oncare.payment.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentTreeResponse {
    private Long deptId;
    private String deptName;
    private List<DepartmentTreeResponse> childrenDepts;
    private List<PaymentUserResponse> members;
}
