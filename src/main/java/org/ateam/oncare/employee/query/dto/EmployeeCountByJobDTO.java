package org.ateam.oncare.employee.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeCountByJobDTO {
    private String jobName;
    private int count;
}
