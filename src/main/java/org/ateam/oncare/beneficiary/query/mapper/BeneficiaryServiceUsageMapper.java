package org.ateam.oncare.beneficiary.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceTypeSummaryResponse;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceUsageResponse;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceVisitRecordResponse;

import java.util.List;

@Mapper
public interface BeneficiaryServiceUsageMapper {

    // 1) 월별 전체 누계
    List<ServiceUsageResponse.ServiceUsageMonthItem>
    selectServiceUsageHistory(@Param("beneficiaryId") Long beneficiaryId);

    // 2) 선택 월의 서비스유형별 누계(금액/횟수)
    List<ServiceTypeSummaryResponse.ServiceTypeSummaryItem>
    selectServiceTypeSummaryByMonth(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("month") String month
    );

    // 3) 선택 월 + 선택 서비스유형 방문 리스트
    List<ServiceVisitRecordResponse.VisitRecordItem>
    selectVisitRecordsByMonthAndType(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("month") String month,
            @Param("serviceTypeId") Integer serviceTypeId
    );
}