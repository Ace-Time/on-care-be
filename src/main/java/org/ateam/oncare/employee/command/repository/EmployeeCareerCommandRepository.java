package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.employee.command.entity.EmployeeCareer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeCareerCommandRepository extends JpaRepository<EmployeeCareer, Long> {
    // 직원 수정 시 기존 경력을 싹 지우기 위해 필요
    void deleteAllByEmployeeId(Integer employeeId);
}
