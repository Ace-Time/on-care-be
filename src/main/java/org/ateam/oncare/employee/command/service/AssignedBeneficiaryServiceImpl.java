package org.ateam.oncare.employee.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.beneficiary.command.repository.BeneficiaryRepository;
import org.ateam.oncare.employee.command.dto.AssignedBeneficiaryDTO;
import org.ateam.oncare.employee.command.repository.AssignedBeneficiaryRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignedBeneficiaryServiceImpl implements AssignedBeneficiaryService {

        private final AssignedBeneficiaryRepository assignedBeneficiaryRepository;
        private final BeneficiaryRepository beneficiaryRepository;

        @Override
        public List<AssignedBeneficiaryDTO> getAssignedBeneficiaries(Long careWorkerId) {

                // 1. [매칭 조회] 해당 요양보호사의 활성 매칭(Y) 가져오기
                List<Matching> matchings = assignedBeneficiaryRepository.findByCareWorkerIdAndStatus(careWorkerId, "Y");

                if (matchings.isEmpty()) {
                        return List.of();
                }

                // 2. [ID 추출] 수급자 ID 목록 추출
                List<Long> beneficiaryIds = matchings.stream()
                                .map(Matching::getBeneficiaryId)
                                .toList();

                // 3. [수급자 조회] ID 목록으로 수급자 정보 일괄 조회 (IN절 활용)
                List<Beneficiary> beneficiaries = beneficiaryRepository.findAllById(beneficiaryIds);

                // 4. [매핑 준비] 조회 속도를 위해 Map 변환 (Key: ID -> Value: Beneficiary)
                Map<Long, Beneficiary> beneficiaryMap = beneficiaries.stream()
                                .collect(Collectors.toMap(Beneficiary::getId, b -> b));

                // 5. [DTO 변환] Matching 정보와 Beneficiary 정보를 합쳐서 반환
                return matchings.stream()
                                .map(m -> {
                                        Beneficiary b = beneficiaryMap.get(m.getBeneficiaryId());

                                        String name = (b != null) ? b.getName() : "정보 없음";

                                        // [수정 포인트]
                                        // 1. getBirthDate() -> getBirthdate() (대소문자 일치)
                                        // 2. LocalDate -> String 변환 (.toString())
                                        String birthDate = (b != null && b.getBirthdate() != null)
                                                        ? b.getBirthdate().toString()
                                                        : "";

                                        String gender = (b != null) ? b.getGender() : "";

                                        return AssignedBeneficiaryDTO.builder()
                                                        .matchingId(m.getId())
                                                        .startDate(m.getStartDate())
                                                        .endDate(m.getEndDate())
                                                        .status(m.getStatus())
                                                        .beneficiaryId(m.getBeneficiaryId())
                                                        .name(name)
                                                        .birthDate(birthDate) // 변환된 문자열 넣기
                                                        .gender(gender)
                                                        .build();
                                })
                                .toList();
        }
}