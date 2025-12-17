package org.ateam.oncare.employee.command.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.ateam.oncare.employee.command.entity.QAuthoritiesOfEmployee;
import org.ateam.oncare.employee.command.entity.QAuthority;
import org.ateam.oncare.employee.command.entity.QEmployee;

import java.util.List;

@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QEmployee employee = QEmployee.employee;
    private final QAuthoritiesOfEmployee authoritiesOfEmployee = QAuthoritiesOfEmployee.authoritiesOfEmployee;
    private final QAuthority authority = QAuthority.authority;

    @Override
    public List<Tuple> findLoginDataByUsername(String email) {
        return queryFactory
                .select(
                        employee,
                        authority.name
                )
                .from(employee)
                .leftJoin(authoritiesOfEmployee).on(employee.id.eq(authoritiesOfEmployee.employeeId))
                .leftJoin(authority).on(authoritiesOfEmployee.authorityCode.eq(authority.id))
                .where(employee.email.eq(email))
                .fetch();
    }
}
