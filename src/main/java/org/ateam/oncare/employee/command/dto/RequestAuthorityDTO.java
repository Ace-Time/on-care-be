package org.ateam.oncare.employee.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestAuthorityDTO {
    private Long id;
    private String name;
}
