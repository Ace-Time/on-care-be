package org.ateam.oncare.employee.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ServiceTypeDTO {
    private Long id;    // 서비스 타입 ID
    private String name; // 서비스 명 (예: 방문요양)
}
