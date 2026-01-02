package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.matching.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.matching.command.repository.CreateVisitScheduleRepository;
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
@Transactional
public class VisitScheduleCreateService {

    private final CreateVisitScheduleRepository visitScheduleRepository;

    private final NotificationCommandService notificationCommandService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final CareWorkerRepository careWorkerRepository;

    private static final String TEMPLATE_TYPE_VISIT_SCHEDULE_CREATED = "VISIT_SCHEDULE_CREATED";

    public void create(CreateVisitScheduleRequest req) {

        VisitSchedule visitSchedule = new VisitSchedule();
        visitSchedule.setBeneficiaryId(req.getBeneficiaryId());
        visitSchedule.setCareWorkerId(req.getCareWorkerId());
        visitSchedule.setServiceTypeId(req.getServiceTypeId());
        visitSchedule.setStartDt(req.getStartDt());
        visitSchedule.setEndDt(req.getEndDt());
        visitSchedule.setVisitStatus(VisitSchedule.VisitStatus.SCHEDULED);
        visitSchedule.setIsLogWritten(false);
        visitSchedule.setNote(req.getNote());

        visitScheduleRepository.save(visitSchedule);

        notifyVisitScheduleCreated(
                req.getCareWorkerId(),
                req.getBeneficiaryId(),
                req.getStartDt(),
                req.getEndDt()
        );
    }

    private void notifyVisitScheduleCreated(
            Long careWorkerId,
            Long beneficiaryId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        if (careWorkerId == null || beneficiaryId == null) return;

        Long receiverEmployeeId = resolveReceiverEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = getActiveTemplate(TEMPLATE_TYPE_VISIT_SCHEDULE_CREATED);

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);
        vars.put("date", formatDate(start));
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