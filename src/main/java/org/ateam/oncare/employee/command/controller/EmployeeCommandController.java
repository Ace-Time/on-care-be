package org.ateam.oncare.employee.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;
import org.ateam.oncare.employee.command.service.EmployeeCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeCommandController {

    private final EmployeeCommandService employeeCommandService;

    /**
     * 직원 등록
     * [POST] /api/employees
     */
    @PostMapping
    public ResponseEntity<String> registerEmployee(@RequestBody EmployeeRequestDTO dto) {
        Long createdId = employeeCommandService.registerEmployee(dto);
        return ResponseEntity.ok("직원 등록이 완료되었습니다. (ID: " + createdId + ")");
    }

    /**
     * 직원 정보 수정
     * [PUT] /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto) {
        employeeCommandService.updateEmployee(id, dto);
        return ResponseEntity.ok("직원 정보가 수정되었습니다. (ID: " + id + ")");
    }
}