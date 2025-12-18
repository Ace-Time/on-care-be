package org.ateam.oncare.employee.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.RequestAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseAuthorityDTO;
import org.ateam.oncare.employee.command.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PutMapping("/authority")
    public ResponseEntity<ResponseAuthorityDTO> updateAuthority(@RequestBody RequestAuthorityDTO requestAuthorityDTO) {
        ResponseAuthorityDTO response = employeeService.updateAuthority(requestAuthorityDTO);
        return ResponseEntity.ok(response);
    }
}
