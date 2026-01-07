package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.command.entity.BeneficiarySignificant;
import org.ateam.oncare.beneficiary.command.repository.BeneficiarySignificantRepository;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryRegistService;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiary;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiaryResponse;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerFacadeService {
    private final BeneficiaryRegistService beneficiaryRegistService;
    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiarySignificantRepository beneficiarySignificantRepository;

    // 4단계 완료 후 신규 수급자 등록
    @Transactional
    public ResponseEntity<RegistNewBeneficiaryResponse> registNewBeneficiary(RegistNewBeneficiary request){

        if (request.getPotentialCustomerId() != null) {
            boolean exists = beneficiaryRepository.existsByPotentialCustomerId(request.getPotentialCustomerId());

            if (exists) {
                log.warn("이미 등록된 수급자입니다 - Potential Customer ID: {}", request.getPotentialCustomerId());

                RegistNewBeneficiaryResponse response = new RegistNewBeneficiaryResponse();
                response.setSuccess(false);
                response.setMessage("이미 수급자로 등록되어 있습니다. 중복 등록이 불가능합니다.");
                response.setBeneficiaryId(null);

                return ResponseEntity.badRequest().body(response);
            }
        }

        // 신규 수급자 정보 (beneficiary table)
        BigInteger beneficiaryId = beneficiaryRegistService.registBeneficiaryAndReturnId(request); //
        // 요양등급 등록 (beneficiary_care_level table)
        int careLevelId = beneficiaryRegistService.registCareLevelAndReturnId(beneficiaryId, request);
        // 수급자 금액산정 테이블 등록 (beneficiary_count table)
        beneficiaryRegistService.registCount(careLevelId,request);
        // 요양등급 만료 임박 구간 수급자 등록 (expiration_of_care_level table)
        beneficiaryRegistService.registExpiration(beneficiaryId);
        // 수급자 별 위험요소 등록 (riskOfMember table)
        beneficiaryRegistService.registRiskOfMember(beneficiaryId, request);
        // 스케줄 등록
        beneficiaryRegistService.registBeneficiarySchedule(beneficiaryId, request);
        // 수급자 특이사항 등록
        beneficiaryRegistService.registBeneficiarySignificant(beneficiaryId,request);
        // 수급자 히스토리 등록
        beneficiaryRegistService.registHistory(beneficiaryId, request);
        // 수급자 매칭 태그 등록
        beneficiaryRegistService.registTagOfBeneficiary(beneficiaryId, request);

        RegistNewBeneficiaryResponse response = new RegistNewBeneficiaryResponse();
        response.setBeneficiaryId(beneficiaryId);
        response.setMessage("신규 수급자 등록이 완료되었습니다.");
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    @Transactional
    public void deleteBeneficiarySignificant(BigInteger beneficiaryId, Integer significantId) {
        Long beneid = beneficiaryId.longValue();
        Long sigId = significantId.longValue();

        beneficiarySignificantRepository.deleteByBeneficiaryIdAndSignificantId(beneid, sigId);
    }

    @Transactional
    public void addBeneficiarySignificant(BigInteger beneficiaryId, Integer significantId) {
        BeneficiarySignificant significant = new BeneficiarySignificant();
        significant.setBeneficiaryId(beneficiaryId.longValue());
        significant.setSignificantId(significantId.longValue());

        beneficiarySignificantRepository.save(significant);
        // id는 자동으로 생성됨 (AUTO_INCREMENT)
    }
}
