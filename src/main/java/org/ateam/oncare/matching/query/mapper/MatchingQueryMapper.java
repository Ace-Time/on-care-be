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

    List<BeneficiarySummaryDto> selectBeneficiariesSummary(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("keyword") String keyword,
            @Param("assignedFilter") String assignedFilter
    );

    long countBeneficiaries(
            @Param("keyword") String keyword,
            @Param("assignedFilter") String assignedFilter
    );

    BeneficiaryDetailDto selectBeneficiaryDetail(@Param("beneficiaryId") Long beneficiaryId);

    List<String> selectBeneficiaryServiceTypes(@Param("beneficiaryId") Long beneficiaryId);

    List<String> selectBeneficiaryTags(@Param("beneficiaryId") Long beneficiaryId);

    List<String> selectBeneficiaryRiskFactors(@Param("beneficiaryId") Long beneficiaryId);

    List<BeneficiaryScheduleViewDto> selectBeneficiaryScheduleViews(@Param("beneficiaryId") Long beneficiaryId);

    AssignedCareWorkerDto selectAssignedCareWorker(@Param("beneficiaryId") Long beneficiaryId);

    Long selectAssignedCareWorkerId(@Param("beneficiaryId") Long beneficiaryId);

    List<CareWorkerCardDto> selectCareWorkerCardsByIds(@Param("ids") List<Long> ids);

    List<String> selectCareWorkerTags(@Param("careWorkerId") Long careWorkerId);

    CareWorkerDetailDto selectCareWorkerDetail(@Param("careWorkerId") Long careWorkerId);

    List<String> selectCareWorkerServiceTypes(@Param("careWorkerId") Long careWorkerId);

    List<String> selectCareWorkerCertificateNames(@Param("careWorkerId") Long careWorkerId);

    List<WorkingTimeDto> selectCareWorkerWorkingTimes(@Param("careWorkerId") Long careWorkerId);

    List<CareWorkerIdDto> selectAvailableCareWorkerIdsByVisitSchedule(
            @Param("vsId") Long vsId,
            @Param("startDt") String startDt,
            @Param("endDt") String endDt
    );

    List<CareWorkerIdDto> selectCareWorkerIdsByVisitServiceType(@Param("vsId") Long vsId);

    Long selectCareWorkerIdByVisitScheduleId(@Param("vsId") Long vsId);

    Long selectVisitScheduleBeneficiaryId(@Param("vsId") Long vsId);

    List<CareWorkerIdDto> selectAvailableCareWorkerIdsByVisitTime(
            @Param("startDt") String startDt,
            @Param("endDt") String endDt
    );
    ServiceTypePairDto selectBeneficiaryPrimaryServiceType(@Param("beneficiaryId") Long beneficiaryId);

    List<CareWorkerIdDto> selectCareWorkerIdsByServiceTypeId(@Param("serviceTypeId") Long serviceTypeId);

    LatLngDto selectBeneficiaryLatLng(@Param("beneficiaryId") Long beneficiaryId);

    int existsBeneficiaryVisitConflict(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("startDt") String startDt,
            @Param("endDt") String endDt
    );

    List<CareWorkerLatLngDto> selectCareWorkerLatLngByIds(@Param("ids") List<Long> ids);

    List<TagOverlapCountDto> selectTagOverlapCounts(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("ids") List<Long> ids
    );

    BeneficiaryGeoDto selectBeneficiaryGeoForUpdate(@Param("beneficiaryId") Long beneficiaryId);

    int updateBeneficiaryGeo(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("lat") Double lat,
            @Param("lng") Double lng
    );

    int updateBeneficiaryGeoReadyOnly(@Param("beneficiaryId") Long beneficiaryId);

    List<CareWorkerGeoDto> selectCareWorkerGeoForUpdateByIds(@Param("ids") List<Long> ids);

    int updateCareWorkerEmployeeGeo(
            @Param("careWorkerId") Long careWorkerId,
            @Param("lat") Double lat,
            @Param("lng") Double lng
    );

    int updateCareWorkerEmployeeGeoReadyOnly(@Param("careWorkerId") Long careWorkerId);
}