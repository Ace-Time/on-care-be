package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.matching.command.repository.CreateVisitScheduleRepository;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitScheduleCreateService {

    private final CreateVisitScheduleRepository visitScheduleRepository;

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
    }
}