package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.command.dto.StageData;
import org.ateam.oncare.counsel.command.dto.Subscription;
import org.ateam.oncare.counsel.command.entity.PotentialStage;
import org.ateam.oncare.counsel.command.repository.PotentialStageRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PotentialStageService {
    private final PotentialStageRepository potentialStageRepository;


    public void registPotentialStage(Subscription request, BigInteger potentialId) {

        Map<String, Object> stageData = request.getStageData();

        Optional<PotentialStage> existingStage = potentialStageRepository.findByPotentialCustomerIdAndStage(
                potentialId.longValue(),
                request.getStage()
        );

        if (existingStage.isPresent()) {
            // [Update] 이미 저장했던 단계라면 내용만 덮어쓰기 (수정)
            PotentialStage stageEntity = existingStage.get();
            stageEntity.setStageData(stageData); // JSON 데이터 업데이트
            stageEntity.setProcessTime(LocalDateTime.now());
        } else {
            // [Insert] 처음 저장하는 단계라면 새로 만들기
            PotentialStage newStage = PotentialStage.builder()
                    .potentialCustomerId(potentialId.longValue())
                    .stage(request.getStage())
                    .stageData(stageData) // JSON 데이터 저장
                    .processStatus("P")
                    .processTime(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();
            potentialStageRepository.save(newStage);
        }
    }

    public Map<Integer, StageData> findStageDataByPotentialId(Long potentialId) {
        List<PotentialStage> stages = potentialStageRepository.findAllByPotentialCustomerId(potentialId);

        return stages.stream().collect(Collectors.toMap(
                PotentialStage::getStage,
                entity -> StageData.builder()
                        .stage(entity.getStage())
                        .processStatus(entity.getProcessStatus())
                        .processTime(entity.getProcessTime())
                        .createdAt(entity.getCreatedAt())
                        .stageData(entity.getStageData()) // [변경] Map 데이터 매핑
                        .potentialId(BigInteger.valueOf(entity.getPotentialCustomerId()))
                        .build()
        ));
    }

    public void updateStageData(StageData request) {
        int stage = request.getStage();
        long potentialId = request.getPotentialId().longValue();

        // [변경] HTML 파싱 로직 제거 후 JSON Map 사용
        Map<String, Object> stageDataMap = request.getStageData();
        String processStatus = request.getProcessStatus();

        PotentialStage potentialStage = potentialStageRepository.findByPotentialCustomerIdAndStage(potentialId, stage)
                .orElseThrow(() -> new IllegalArgumentException("해당 단계 데이터를 찾을 수 없습니다. potentialId=" + potentialId));

        // Entity 편의 메서드를 통해 업데이트 (시간 갱신 로직 포함됨)
        potentialStage.updateStageData(stageDataMap, processStatus);
    }
}
