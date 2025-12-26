package org.ateam.oncare.employee.query.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EmployeeFilterResponseDTO {
    private List<FilterOptionDTO> jobs;
    private List<FilterOptionDTO> certificates;
    private List<FilterOptionDTO> serviceTypes;
}
