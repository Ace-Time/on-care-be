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
    public ResponseEntity<List<EmployeeListDTO>> getEmployeeList(EmployeeSearchCondition condition) {
        List<EmployeeListDTO> list = employeeQueryService.getEmployeeList(condition);
        return ResponseEntity.ok(list);
    }

    /**
     * 직원 상세 조회
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailDTO> getEmployeeDetail(@PathVariable Long id) {
        EmployeeDetailDTO detail = employeeQueryService.getEmployeeDetail(id);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }
}