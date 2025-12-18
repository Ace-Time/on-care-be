package org.ateam.oncare.employee.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.query.dto.VisitScheduleVO;
import org.ateam.oncare.employee.query.service.EmployeeQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class EmployeeSchedulesQueryController {

    private final EmployeeQueryService employeeQueryService;

    // 직원(요양보호사)별 일정 조회
    // GET /api/schedules/employees/{id}
    @GetMapping("/employees/beneficiary/{employeeId}")
    public List<VisitScheduleVO> getEmployeeSchedules(@PathVariable Integer employeeId) {
        return employeeQueryService.getEmployeeSchedules(employeeId);
    }
}
