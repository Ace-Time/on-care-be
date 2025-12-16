package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.beneficiary.query.dto.request.BeneficiarySearchRequest;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryListItemResponse;

import java.util.List;

@Mapper
public interface BeneficiaryMapper {

    List<BeneficiaryListItemResponse> selectBeneficiaries(BeneficiarySearchRequest req);

    long countBeneficiaries(BeneficiarySearchRequest req);
}