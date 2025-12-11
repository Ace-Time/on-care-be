package org.ateam.oncare.employee.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;
import org.ateam.oncare.employee.command.service.EmployeeCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeCommandService employeeCommandService;

    // 등록
    @PostMapping
    public ResponseEntity<String> register(@RequestBody EmployeeRequestDTO dto) {
        Long id = employeeCommandService.registerEmployee(dto);
        return ResponseEntity.ok("직원 등록 완료. ID: " + id);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto) {
        employeeCommandService.updateEmployee(id, dto);
        return ResponseEntity.ok("직원 수정 완료. ID: " + id);
    }
}