package org.ateam.oncare.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CertificateMasterDTO {
    private Long id;
    private String certificateName;
    private String organization;
    private Integer eduCycleYears;
}
