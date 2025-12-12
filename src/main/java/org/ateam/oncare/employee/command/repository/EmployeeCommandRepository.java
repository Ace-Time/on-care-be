package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.employee.command.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCommandRepository extends JpaRepository<Employee, Long> {
}
