package org.ateam.oncare.employee.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.query.dto.ScheduleResDTO;
import org.ateam.oncare.employee.query.service.EmployeeQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules/employees") // URL 구조 변경: /api/schedules/employees/...
@RequiredArgsConstructor
public class EmployeeSchedulesQueryController {

    private final EmployeeQueryService employeeQueryService;

    // 직원(요양보호사)별 '방문요양' 일정 조회
    // GET /api/schedules/employees/{id}/visits
    @GetMapping("/{employeeId}/visits")
    public List<ScheduleResDTO> getVisitSchedules(@PathVariable Integer employeeId) {
        return employeeQueryService.getVisitSchedules(employeeId);
    }

    // 직원(요양보호사)별 '물품 렌탈/회수' 일정 조회
    // GET /api/schedules/employees/{id}/products
    @GetMapping("/{employeeId}/products")
    public List<ScheduleResDTO> getProductSchedules(@PathVariable Integer employeeId) {
        return employeeQueryService.getProductSchedules(employeeId);
    }
}
