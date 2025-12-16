package org.ateam.oncare.beneficiary.command.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BeneficiaryRentalContractMapper {

    int completeRentalContract(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("rentalContractId") Long rentalContractId
    );
}