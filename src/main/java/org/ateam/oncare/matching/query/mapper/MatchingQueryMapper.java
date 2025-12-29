package org.ateam.oncare.matching.query.mapper;

import org.ateam.oncare.matching.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;
import java.util.List;

@Mapper
public interface MatchingQueryMapper {

    List<BeneficiaryScheduleDto> selectBeneficiarySchedules(@Param("beneficiaryId") Long beneficiaryId);

    List<CareWorkerIdDto> selectAvailableCareWorkerIds(
            @Param("targetBeneficiaryId") Long targetBeneficiaryId,
            @Param("day") Integer day,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<CareWorkerIdDto> selectCareWorkerIdsByServiceType(@Param("beneficiaryId") Long beneficiaryId);

    List<CareWorkerIdDto> selectCareWorkerIdsByRiskCertificates(@Param("beneficiaryId") Long beneficiaryId);

    List<BeneficiarySummaryDto> selectBeneficiariesSummary();

    BeneficiaryDetailDto selectBeneficiaryDetail(@Param("beneficiaryId") Long beneficiaryId);

    List<String> selectBeneficiaryServiceTypes(@Param("beneficiaryId") Long beneficiaryId);

    List<String> selectBeneficiaryTags(@Param("beneficiaryId") Long beneficiaryId);

    List<String> selectBeneficiaryRiskFactors(@Param("beneficiaryId") Long beneficiaryId);

    List<BeneficiaryScheduleViewDto> selectBeneficiaryScheduleViews(@Param("beneficiaryId") Long beneficiaryId);

    AssignedCareWorkerDto selectAssignedCareWorker(@Param("beneficiaryId") Long beneficiaryId);

    List<CareWorkerCardDto> selectCareWorkerCardsByIds(@Param("ids") List<Long> ids);

    List<String> selectCareWorkerTags(@Param("careWorkerId") Long careWorkerId);

    CareWorkerDetailDto selectCareWorkerDetail(@Param("careWorkerId") Long careWorkerId);

    List<String> selectCareWorkerServiceTypes(@Param("careWorkerId") Long careWorkerId);

    List<WorkingTimeDto> selectCareWorkerWorkingTimes(@Param("careWorkerId") Long careWorkerId);

    Long selectAssignedCareWorkerId(@Param("beneficiaryId") Long beneficiaryId);

    // MatchingQueryMapper.java 에 아래 메서드들만 추가

    List<CareWorkerIdDto> selectAvailableCareWorkerIdsByVisitSchedule(
            @Param("vsId") Long vsId,
            @Param("startDt") String startDt,
            @Param("endDt") String endDt
    );

    List<CareWorkerIdDto> selectCareWorkerIdsByVisitServiceType(@Param("vsId") Long vsId);

    Long selectCareWorkerIdByVisitScheduleId(@Param("vsId") Long vsId);

    Long selectVisitScheduleBeneficiaryId(@Param("vsId") Long vsId);

    List<CareWorkerIdDto> selectAvailableCareWorkerIdsByVisitTime(String startDt, String endDt);
    List<CareWorkerIdDto> selectCareWorkerIdsByServiceTypeId(Long serviceTypeId);

    LatLngDto selectBeneficiaryLatLng(Long beneficiaryId);

    List<CareWorkerLatLngDto> selectCareWorkerLatLngByIds(@Param("ids") List<Long> ids);

    List<TagOverlapCountDto> selectTagOverlapCounts(@Param("beneficiaryId") Long beneficiaryId,
                                                    @Param("ids") List<Long> ids);

    // geo - beneficiary
    BeneficiaryGeoDto selectBeneficiaryGeoForUpdate(Long beneficiaryId);
    int updateBeneficiaryGeo(Long beneficiaryId, Double lat, Double lng);
    int updateBeneficiaryGeoReadyOnly(Long beneficiaryId);

    // geo - careworker (care_worker -> employee_id)
    List<CareWorkerGeoDto> selectCareWorkerGeoForUpdateByIds(@org.apache.ibatis.annotations.Param("ids") List<Long> ids);
    int updateCareWorkerEmployeeGeo(@org.apache.ibatis.annotations.Param("careWorkerId") Long careWorkerId,
                                    @org.apache.ibatis.annotations.Param("lat") Double lat,
                                    @org.apache.ibatis.annotations.Param("lng") Double lng);
    int updateCareWorkerEmployeeGeoReadyOnly(@org.apache.ibatis.annotations.Param("careWorkerId") Long careWorkerId);

}