package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.matching.command.dto.ChangeMatchingCareWorkerRequest;
import org.ateam.oncare.matching.command.repository.MatchingCareWorkerChangeRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchingCareWorkerChangeService {

    private final MatchingCareWorkerChangeRepository matchingCareWorkerChangeRepository;
    private final CareWorkerRepository careWorkerRepository;

    // === 알림(추가) ===
    private final NotificationCommandService notificationCommandService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    private static final String TEMPLATE_TYPE_MATCHING_CANCELED = "MATCHING_CANCELED";
    private static final String TEMPLATE_TYPE_MATCHING_ASSIGNED = "MATCHING_ASSIGNED";

    @Transactional
    public void changeCareWorker(Long matchingId, ChangeMatchingCareWorkerRequest request) {
        Matching matching = matchingCareWorkerChangeRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭이 존재하지 않습니다. matchingId=" + matchingId));

        Long newCareWorkerId = request.getCareWorkerId();
        if (newCareWorkerId == null) {
            throw new IllegalArgumentException("careWorkerId는 필수입니다.");
        }

        if (!careWorkerRepository.existsById(newCareWorkerId)) {
            throw new IllegalArgumentException("요양보호사가 존재하지 않습니다. careWorkerId=" + newCareWorkerId);
        }

        Long oldCareWorkerId = matching.getCareWorkerId();
        Long beneficiaryId = matching.getBeneficiaryId();

        // 변경이 없으면 아무것도 안 함 (알림도 X)
        if (oldCareWorkerId != null && oldCareWorkerId.equals(newCareWorkerId)) {
            return;
        }

        // FK 값만 변경
        matching.setCareWorkerId(newCareWorkerId);

        // === 알림 ===
        // 1) 기존 요양보호사에게 "배정 취소"
        if (oldCareWorkerId != null) {
            notifyMatchingCanceled(oldCareWorkerId, beneficiaryId);
        }
        // 2) 새 요양보호사에게 "담당요양사 배정"
        notifyMatchingAssigned(newCareWorkerId, beneficiaryId);
    }

    private void notifyMatchingCanceled(Long careWorkerId, Long beneficiaryId) {
        Long receiverEmployeeId = resolveReceiverEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = getActiveTemplate(TEMPLATE_TYPE_MATCHING_CANCELED);

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);

        String title = applyTemplate(template.getTitle(), vars);
        String content = applyTemplate(template.getContent(), vars);

        notificationCommandService.sendCustom(
                receiverEmployeeId,
                title,
                content,
                template.getTemplateType(),
                template.getSeverity()
        );
    }

    private void notifyMatchingAssigned(Long careWorkerId, Long beneficiaryId) {
        Long receiverEmployeeId = resolveReceiverEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = getActiveTemplate(TEMPLATE_TYPE_MATCHING_ASSIGNED);

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);

        String title = applyTemplate(template.getTitle(), vars);
        String content = applyTemplate(template.getContent(), vars);

        notificationCommandService.sendCustom(
                receiverEmployeeId,
                title,
                content,
                template.getTemplateType(),
                template.getSeverity()
        );
    }

    private NotificationTemplate getActiveTemplate(String templateType) {
        List<NotificationTemplate> list = notificationTemplateRepository.findByTemplateTypeAndIsActive(templateType, 1);
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("활성 템플릿이 없습니다: " + templateType);
        }
        return list.get(0);
    }

    private String applyTemplate(String template, Map<String, String> vars) {
        String result = template;
        for (Map.Entry<String, String> e : vars.entrySet()) {
            result = result.replace("{" + e.getKey() + "}", e.getValue());
        }
        return result;
    }

    private Long resolveReceiverEmployeeId(Long careWorkerId) {
        CareWorker cw = careWorkerRepository.findById(careWorkerId)
                .orElseThrow(() -> new IllegalArgumentException("요양보호사를 찾을 수 없습니다. id=" + careWorkerId));
        return Long.valueOf(cw.getEmployeeId());
    }

    private String resolveBeneficiaryName(Long beneficiaryId) {
        Beneficiary b = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new IllegalArgumentException("수급자를 찾을 수 없습니다. id=" + beneficiaryId));
        return b.getName();
    }
}