    package org.ateam.oncare.beneficiary.query.service;

    import lombok.RequiredArgsConstructor;
    import org.ateam.oncare.beneficiary.query.dto.response.RentalUsageResponse;
    import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryRentalUsageMapper;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class BeneficiaryRentalUsageService {

        private final BeneficiaryRentalUsageMapper mapper;

        public RentalUsageResponse getRentalUsage(Long beneficiaryId) {
            RentalUsageResponse res = new RentalUsageResponse();
            res.setCurrent(mapper.selectCurrentRentals(beneficiaryId));
            res.setHistory(mapper.selectRentalHistories(beneficiaryId));
            return res;
        }
    }