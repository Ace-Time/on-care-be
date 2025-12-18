package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.employee.command.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, EmployeeRepositoryCustom {
    Optional<Employee> findByEmail(String email);
}
