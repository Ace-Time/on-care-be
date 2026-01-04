package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.command.dto.StageData;
import org.ateam.oncare.counsel.command.dto.Subscription;
import org.ateam.oncare.counsel.command.entity.PotentialStage;
import org.ateam.oncare.counsel.command.repository.PotentialStageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PotentialStageService {

    private final PotentialStageRepository potentialStageRepository;

    /**
     * 신규 잠재고객의 가입상담 시작 시 1단계 초기화
     */
    @Transactional
    public void registPotentialStage(Subscription request, BigInteger potentialId) {
        // 1단계 초기 데이터 생성
        PotentialStage stage1 = PotentialStage.builder()
                .stage(1)
                .processStatus("P")  // 진행중
                .createdAt(LocalDateTime.now())
                .stageData(new HashMap<>())  // 빈 데이터로 시작
                .potentialCustomerId(potentialId.longValue())
                .build();

        potentialStageRepository.save(stage1);
        log.info("잠재고객 {} 의 1단계가 초기화되었습니다.", potentialId);
    }

    /**
     * 단계별 데이터 저장/업데이트
     */
    @Transactional
    public void updateStageData(StageData stageData) {
        Long potentialCustomerId = stageData.getPotentialId();
        Integer stage = stageData.getStage();

        // 해당 단계의 기존 데이터 조회
        PotentialStage potentialStage = potentialStageRepository
                .findByPotentialCustomerIdAndStage(potentialCustomerId, stage)
                .orElseGet(() -> {
                    // 없으면 새로 생성
                    return PotentialStage.builder()
                            .stage(stage)
                            .processStatus("P")
                            .createdAt(LocalDateTime.now())
                            .potentialCustomerId(potentialCustomerId)
                            .stageData(new HashMap<>())
                            .build();
                });

        // 데이터 업데이트
        potentialStage.updateStageData(
                stageData.getStageData(),
                stageData.getProcessStatus() != null ? stageData.getProcessStatus() : "P"
        );

        potentialStageRepository.save(potentialStage);
        log.info("잠재고객 {} 의 {}단계 데이터가 저장되었습니다. 상태: {}",
                potentialCustomerId, stage, potentialStage.getProcessStatus());
    }

    /**
     * 특정 잠재고객의 모든 단계 데이터 조회
     */
    @Transactional(readOnly = true)
    public Map<Integer, StageData> findStageDataByPotentialId(Long potentialCustomerId) {
        List<PotentialStage> stages = potentialStageRepository
                .findByPotentialCustomerIdOrderByStageAsc(potentialCustomerId);

        return stages.stream()
                .collect(Collectors.toMap(
                        PotentialStage::getStage,
                        stage -> StageData.builder()
                                .stage(stage.getStage())
                                .potentialId(stage.getPotentialCustomerId())
                                .processStatus(stage.getProcessStatus())
                                .processTime(stage.getProcessTime())
                                .stageData(stage.getStageData())
                                .build()
                ));
    }

    /**
     * 특정 단계의 데이터만 조회
     */
    @Transactional(readOnly = true)
    public StageData findStageData(Long potentialCustomerId, Integer stage) {
        return potentialStageRepository
                .findByPotentialCustomerIdAndStage(potentialCustomerId, stage)
                .map(ps -> StageData.builder()
                        .stage(ps.getStage())
                        .potentialId(ps.getPotentialCustomerId())
                        .processStatus(ps.getProcessStatus())
                        .processTime(ps.getProcessTime())
                        .stageData(ps.getStageData())
                        .build())
                .orElse(null);
    }

    /**
     * 모든 단계가 완료되었는지 확인
     */
    @Transactional(readOnly = true)
    public boolean isAllStagesCompleted(Long potentialCustomerId) {
        List<PotentialStage> stages = potentialStageRepository
                .findByPotentialCustomerIdOrderByStageAsc(potentialCustomerId);

        // 1~4단계가 모두 존재하고 모두 완료 상태(F)인지 확인
        if (stages.size() < 4) {
            return false;
        }

        return stages.stream()
                .allMatch(stage -> "F".equals(stage.getProcessStatus()));
    }
}
