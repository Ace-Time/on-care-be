package org.ateam.oncare.schedule.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.ConfirmedScheduleMemoUpsertRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedScheduleMemoUpsertResponse;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmedScheduleMemoCommandService {

    private final VisitScheduleRepository visitScheduleRepository;

    @Transactional
    public ConfirmedScheduleMemoUpsertResponse upsert(ConfirmedScheduleMemoUpsertRequest request) {
        if (request == null || request.getVsId() == null) {
            throw new IllegalArgumentException("vsId is required");
        }

        VisitSchedule vs = visitScheduleRepository.findById(request.getVsId())
                .orElseThrow(() -> new IllegalArgumentException("visit_schedule not found: vsId=" + request.getVsId()));

        String note = request.getNote();
        if (note == null) note = "";
        note = note.trim();

        vs.setNote(note);
        visitScheduleRepository.save(vs);

        return ConfirmedScheduleMemoUpsertResponse.builder()
                .vsId(vs.getVsId())
                .note(vs.getNote() == null ? "" : vs.getNote())
                .build();
    }
}