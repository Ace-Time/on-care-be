package org.ateam.oncare.employee.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.RequestAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseAuthorityDTO;
import org.ateam.oncare.employee.command.service.EmployeeService;
import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;
import org.ateam.oncare.employee.command.service.EmployeeCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeCommandService employeeCommandService;

    @PutMapping("/authority")
    public ResponseEntity<ResponseAuthorityDTO> updateAuthority(@RequestBody RequestAuthorityDTO requestAuthorityDTO) {
        ResponseAuthorityDTO response = employeeService.updateAuthority(requestAuthorityDTO);
        return ResponseEntity.ok(response);
    }

    // 등록
    @PostMapping
    public ResponseEntity<String> register(@RequestBody EmployeeRequestDTO dto) {
        Integer id = employeeCommandService.registerEmployee(dto);
        return ResponseEntity.ok("직원 등록 완료. ID: " + id);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody EmployeeRequestDTO dto) {
        employeeCommandService.updateEmployee(id, dto);
        return ResponseEntity.ok("직원 수정 완료. ID: " + id);
    }
}