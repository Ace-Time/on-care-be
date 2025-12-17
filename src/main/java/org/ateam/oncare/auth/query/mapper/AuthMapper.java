package org.ateam.oncare.auth.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.auth.query.dto.RefreshTokenOfEmployeeDTO;

@Mapper
public interface AuthMapper {
    RefreshTokenOfEmployeeDTO selectRefreshTokenOfEmployee(String rtId);
}
