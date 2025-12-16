package org.ateam.oncare.employee.command.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.ateam.oncare.employee.command.entity.Employee;
import org.ateam.oncare.employee.command.entity.QAuthority;
import org.ateam.oncare.employee.command.entity.QEmployee;
import org.ateam.oncare.employee.command.repository.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeUserDetailService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        List<Tuple> results = employeeRepository.findLoginDataByUsername(email);

        //첫 번째 Row에서 직원 공통 정보 추출
        Employee employee = results.get(0).get(QEmployee.employee);

        // 여러 Row에 흩어진 권한 이름들을 모아서 -> List<GrantedAuthority>로 변환
        List<SimpleGrantedAuthority> authorities = results.stream()
                .map(tuple -> tuple.get(QAuthority.authority.name))// 권한 이름 추출
                .filter(Objects::nonNull) // null 방지
                .map(SimpleGrantedAuthority::new)// String -> GrantedAuthority 변환
                .collect(Collectors.toList());

        EmployeeImpl employeeImpl = new EmployeeImpl(
                employee.getEmail(),
                employee.getPw(),
                authorities
        );

        employeeImpl.setDetails(
                employee.getId(),
                employee.getEmail(),
                employee.getName(),
                employee.getPhone()
        );

        log.debug("queryDSL 결과(employeeImpl):{}", employeeImpl);
        log.debug("queryDSL 결과(권한):{}", authorities);


        return employeeImpl;
    }
}
