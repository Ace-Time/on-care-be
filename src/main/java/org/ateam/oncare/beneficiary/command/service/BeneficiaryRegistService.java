package org.ateam.oncare.beneficiary.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.command.entity.*;
import org.ateam.oncare.beneficiary.command.repository.*;
import org.ateam.oncare.common.command.repository.PersonalTagRepository;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiary;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.schedule.command.repository.BeneficiaryScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeneficiaryRegistService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final GuardianRepository guardianRepository;
    private final BeneficiaryCareLevelRepository beneficiaryCareLevelRepository;
    private final BeneficiaryCountRepository beneficiaryCountRepository;
    private final ExpirationOfCareLevelRepository expirationOfCareLevelRepository;
    private final RiskOfMemberRepository riskOfMemberRepository;
    private final BeneficiaryScheduleRepository beneficiaryScheduleRepository;
    private final BeneficiarySignificantRepository beneficiarySignificantRepository;
    private final BeneficiaryHistoryRepository beneficiaryHistoryRepository;
    private final TagOfBeneficiaryRepository tagOfBeneficiaryRepository;
    private final PersonalTagRepository personalTagRepository;

    @Transactional
    public BigInteger registBeneficiaryAndReturnId(RegistNewBeneficiary request) {
        log.info("신규 수급자 등록 시작: {}", request.getName());
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(request.getName());
        beneficiary.setGender(request.getGender());
        beneficiary.setBirthdate(request.getBirthdate());
        beneficiary.setAddress(request.getAddress());
        beneficiary.setPhone(request.getPhone());
        beneficiary.setStatus(true);  // 1: 서비스중
        beneficiary.setGeoReady(false);  // 좌표 미계산
        beneficiary.setPotentialCustomerId(request.getPotentialCustomerId());
        beneficiary.setRiskId(1);  // 기본 위험도 (추후 계산)
        beneficiary.setLastCounselDate(LocalDateTime.now());

        Beneficiary saved = beneficiaryRepository.save(beneficiary);

        if (request.getGuardianName() != null && !request.getGuardianName().isEmpty()) {
            Guardian guardian = new Guardian();
            guardian.setBeneficiaryId(saved.getId());
            guardian.setName(request.getGuardianName());
            guardian.setPhone(request.getGuardianPhone());
            guardian.setRelation(request.getGuardianRelation());
            guardian.setIsPrimary("Y");  // 주 보호자
            guardianRepository.save(guardian);
        }

        log.info("수급자 등록 완료 - ID: {}", saved.getId());
        return BigInteger.valueOf(saved.getId());
    }


    @Transactional
    public int registCareLevelAndReturnId(BigInteger beneficiaryID, RegistNewBeneficiary request) {
        log.info("요양등급 등록 시작 - 수급자 ID: {}", beneficiaryID);

        BeneficiaryCareLevel careLevel = new BeneficiaryCareLevel();
        careLevel.setBeneficiaryId(beneficiaryID.longValue());
        careLevel.setStartDate(request.getCareLevelStartDate());
        careLevel.setEndDate(request.getCareLevelEndDate());
        careLevel.setNumber(request.getCareLevelNumber());
        careLevel.setRenewal("N");  // 최초 등록

        BeneficiaryCareLevel saved = beneficiaryCareLevelRepository.save(careLevel);

        log.info("요양등급 등록 완료 - ID: {}", saved.getId());
        return saved.getId();
    }

    @Transactional
    public void registCount(int careLevelId, RegistNewBeneficiary request) {
        log.info("금액산정 등록 시작 - 요양등급 ID: {}", careLevelId);

        // 등급에 따른 m_care_level_id 매핑
        Map<String, Integer> levelMap = new HashMap<>();
        levelMap.put("1등급", 1);
        levelMap.put("2등급", 2);
        levelMap.put("3등급", 3);
        levelMap.put("4등급", 4);
        levelMap.put("5등급", 5);
        levelMap.put("인지지원등급", 6);

        Integer mCareLevelId = levelMap.get(request.getLevel());
        if (mCareLevelId == null) {
            log.warn("등급 오류: {}", request.getLevel());
        }

        BeneficiaryCountId countId = new BeneficiaryCountId();
        countId.setBeneficiaryCareLevelId(careLevelId);
        countId.setMCareLevelId(mCareLevelId);

        BeneficiaryCount count = new BeneficiaryCount();
        count.setId(countId);

        beneficiaryCountRepository.save(count);

        log.info("금액산정 등록 완료");
    }

    /**
     * 4. 요양등급 만료 임박 구간 수급자 등록 (expiration_of_care_level table)
     */
    @Transactional
    public void registExpiration(BigInteger beneficiaryId) {
        log.info("만료 임박 구간 등록 시작 - 수급자 ID: {}", beneficiaryId);

            ExpirationOfCareLevel expiration = new ExpirationOfCareLevel();
            expiration.setBeneficiaryId(beneficiaryId.longValue());
            expiration.setExpiredSection(1); // 만료기간 null로
            expiration.setOutboundStatus("N");
            expiration.setExtendsStatus("N");
            expiration.setEmpId(1);  // 직원 번호 null로 돼야하는데 오류난다

            expirationOfCareLevelRepository.save(expiration);

        log.info("만료 임박 구간 등록 완료");
    }

    /**
     * 5. 수급자 별 위험요소 등록 (riskOfMember table)
     */
    @Transactional
    public void registRiskOfMember(BigInteger beneficiaryId, RegistNewBeneficiary request) {
        log.info("위험요소 등록 시작 - 수급자 ID: {}", beneficiaryId);

        if (request.getSelectedRisks() == null || request.getSelectedRisks().isEmpty()) {
            log.info("등록할 위험요소 없음");
            return;
        }

        // 위험요소명 → ID 매핑 (m_risk 테이블 참고)
        Map<String, Integer> riskMap = new HashMap<>();
        riskMap.put("낙상", 1);
        riskMap.put("욕창", 2);
        riskMap.put("치매", 3);
        riskMap.put("고혈압", 4);
        riskMap.put("당뇨", 5);
        riskMap.put("뇌졸증", 6);
        riskMap.put("거동불편", 7);
        riskMap.put("공격성", 8);
        riskMap.put("몽유병", 9);

        for (String riskName : request.getSelectedRisks()) {
            Integer riskId = riskMap.get(riskName);
            if (riskId != null) {
                RiskOfMemberId riskMemberId = new RiskOfMemberId();
                riskMemberId.setBeneficiaryId(beneficiaryId.longValue());
                riskMemberId.setRiskId(riskId);

                RiskOfMember riskOfMember = new RiskOfMember();
                riskOfMember.setId(riskMemberId);

                riskOfMemberRepository.save(riskOfMember);
            }
        }

        log.info("위험요소 등록 완료 - {}건", request.getSelectedRisks().size());
    }

    /**
     * 6. 스케줄 등록 (beneficiary_schedule table)
     */
    @Transactional
    public void registBeneficiarySchedule(BigInteger beneficiaryId, RegistNewBeneficiary request) {
        log.info("스케줄 등록 시작 - 수급자 ID: {}", beneficiaryId);

        if (request.getBeneficiarySchedules() == null || request.getBeneficiarySchedules().isEmpty()) {
            log.info("등록할 스케줄 없음");
            return;
        }

        // 요일 변환
        Map<String, Integer> dayMap = new HashMap<>();
        dayMap.put("월", 1);
        dayMap.put("화", 2);
        dayMap.put("수", 3);
        dayMap.put("목", 4);
        dayMap.put("금", 5);
        dayMap.put("토", 6);
        dayMap.put("일", 7);

        for (RegistNewBeneficiary.BeneficiaryScheduleDto scheduleDto : request.getBeneficiarySchedules()) {
            if (scheduleDto.getBeneficiaryScheduleDay() == null ||
                    scheduleDto.getBeneficiaryScheduleDay().isEmpty()) {
                continue;
            }

            BeneficiarySchedule schedule = new BeneficiarySchedule();
            schedule.setBeneficiaryId(beneficiaryId.longValue());
            schedule.setDay(dayMap.get(scheduleDto.getBeneficiaryScheduleDay()));
            schedule.setStartTime(LocalTime.parse(scheduleDto.getBeneficiaryScheduleStartTime()));
            schedule.setEndTime(LocalTime.parse(scheduleDto.getBeneficiaryScheduleEndTime()));
            schedule.setServiceTypeId(1);  // 기본 서비스 타입 (방문요양)

            beneficiaryScheduleRepository.save(schedule);
        }

        log.info("스케줄 등록 완료 - {}건", request.getBeneficiarySchedules().size());
    }

    /**
     * 7. 수급자 특이사항 등록 (beneficiary_significant table)
     */
    @Transactional
    public void registBeneficiarySignificant(BigInteger beneficiaryId, RegistNewBeneficiary request) {
        log.info("특이사항 등록 시작 - 수급자 ID: {}", beneficiaryId);

        // m_significant 매핑
        Map<String, Long> significantMap = new HashMap<>();
        significantMap.put("렌탈금액민감", 1L);
        significantMap.put("보호자결정의존", 2L);
        significantMap.put("보편상품신뢰", 3L);
        significantMap.put("거동불편", 4L);
        significantMap.put("목욕불편", 5L);
        significantMap.put("문자소통형", 6L);
        significantMap.put("정기연락중시형", 7L);
        significantMap.put("요양보호사고정선호", 8L);
        significantMap.put("성격민감도높음", 9L);
        significantMap.put("금액민감도높음", 10L);
        significantMap.put("금액부담", 11L);

        List<String> significantList = new ArrayList<>();

        // Y로 체크된 항목들만 추가
        if ("Y".equals(request.get렌탈금액민감())) significantList.add("렌탈금액민감");
        if ("Y".equals(request.get보호자결정의존())) significantList.add("보호자결정의존");
        if ("Y".equals(request.get보편상품신뢰())) significantList.add("보편상품신뢰");
        if ("Y".equals(request.get거동불편())) significantList.add("거동불편");
        if ("Y".equals(request.get목욕불편())) significantList.add("목욕불편");
        if ("Y".equals(request.get문자소통형())) significantList.add("문자소통형");
        if ("Y".equals(request.get정기연락중시형())) significantList.add("정기연락중시형");
        if ("Y".equals(request.get요양보호사고정선호())) significantList.add("요양보호사고정선호");
        if ("Y".equals(request.get성격민감도높음())) significantList.add("성격민감도높음");
        if ("Y".equals(request.get금액민감도높음())) significantList.add("금액민감도높음");
        if ("Y".equals(request.get금액부담())) significantList.add("금액부담");

        for (String significant : significantList) {
            Long significantId = significantMap.get(significant);
            if (significantId != null) {
                BeneficiarySignificant bs = BeneficiarySignificant.builder()
                        .beneficiaryId(beneficiaryId.longValue())
                        .significantId(significantId)
                        .build();

                beneficiarySignificantRepository.save(bs);
            }
        }

        log.info("특이사항 등록 완료 - {}건", significantList.size());
    }

    /**
     * 8. 수급자 히스토리 등록 (beneficiary_history table)
     */
    @Transactional
    public void registHistory(BigInteger beneficiaryId, RegistNewBeneficiary request) {
        log.info("히스토리 등록 시작 - 수급자 ID: {}", beneficiaryId);

        BeneficiaryHistory history = BeneficiaryHistory.builder()
                .beneficiaryId(beneficiaryId.longValue())
                .joinDate(request.getContractStartDate().atStartOfDay())
                .terminateDate(request.getContractEndDate().atStartOfDay())
                .build();

        beneficiaryHistoryRepository.save(history);

        log.info("히스토리 등록 완료");
    }

    @Transactional
    public void registTagOfBeneficiary(BigInteger beneficiaryId, RegistNewBeneficiary request) {
        for (String tagName : request.getSelectedMatchTags()) {
            // personal_tag 테이블에서 name으로 id 조회
            Optional<Long> tagIdOpt = personalTagRepository.findIdByTag(tagName);

            if (tagIdOpt.isPresent()) {
                TagOfBeneficiary tag = new TagOfBeneficiary();
                tag.setBeneficiaryId(beneficiaryId.longValue());
                tag.setTagId(tagIdOpt.get());
                tagOfBeneficiaryRepository.save(tag);
            }
        }
    }
}
