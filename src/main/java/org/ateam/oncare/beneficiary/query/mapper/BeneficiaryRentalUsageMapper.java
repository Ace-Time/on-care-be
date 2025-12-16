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

    // ✅ 모달 상세 조회 (계약 1건 + 용품 1건)
    RentalContractDetailResponse selectRentalContractDetail(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("rentalContractId") Long rentalContractId,
            @Param("productAssetId") Long productAssetId
    );

}