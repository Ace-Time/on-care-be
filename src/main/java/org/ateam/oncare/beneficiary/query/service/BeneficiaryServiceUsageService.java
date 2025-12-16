package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceUsageResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryServiceUsageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceUsageService {

    private final BeneficiaryServiceUsageMapper mapper;

    public ServiceUsageResponse getServiceUsageHistory(Long beneficiaryId) {

        List<ServiceUsageResponse.ServiceUsageMonthItem> histories =
                mapper.selectServiceUsageHistory(beneficiaryId);

        for (ServiceUsageResponse.ServiceUsageMonthItem monthItem : histories) {
            monthItem.setServiceTypes(
                    mapper.selectServiceTypesByMonth(
                            beneficiaryId,
                            monthItem.getMonth()
                    )
            );
        }

        ServiceUsageResponse response = new ServiceUsageResponse();
        response.setHistories(histories);
        return response;
    }
}