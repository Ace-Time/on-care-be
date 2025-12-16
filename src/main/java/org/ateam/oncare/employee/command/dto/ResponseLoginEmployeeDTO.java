package org.ateam.oncare.employee.command.dto;



import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseLoginEmployeeDTO {
    private Long id;
    private String name;
    private LocalDate birth;
    private String gender;
    private Long deptCode;
    private Long jobCode;
    private Long statusId;
    private List<String> authorities;
}
