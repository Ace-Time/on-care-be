package org.ateam.oncare.auth.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.query.dto.RefreshTokenOfEmployeeDTO;
import org.ateam.oncare.auth.query.mapper.AuthMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthQueryServiceImpl implements AuthQueryService {

    private final AuthMapper authMapper;

    @Override
    public RefreshTokenOfEmployeeDTO selectRefreshTokenOfEmployee(String id) {

        return authMapper.selectRefreshTokenOfEmployee(id);
    }
}
