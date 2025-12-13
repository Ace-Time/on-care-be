package org.ateam.oncare.employee.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.employee.command.entity.Authorities;
import org.ateam.oncare.employee.command.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final AuthorityRepository authorityRepository;


    @Override
    public ResponseLoginEmployeeDTO loginGetEmployee(RequestLogin loginRequest) {


        return null;
    }

    @Override
    public Map<Long, String> getAuthorityMasters() {
        List<Authorities> authorities = authorityRepository.findAll();

        return authorities.stream()
                .collect(Collectors.toMap(
                    Authorities::getId,
                    Authorities::getName
                ));
    }
}
