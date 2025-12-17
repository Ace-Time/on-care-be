package org.ateam.oncare.employee.command.repository;

import com.querydsl.core.Tuple;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;

import java.util.List;

public interface EmployeeRepositoryCustom {
    List<Tuple> findLoginDataByUsername(String email);
}
