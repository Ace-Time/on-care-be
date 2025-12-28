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

        String safeHtml = request.getHtmlCode();

        Optional<PotentialStage> existingStage = potentialStageRepository.findByPotentialCustomerIdAndStage(
                potentialId.longValue(),
                request.getStage()
        );

        if (existingStage.isPresent()) {
            // [Update] 이미 저장했던 단계라면 내용만 덮어쓰기 (수정)
            PotentialStage stageEntity = existingStage.get();
            stageEntity.setHtmlCode(safeHtml);
            stageEntity.setProcessTime(LocalDateTime.now());
            // 필요한 필드 업데이트...
        } else {
            // [Insert] 처음 저장하는 단계라면 새로 만들기
            PotentialStage newStage = PotentialStage.builder()
                    .potentialCustomerId(potentialId.longValue())
                    .stage(request.getStage())
                    .htmlCode(safeHtml)
                    .processStatus("P")
                    .processTime(LocalDateTime.now())
                    .month(LocalDateTime.now())
                    .build();
            potentialStageRepository.save(newStage);
        }
    }

    public Map<Integer, StageData> findStageDataByPotentialId(Long potentialId) {
        List<PotentialStage> stages = potentialStageRepository.findAllByPotentialCustomerId(potentialId);
        return stages.stream().collect(Collectors.toMap(
                PotentialStage::getStage,
                entity -> StageData.builder()
                        .processStatus(entity.getProcessStatus())
                        .processTime(entity.getProcessTime())
                        .month(entity.getMonth())
                        .htmlCode(entity.getHtmlCode())
                        .build()
        ));
    }
}
