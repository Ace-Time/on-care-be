package org.ateam.oncare.employee.command.repository;

import com.querydsl.core.Tuple;

import java.util.List;

public interface EmployeeRepositoryCustom {
    List<Tuple> findLoginDataByUsername(String email);
}
