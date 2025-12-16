package org.ateam.oncare.beneficiary.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceUsageResponse;

import java.util.List;

@Mapper
public interface BeneficiaryServiceUsageMapper {

    List<ServiceUsageResponse.ServiceUsageMonthItem>
    selectServiceUsageHistory(@Param("beneficiaryId") Long beneficiaryId);

    List<ServiceUsageResponse.ServiceTypeItem>
    selectServiceTypesByMonth(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("month") String month
    );
}