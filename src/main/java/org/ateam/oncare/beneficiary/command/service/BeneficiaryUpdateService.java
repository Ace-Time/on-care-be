package org.ateam.oncare.beneficiary.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.dto.request.BeneficiaryUpdateRequest;
import org.ateam.oncare.beneficiary.command.dto.response.BeneficiaryUpdateResponse;
import org.ateam.oncare.beneficiary.command.mapper.BeneficiaryUpdateMapper;
import org.ateam.oncare.beneficiary.command.repository.BeneficiaryUpdateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryUpdateService {

    private final BeneficiaryUpdateMapper mapper;
    private final BeneficiaryUpdateRepository beneficiaryUpdateRepository;   // 최근 상담일 수정 위해 추가

    @Transactional
    public BeneficiaryUpdateResponse update(Long beneficiaryId, BeneficiaryUpdateRequest req) {

        // 1) beneficiary 기본정보 UPDATE
        int updated = mapper.updateBeneficiary(beneficiaryId, req);
        if (updated == 0) {
            throw new IllegalArgumentException("존재하지 않는 수급자입니다. id=" + beneficiaryId);
        }

        // 2) 보호자 upsert (보호자 정보가 들어온 경우에만)
        boolean hasGuardian =
                (req.getGuardianName() != null && !req.getGuardianName().isBlank())
                        || (req.getGuardianPhone() != null && !req.getGuardianPhone().isBlank())
                        || (req.getGuardianRelation() != null && !req.getGuardianRelation().isBlank());

        if (hasGuardian) {
            mapper.upsertGuardian(beneficiaryId, req);
        }

        // 3) 태그 동기화: 전체 삭제 후 재등록
        mapper.deleteBeneficiaryTags(beneficiaryId);
        List<Long> tagIds = req.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            mapper.insertBeneficiaryTags(beneficiaryId, tagIds);
        }

        // 4) 위험요소 동기화: 전체 삭제 후 재등록
        mapper.deleteRiskFactors(beneficiaryId);
        List<Integer> riskIds = req.getRiskFactorIds();
        if (riskIds != null && !riskIds.isEmpty()) {
            mapper.insertRiskFactors(beneficiaryId, riskIds);
        }

        // 4-1) 위험요소 기반 beneficiary.risk_id 갱신
        mapper.updateRiskLevelByFactors(beneficiaryId);

        // ✅ 5) (옵션) care level (만료일/인정번호) 수정
        boolean hasCareLevelInfo =
                (req.getCareLevelEndDate() != null && !req.getCareLevelEndDate().isBlank())
                        || (req.getCareLevelNumber() != null);

        if (hasCareLevelInfo) {
            mapper.updateCareLevelInfo(beneficiaryId, req);
        }

        // ✅ 5-1) (옵션) 장기요양등급 수정 (beneficiary_count.m_care_level_id)
        if (req.getCareLevelId() != null) {
            mapper.updateCareLevelGrade(beneficiaryId, req.getCareLevelId());
        }

        // ✅ 응답: 요청값 그대로 + message
        return BeneficiaryUpdateResponse.builder()
                .beneficiaryId(beneficiaryId)
                .message("수급자 기본 정보 수정이 완료되었습니다.")
                .name(req.getName())
                .gender(req.getGender())
                .birthdate(req.getBirthdate())
                .phone(req.getPhone())
                .address(req.getAddress())
                .status(req.getStatus())
                .guardianName(req.getGuardianName())
                .guardianRelation(req.getGuardianRelation())
                .guardianPhone(req.getGuardianPhone())
                .tagIds(req.getTagIds())
                .riskFactorIds(req.getRiskFactorIds())
                .careLevelEndDate(req.getCareLevelEndDate())
                .careLevelNumber(req.getCareLevelNumber())
                .careLevelId(req.getCareLevelId())
                .build();
    }

    @Transactional
    public void updateLastCounselDate(Long beneficiaryId, LocalDateTime consultDate) {
        beneficiaryUpdateRepository.updateLastCounselDate(beneficiaryId, consultDate);
    } // 최근 상담일 수정을 위한 메소드
}
