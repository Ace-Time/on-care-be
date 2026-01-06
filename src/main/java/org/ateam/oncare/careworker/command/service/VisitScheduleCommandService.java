package org.ateam.oncare.careworker.command.service;

//import org.ateam.oncare.beneficiary.command.service.BeneficiaryCostService;
import org.ateam.oncare.careworker.command.dto.CompleteVisitRequest;
import org.ateam.oncare.careworker.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.dto.StartVisitRequest;
import org.ateam.oncare.careworker.command.dto.UpdateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.mapper.CareLogCommandMapper;
import org.ateam.oncare.careworker.command.mapper.VisitScheduleCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitScheduleCommandService {

    private final VisitScheduleCommandMapper visitScheduleCommandMapper;
    private final CareLogCommandMapper careLogCommandMapper;
    // private final BeneficiaryCostService beneficiaryCostService;

    @Transactional
    public void startVisit(Long vsId) {
        log.info("서비스 시작 - vsId: {}", vsId);
        int updated = visitScheduleCommandMapper.updateVisitStatusToInProgress(vsId);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없거나 이미 시작되었습니다.");
        }

        log.info("서비스 시작 완료 - vsId: {} (현재 시간 자동 기록)", vsId);
    }

    @Transactional
    public void completeVisit(Long vsId) {
        log.info("서비스 종료 - vsId: {}", vsId);
        int updated = visitScheduleCommandMapper.updateVisitStatusToCompleted(vsId);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없거나 이미 완료되었습니다.");
        }

        log.info("서비스 종료 완료 - vsId: {} (현재 시간 자동 기록)", vsId);

        // // RFID 출퇴근 완료 후 서비스 비용 계산 및 월별 누적
        // try {
        // beneficiaryCostService.accumulateCostForCompletedVisit(vsId);
        // } catch (Exception e) {
        // log.error("비용 계산 중 오류 발생 - vsId: {}", vsId, e);
        // // 비용 계산 실패해도 서비스 완료는 성공으로 처리 (보상 트랜잭션 필요 시 추가)
        // }
    }

    @Transactional
    public void createVisitSchedule(Long careWorkerId, CreateVisitScheduleRequest request) {
        if (request.getStartDt() != null && request.getEndDt() != null
                && request.getStartDt().isAfter(request.getEndDt())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        // serviceTypeIds가 있으면 여러 서비스 유형 처리
        java.util.List<Long> serviceTypeIds = getServiceTypeIds(request);

        if (serviceTypeIds != null && !serviceTypeIds.isEmpty()) {
            log.info("방문 요양 일정 작성 시작 - careWorkerId: {}, beneficiaryId: {}, serviceTypeIds: {}",
                    careWorkerId, request.getBeneficiaryId(), serviceTypeIds);

            // 각 서비스 유형마다 별도의 일정 생성
            for (Long serviceTypeId : serviceTypeIds) {
                CreateVisitScheduleRequest singleRequest = new CreateVisitScheduleRequest();
                singleRequest.setBeneficiaryId(request.getBeneficiaryId());
                singleRequest.setServiceTypeId(serviceTypeId);
                singleRequest.setStartDt(request.getStartDt());
                singleRequest.setEndDt(request.getEndDt());
                singleRequest.setVisitStatus(request.getVisitStatus());
                singleRequest.setNote(request.getNote());

                int inserted = visitScheduleCommandMapper.insertVisitSchedule(careWorkerId, singleRequest);

                if (inserted == 0) {
                    throw new IllegalStateException("방문 요양 일정 작성에 실패했습니다. serviceTypeId: " + serviceTypeId);
                }
            }

            log.info("방문 요양 일정 작성 완료 - 생성된 일정 개수: {}", serviceTypeIds.size());
        } else {
            throw new IllegalArgumentException("서비스 유형이 지정되지 않았습니다.");
        }
    }

    @Transactional
    public void updateVisitSchedule(Long vsId, UpdateVisitScheduleRequest request) {
        if (request.getStartDt() != null && request.getEndDt() != null
                && request.getStartDt().isAfter(request.getEndDt())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        // serviceTypeIds가 있으면 여러 서비스 유형 처리 (Reconciliation Logic)
        java.util.List<Long> serviceTypeIds = getServiceTypeIds(request);

        if (serviceTypeIds != null && !serviceTypeIds.isEmpty()) {
            log.info("방문 요양 일정 수정 - vsId: {}, serviceTypeIds: {}", vsId, serviceTypeIds);

            // 0. 현재 일정의 기본 정보 조회 (수급자, 요양보호사, 날짜 확인용)
            // selectVisitScheduleById를 통해 원본 일정을 조회하여, 날짜가 변경되었더라도 "기존" 형제 일정을 찾을 수 있도록 함.
            org.ateam.oncare.schedule.command.entity.VisitSchedule originalSchedule = visitScheduleCommandMapper
                    .selectVisitScheduleById(vsId);

            if (originalSchedule == null) {
                throw new IllegalArgumentException("해당 방문 요양 일정을 찾을 수 없습니다. vsId: " + vsId);
            }

            // 1. 형제 일정 조회 (동일 날짜, 동일 수급자, 동일 요양보호사)
            // request.getStartDt() (변경할 날짜)가 아니라, originalSchedule.getStartDt() (기존 날짜)를
            // 사용해야 함.
            // 조회 시 careWorkerId가 필요함.
            java.util.List<org.ateam.oncare.schedule.command.entity.VisitSchedule> siblings = visitScheduleCommandMapper
                    .selectVisitScheduleSiblings(
                            originalSchedule.getBeneficiaryId(),
                            originalSchedule.getCareWorkerId(),
                            originalSchedule.getStartDt().toLocalDate());

            log.info("형제 일정 조회 결과 (Original Date): {}건", siblings.size());

            // 2. Reconciliation (재조정)
            // Existing Map: ServiceType -> VsId
            java.util.Map<Long, Long> existingMap = new java.util.HashMap<>();
            for (org.ateam.oncare.schedule.command.entity.VisitSchedule s : siblings) {
                existingMap.put(s.getServiceTypeId(), s.getVsId());
            }

            java.util.Set<Long> requestedSet = new java.util.HashSet<>(serviceTypeIds);

            // 2-1. Update (교집합): 이미 존재하는 서비스 유형 -> 내용 업데이트
            for (Long sTypeId : serviceTypeIds) {
                if (existingMap.containsKey(sTypeId)) {
                    Long existingVsId = existingMap.get(sTypeId);
                    log.info("일정 업데이트 (기존 존재) - vsId: {}, serviceTypeId: {}", existingVsId, sTypeId);

                    // 재활용할 request 객체 복사 또는 수정 (serviceTypeId 설정)
                    request.setServiceTypeId(sTypeId); // 해당 타입으로 설정
                    visitScheduleCommandMapper.updateVisitSchedule(existingVsId, request);
                }
            }

            // 2-2. Insert (차집합: Request - Existing): 없는 서비스 유형 -> 새로 생성
            // Insert 시에는 employeeId가 필요함!
            Long employeeId = visitScheduleCommandMapper.selectEmployeeIdByVsId(vsId); // 아무 vsId나 써도 됨 (같은 작업자니까)

            for (Long sTypeId : serviceTypeIds) {
                if (!existingMap.containsKey(sTypeId)) {
                    log.info("일정 신규 생성 (추가) - serviceTypeId: {}", sTypeId);

                    CreateVisitScheduleRequest createRequest = new CreateVisitScheduleRequest();
                    createRequest.setBeneficiaryId(request.getBeneficiaryId());
                    createRequest.setServiceTypeId(sTypeId);
                    createRequest.setStartDt(request.getStartDt());
                    createRequest.setEndDt(request.getEndDt());
                    createRequest.setVisitStatus(request.getVisitStatus());
                    createRequest.setNote(request.getNote());

                    visitScheduleCommandMapper.insertVisitSchedule(employeeId, createRequest);
                }
            }

            // 2-3. Delete (차집합: Existing - Request): 선택 해제된 서비스 유형 -> 삭제
            for (Long existingType : existingMap.keySet()) {
                if (!requestedSet.contains(existingType)) {
                    Long targetVsId = existingMap.get(existingType);
                    log.info("일정 삭제 (선택 해제) - vsId: {}, serviceTypeId: {}", targetVsId, existingType);
                    deleteVisitSchedule(targetVsId);
                }
            }
        } else {
            // serviceTypeIds가 null이거나 비어있으면 예외 처리 또는 단일 vsId만 업데이트 (기존 로직)
            // 하지만 프론트에서 항상 배열로 보낸다면 위 로직만 타게 됨.
            // 방어 코드로 단일 업데이트 유지
            log.info("단일 일정 수정 (Legacy) - vsId: {}", vsId);
            visitScheduleCommandMapper.updateVisitSchedule(vsId, request);
        }
    }

    // serviceTypeIds 또는 serviceTypeId를 반환하는 헬퍼 메서드
    private java.util.List<Long> getServiceTypeIds(CreateVisitScheduleRequest request) {
        if (request.getServiceTypeIds() != null && !request.getServiceTypeIds().isEmpty()) {
            return request.getServiceTypeIds();
        } else if (request.getServiceTypeId() != null) {
            return java.util.Collections.singletonList(request.getServiceTypeId());
        }
        return null;
    }

    private java.util.List<Long> getServiceTypeIds(UpdateVisitScheduleRequest request) {
        if (request.getServiceTypeIds() != null && !request.getServiceTypeIds().isEmpty()) {
            return request.getServiceTypeIds();
        } else if (request.getServiceTypeId() != null) {
            return java.util.Collections.singletonList(request.getServiceTypeId());
        }
        return null;
    }

    @Transactional
    public void deleteVisitSchedule(Long vsId) {
        log.info("방문 요양 일정 삭제 시작 - vsId: {}", vsId);

        // 1. 관련된 요양일지 먼저 삭제 (논리삭제)
        int careLogsDeleted = careLogCommandMapper.deleteCareLogsByVsId(vsId);
        log.info("관련 요양일지 삭제 완료 - vsId: {}, 삭제된 개수: {}", vsId, careLogsDeleted);

        // 2. 방문 일정 삭제 (물리삭제)
        int deleted = visitScheduleCommandMapper.deleteVisitSchedule(vsId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 방문 요양 일정을 찾을 수 없습니다. vsId: " + vsId);
        }

        log.info("방문 요양 일정 삭제 완료 - vsId: {}", vsId);
    }
}
