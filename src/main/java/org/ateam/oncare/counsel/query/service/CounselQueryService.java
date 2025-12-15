package org.ateam.oncare.counsel.query.service;


import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface CounselQueryService {

    @Nullable List<CustomerListResponse> searchCustomers(String keyword);
}
