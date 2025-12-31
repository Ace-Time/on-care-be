package org.ateam.oncare.counsel.query.service;


import org.ateam.oncare.counsel.query.dto.CounselDetailResponse;
import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.math.BigInteger;
import java.util.List;

public interface CounselQueryService {

    @Nullable List<CustomerListResponse> searchCustomers(String keyword);

    @Nullable Slice<CounselListResponse> findCounselsByCustomerId(BigInteger customerId, String customerType, String counselCategoryName, Pageable pageable);

    @Nullable CounselDetailResponse findCounselDetailById(BigInteger counselHistoryId);

    Long findPotentialIdByBeneficiaryId(Long beneficiaryId);
}
