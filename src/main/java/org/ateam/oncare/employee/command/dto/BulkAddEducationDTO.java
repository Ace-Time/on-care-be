package org.ateam.oncare.employee.command.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkAddEducationDTO {
    private List<Long> careWorkerCertIds; // 선택된 직원들의 보유 자격증 ID 목록
    private AddEducationDTO educationInfo; // 공통 교육 정보
}
