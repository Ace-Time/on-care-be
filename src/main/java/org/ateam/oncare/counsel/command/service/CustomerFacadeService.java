package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryRegistService;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiary;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerFacadeService {
    private final BeneficiaryRegistService beneficiaryRegistService;

    // 4단계 완료 후 신규 수급자 등록
    @Transactional
    public ResponseEntity<RegistNewBeneficiaryResponse> registNewBeneficiary(RegistNewBeneficiary request){

        // 신규 수급자 정보 (beneficiary table)
        BigInteger beneficiaryID = beneficiaryRegistService.registBeneficiaryAndReturnId(request); //
        // 요양등급 등록 (beneficiary_care_level table)
        int careLevelId = beneficiaryRegistService.registCareLevelAndReturnId(beneficiaryID, request);
        // 수급자 금액산정 테이블 등록 (beneficiary_count table)
        beneficiaryRegistService.registCount(careLevelId,request);
        // 요양등급 만료 임박 구간 수급자 등록 (expiration_of_care_level table)
        beneficiaryRegistService.registExpiration(beneficiaryID);
        // 수급자 별 위험요소 등록 (riskOfMember table)
        beneficiaryRegistService.registRiskOfMember(beneficiaryID);
        // 스케줄 등록
        beneficiaryRegistService.registBeneficiarySchedule(beneficiaryID, request);
        // 수급자 특이사항 등록
        beneficiaryRegistService.registBeneficiarySignificant(beneficiaryID,request);
        // 수급자 히스토리 등록
        beneficiaryRegistService.registHistory(beneficiaryID, request);


    }
}
