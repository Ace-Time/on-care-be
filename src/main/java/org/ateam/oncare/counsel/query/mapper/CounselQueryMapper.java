package org.ateam.oncare.counsel.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.counsel.query.dto.CounselDetailResponse;
import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface CounselQueryMapper {
    @Nullable List<CustomerListResponse> findAllCustomers();

    @Nullable List<CustomerListResponse> searchCustomersByPhone(String keyword);

    @Nullable List<CustomerListResponse> searchCustomersByName(String keyword);

    List<CounselListResponse> findCounselsByCustomerId(@Param("customerId")BigInteger customerId,
                                                       @Param("customerType")String customerType,
                                                       @Param("counselCategoryName")String counselCategoryName,
                                                       @Param("limit")int limit, @Param("offset")long offset);

    @Nullable CounselDetailResponse findCounselDetailById(BigInteger counselHistoryId);

    Long findPotentialIdByBeneficiaryId(Long beneficiaryId);

    Long findLastCounselorId(@Param("beneficiaryId") Long beneficiaryId);
}
