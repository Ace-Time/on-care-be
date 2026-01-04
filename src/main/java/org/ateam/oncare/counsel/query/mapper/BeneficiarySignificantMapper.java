package org.ateam.oncare.counsel.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.counsel.query.dto.BeneficiarySignificantResponse;
import org.ateam.oncare.counsel.query.dto.SignificantMasterResponse;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface BeneficiarySignificantMapper {
    List<BeneficiarySignificantResponse> findSignificantsByBeneficiaryIdAndCategory(BigInteger beneficiaryId, String categoryName);

    List<BeneficiarySignificantResponse> findSignificantsByBeneficiaryId(BigInteger beneficiaryId);

    List<SignificantMasterResponse> findAllSignificants();
}
