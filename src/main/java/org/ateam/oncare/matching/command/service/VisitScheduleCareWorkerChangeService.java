package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.matching.command.dto.ChangeConfirmedVisitCareWorkerRequest;
import org.ateam.oncare.matching.command.repository.VisitScheduleCareWorkerChangeRepository;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VisitScheduleCareWorkerChangeService {

    private final VisitScheduleCareWorkerChangeRepository visitScheduleCareWorkerChangeRepository;
    private final CareWorkerRepository careWorkerRepository;
    private final NotificationCommandService notificationCommandService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    private static final String TEMPLATE_TYPE_VISIT_SCHEDULE_CANCELED = "VISIT_SCHEDULE_CANCELED";
    private static final String TEMPLATE_TYPE_VISIT_SCHEDULE_UPDATED = "VISIT_SCHEDULE_UPDATED";

    @Transactional
    public void changeCareWorker(Long vsId, ChangeConfirmedVisitCareWorkerRequest request) {

        VisitSchedule visitSchedule = visitScheduleCareWorkerChangeRepository.findById(vsId)
                .orElseThrow(() -> new IllegalArgumentException("방문일정이 존재하지 않습니다. vsId=" + vsId));

        Long newCareWorkerId = request.getCareWorkerId();
        if (newCareWorkerId == null) {
            throw new IllegalArgumentException("careWorkerId는 필수입니다.");
        }

        careWorkerRepository.findById(newCareWorkerId)
                .orElseThrow(() -> new IllegalArgumentException("요양보호사가 존재하지 않습니다. careWorkerId=" + newCareWorkerId));

        Long oldCareWorkerId = visitSchedule.getCareWorkerId();
        Long beneficiaryId = visitSchedule.getBeneficiaryId();

        if (oldCareWorkerId != null && oldCareWorkerId.equals(newCareWorkerId)) {
            return;
        }

        visitSchedule.setCareWorkerId(newCareWorkerId);

        if (oldCareWorkerId != null) {
            notifyVisitScheduleCanceled(
                    oldCareWorkerId,
                    beneficiaryId,
                    visitSchedule.getStartDt()
            );
        }

        notifyVisitScheduleUpdated(
                newCareWorkerId,
                beneficiaryId,
                visitSchedule.getStartDt(),
                visitSchedule.getStartDt(),
                visitSchedule.getStartDt(),
                visitSchedule.getEndDt()
        );
    }

    private void notifyVisitScheduleCanceled(Long careWorkerId, Long beneficiaryId, LocalDateTime date) {
        Long receiverEmployeeId = resolveReceiverEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = getActiveTemplate(TEMPLATE_TYPE_VISIT_SCHEDULE_CANCELED);

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);
        vars.put("date", formatDate(date));

        notificationCommandService.sendCustom(
                receiverEmployeeId,
                applyTemplate(template.getTitle(), vars),
                applyTemplate(template.getContent(), vars),
                template.getTemplateType(),
                template.getSeverity()
        );
    }

    private void notifyVisitScheduleUpdated(
            Long careWorkerId,
            Long beneficiaryId,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            LocalDateTime start,
            LocalDateTime end
    ) {
        Long receiverEmployeeId = resolveReceiverEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = getActiveTemplate(TEMPLATE_TYPE_VISIT_SCHEDULE_UPDATED);

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);
        vars.put("fromDate", formatDate(fromDate));
        vars.put("toDate", formatDate(toDate));
        vars.put("start", formatTime(start));
        vars.put("end", formatTime(end));

        notificationCommandService.sendCustom(
                receiverEmployeeId,
                applyTemplate(template.getTitle(), vars),
                applyTemplate(template.getContent(), vars),
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

    private String formatDate(LocalDateTime dt) {
        return dt == null ? "-" : dt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String formatTime(LocalDateTime dt) {
        return dt == null ? "-" : dt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}