package org.ateam.oncare.counsel.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Mapper
public interface CounselQueryMapper {
    @Nullable List<CustomerListResponse> findAllCustomers();

    @Nullable List<CustomerListResponse> searchCustomersByPhone(String keyword);

    @Nullable List<CustomerListResponse> searchCustomersByName(String keyword);
}
