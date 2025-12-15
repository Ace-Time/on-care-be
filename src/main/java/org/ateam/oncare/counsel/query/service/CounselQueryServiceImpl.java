package org.ateam.oncare.counsel.query.service;

import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.ateam.oncare.counsel.query.mapper.CounselQueryMapper;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class CounselQueryServiceImpl implements CounselQueryService {
    private final CounselQueryMapper counselQueryMapper;

    @Autowired
    public CounselQueryServiceImpl(CounselQueryMapper counselQueryMapper) {
        this.counselQueryMapper = counselQueryMapper;
    }

    @Override
    public @Nullable List<CustomerListResponse> searchCustomers(String keyword) {
        if(!StringUtils.hasText(keyword)){
            return counselQueryMapper.findAllCustomers();
        }
        if (keyword.matches("^[0-9-]*$")) {
            String cleanPhone = keyword.replaceAll("-", "");
            return counselQueryMapper.searchCustomersByPhone(cleanPhone);
        }
        return counselQueryMapper.searchCustomersByName(keyword);
    }
}
