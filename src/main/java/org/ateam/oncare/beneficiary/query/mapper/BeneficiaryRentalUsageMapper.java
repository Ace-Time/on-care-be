package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.RentalContractDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.RentalUsageResponse;

import java.util.List;

@Mapper
public interface BeneficiaryRentalUsageMapper {
    List<RentalUsageResponse.RentalItem> selectCurrentRentals(@Param("beneficiaryId") Long beneficiaryId);
    List<RentalUsageResponse.RentalItem> selectRentalHistories(@Param("beneficiaryId") Long beneficiaryId);

    RentalContractDetailResponse selectRentalContractDetail(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("rentalContractId") Long rentalContractId,
            @Param("productAssetId") String productAssetId
    );
}