package org.ateam.oncare.schedule.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateResponse;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfirmedVisitScheduleCommandService {

    private final VisitScheduleRepository visitScheduleRepository;

    // === 알림(추가) ===
    private final NotificationCommandService notificationCommandService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final CareWorkerRepository careWorkerRepository;

    private static final String TEMPLATE_TYPE_VISIT_SCHEDULE_CANCELED = "VISIT_SCHEDULE_CANCELED";
    private static final String TEMPLATE_TYPE_VISIT_SCHEDULE_UPDATED = "VISIT_SCHEDULE_UPDATED";

    @Transactional
    public ConfirmedVisitScheduleTimeUpdateResponse updateTime(
            Long vsId,
            ConfirmedVisitScheduleTimeUpdateRequest request
    ) {
        if (vsId == null) throw new IllegalArgumentException("vsId is required");
        if (request == null) throw new IllegalArgumentException("request is required");

        LocalDateTime startDt = request.getStartDt();
        LocalDateTime endDt = request.getEndDt();

        if (startDt == null || endDt == null) {
            throw new IllegalArgumentException("startDt/endDt are required");
        }
        if (!endDt.isAfter(startDt)) {
            throw new IllegalArgumentException("endDt must be after startDt");
        }

        LocalDate today = LocalDate.now();
        if (startDt.toLocalDate().isBefore(today)) {
            throw new IllegalStateException("일정 수정은 현재 시각 이후로 가능합니다.");
        }

        VisitSchedule vs = visitScheduleRepository.findById(vsId)
                .orElseThrow(() ->
                        new IllegalArgumentException("visit_schedule not found: vsId=" + vsId)
                );

        String status = vs.getVisitStatus() == null ? "" : vs.getVisitStatus().name();
        if ("IN_PROGRESS".equals(status) || "DONE".equals(status)) {
            throw new IllegalStateException("진행/완료 일정은 시간 변경이 불가합니다.");
        }

        Long careWorkerId = vs.getCareWorkerId();
        Long beneficiaryId = vs.getBeneficiaryId();

        if (careWorkerId != null) {
            boolean overlappedCareWorker =
                    visitScheduleRepository.existsOverlapForCareWorker(
                            careWorkerId,
                            vsId,
                            startDt,
                            endDt
                    );
            if (overlappedCareWorker) {
                throw new IllegalStateException(
                        "해당 시간에 요양보호사가 이미 배치되어 있어 수정할 수 없습니다."
                );
            }
        }

        if (beneficiaryId != null) {
            boolean overlappedBeneficiary =
                    visitScheduleRepository.existsOverlapForBeneficiary(
                            beneficiaryId,
                            vsId,
                            startDt,
                            endDt
                    );
            if (overlappedBeneficiary) {
                throw new IllegalStateException(
                        "해당 시간에 수급자가 이미 배치되어 있어 수정할 수 없습니다."
                );
            }
        }

        vs.setStartDt(startDt);
        vs.setEndDt(endDt);

        VisitSchedule saved = visitScheduleRepository.save(vs);

        if (careWorkerId != null && beneficiaryId != null) {
            notifyVisitScheduleUpdated(
                    careWorkerId,
                    beneficiaryId,
                    saved.getStartDt(),
                    saved.getStartDt(),
                    saved.getStartDt(),
                    saved.getEndDt()
            );
        }

        return ConfirmedVisitScheduleTimeUpdateResponse.builder()
                .vsId(saved.getVsId())
                .startDt(saved.getStartDt())
                .endDt(saved.getEndDt())
                .build();
    }

    @Transactional
    public void delete(Long vsId) {
        if (vsId == null) throw new IllegalArgumentException("vsId is required");

        VisitSchedule vs = visitScheduleRepository.findById(vsId)
                .orElseThrow(() ->
                        new IllegalArgumentException("visit_schedule not found: vsId=" + vsId)
                );

        String status = vs.getVisitStatus() == null ? "" : vs.getVisitStatus().name();
        if ("IN_PROGRESS".equals(status) || "DONE".equals(status)) {
            throw new IllegalStateException("진행/완료 일정은 삭제가 불가합니다.");
        }

        Long careWorkerId = vs.getCareWorkerId();
        Long beneficiaryId = vs.getBeneficiaryId();
        LocalDateTime date = vs.getStartDt();

        visitScheduleRepository.delete(vs);

        if (careWorkerId != null && beneficiaryId != null) {
            notifyVisitScheduleCanceled(careWorkerId, beneficiaryId, date);
        }
    }

    private void notifyVisitScheduleCanceled(Long careWorkerId, Long beneficiaryId, LocalDateTime date) {
        Long receiverEmployeeId = resolveReceiverEmployeeId(careWorkerId);
        String beneficiaryName = resolveBeneficiaryName(beneficiaryId);

        NotificationTemplate template = getActiveTemplate(TEMPLATE_TYPE_VISIT_SCHEDULE_CANCELED);

        Map<String, String> vars = new HashMap<>();
        vars.put("beneficiaryName", beneficiaryName);
        vars.put("date", formatDate(date));

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
        List<NotificationTemplate> list =
                notificationTemplateRepository.findByTemplateTypeAndIsActive(templateType, 1);
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
        if (dt == null) return "-";
        return dt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String formatTime(LocalDateTime dt) {
        if (dt == null) return "-";
        return dt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}