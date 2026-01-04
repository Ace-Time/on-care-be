package org.ateam.oncare.employee.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareerDTO {
    private String companyName;
    private String workPeriod;
    private String task;
}
