package org.ateam.oncare.auth.query.service;

import org.ateam.oncare.auth.query.dto.RefreshTokenOfEmployeeDTO;

public interface AuthQueryService {
    RefreshTokenOfEmployeeDTO selectRefreshTokenOfEmployee(String id);
}
