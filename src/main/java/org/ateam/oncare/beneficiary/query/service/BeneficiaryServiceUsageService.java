package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceTypeSummaryResponse;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceUsageResponse;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceVisitRecordResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryServiceUsageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceUsageService {

    private final BeneficiaryServiceUsageMapper mapper;

    // 1) 월별 전체 누계
    public ServiceUsageResponse getServiceUsageHistory(Long beneficiaryId) {
        List<ServiceUsageResponse.ServiceUsageMonthItem> histories =
                mapper.selectServiceUsageHistory(beneficiaryId);

        ServiceUsageResponse response = new ServiceUsageResponse();
        response.setHistories(histories);
        return response;
    }

    // 2) 선택한 월의 서비스유형별 누계
    public ServiceTypeSummaryResponse getServiceTypeSummary(Long beneficiaryId, String month) {
        List<ServiceTypeSummaryResponse.ServiceTypeSummaryItem> items =
                mapper.selectServiceTypeSummaryByMonth(beneficiaryId, month);

        ServiceTypeSummaryResponse response = new ServiceTypeSummaryResponse();
        response.setMonth(month);
        response.setServiceTypes(items);
        return response;
    }

    // 3) 선택한 월 + 선택한 서비스유형 방문 리스트
    public ServiceVisitRecordResponse getVisitRecords(Long beneficiaryId, String month, Integer serviceTypeId) {
        List<ServiceVisitRecordResponse.VisitRecordItem> items =
                mapper.selectVisitRecordsByMonthAndType(beneficiaryId, month, serviceTypeId);

        ServiceVisitRecordResponse response = new ServiceVisitRecordResponse();
        response.setMonth(month);
        response.setServiceTypeId(serviceTypeId);

        // 리스트가 비어도 serviceTypeName은 프론트 표시상 필요할 수 있어서(옵션)
        if (!items.isEmpty()) {
            response.setServiceTypeName(items.get(0).getServiceTypeName());
        }

        response.setRecords(items);
        return response;
    }
}