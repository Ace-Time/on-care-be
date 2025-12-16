package org.ateam.oncare.beneficiary.query.service;

import org.ateam.oncare.beneficiary.query.dto.request.BeneficiarySearchRequest;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryListItemResponse;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;

public interface BeneficiaryService {
    PageResponse<BeneficiaryListItemResponse> getBeneficiaries(BeneficiarySearchRequest req);
}