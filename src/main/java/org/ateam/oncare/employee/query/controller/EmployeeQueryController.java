package org.ateam.oncare.employee.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.query.dto.EmployeeDetailDTO;
import org.ateam.oncare.employee.query.dto.EmployeeListDTO;
import org.ateam.oncare.employee.query.dto.EmployeeSearchCondition;
import org.ateam.oncare.employee.query.service.EmployeeQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeQueryController {

    private final EmployeeQueryService employeeQueryService;

    /**
     * 직원 목록 조회 (검색 및 필터)
     * GET /api/employees?keyword=김미영&jobCode=1
     */
    @GetMapping
    public ResponseEntity<?> getEmployeeList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String jobCode,
            @RequestParam(required = false) String statusId,
            @RequestParam(required = false) String certificateId,
            @RequestParam(required = false) String serviceTypeId) {
        try {
            Long parsedJobCode = parseLong(jobCode);
            Long parsedStatusId = parseLong(statusId);
            Long parsedCertificateId = parseLong(certificateId);
            Long parsedServiceTypeId = parseLong(serviceTypeId);

            EmployeeSearchCondition condition = new EmployeeSearchCondition(keyword, parsedJobCode, parsedStatusId,
                    parsedCertificateId, parsedServiceTypeId);
            List<EmployeeListDTO> list = employeeQueryService.getEmployeeList(condition);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에도 남김
            return ResponseEntity.status(500).body("Error: " + e.getMessage() + "\n" + e.toString());
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            // 파라미터가 숫자가 아닌 경우(예: '관리자') null 처리하여 필터링 무시
            return null;
        }
    }

    /**
     * 직원 상세 조회
     * GET /api/employees/{id}
     */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<EmployeeDetailDTO> getEmployeeDetail(@PathVariable Long id) {
        EmployeeDetailDTO detail = employeeQueryService.getEmployeeDetail(id);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    /**
     * 교육 일정 임박/초과 직원 조회
     * GET /api/employees/education/alerts
     */
    @GetMapping("/education/alerts")
    public ResponseEntity<List<org.ateam.oncare.employee.query.dto.EducationAlertDTO>> getEducationAlerts() {
        return ResponseEntity.ok(employeeQueryService.getEducationAlerts());
    }

    /**
     * 필터 옵션 메타데이터 조회 (직군, 자격증 등)
     * GET /api/employees/filters
     */
    @GetMapping("/filters")
    public ResponseEntity<org.ateam.oncare.employee.query.dto.EmployeeFilterResponseDTO> getEmployeeFilters() {
        return ResponseEntity.ok(employeeQueryService.getEmployeeFilters());
    }

    /**
     * 현재 재직 중인 직원 수 조회
     * GET /api/employees/count/active
     */
    @GetMapping("/count/active")
    public ResponseEntity<Integer> countActiveEmployees() {
        return ResponseEntity.ok(employeeQueryService.countActiveEmployees());
    }

    /**
     * 현재 휴직 중인 직원 수 조회
     * GET /api/employees/count/on-leave
     */
    @GetMapping("/count/on-leave")
    public ResponseEntity<Integer> countOnLeaveEmployees() {
        return ResponseEntity.ok(employeeQueryService.countOnLeaveEmployees());
    }
}