package org.ateam.oncare.schedule.command;

import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateResponse;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.ateam.oncare.schedule.command.service.ConfirmedVisitScheduleCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ConfirmedVisitScheduleCommandServiceTest {

    @Autowired private ConfirmedVisitScheduleCommandService service;
    @Autowired private VisitScheduleRepository visitScheduleRepository;

    private static final Long TEST_BENEFICIARY_ID = 1L;
    private static final Long TEST_CARE_WORKER_ID = 1L;
    private static final Long TEST_SERVICE_TYPE_ID = 1L;

    private VisitSchedule saveFutureVisitSchedule(LocalDateTime start, LocalDateTime end) {
        VisitSchedule vs = new VisitSchedule();
        vs.setBeneficiaryId(TEST_BENEFICIARY_ID);
        vs.setCareWorkerId(TEST_CARE_WORKER_ID);
        vs.setServiceTypeId(TEST_SERVICE_TYPE_ID);
        vs.setIsLogWritten(false);
        vs.setVisitStatus(VisitSchedule.VisitStatus.SCHEDULED);
        vs.setStartDt(start);
        vs.setEndDt(end);
        vs.setNote("old");
        return visitScheduleRepository.save(vs);
    }

    @Test
    void updateTime_success() {
        // 정상 케이스: 미래 일정의 start/end 시간을 변경하면 DB에 반영되고 응답 DTO가 변경값을 담아 반환되는지 테스트
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0);
        VisitSchedule saved = saveFutureVisitSchedule(start, end);

        ConfirmedVisitScheduleTimeUpdateRequest req = new ConfirmedVisitScheduleTimeUpdateRequest();
        LocalDateTime newStart = start.plusHours(1);
        LocalDateTime newEnd = end.plusHours(1);
        req.setStartDt(newStart);
        req.setEndDt(newEnd);

        ConfirmedVisitScheduleTimeUpdateResponse res = service.updateTime(saved.getVsId(), req);

        assertNotNull(res);
        assertEquals(saved.getVsId(), res.getVsId());
        assertEquals(newStart, res.getStartDt());
        assertEquals(newEnd, res.getEndDt());

        VisitSchedule reloaded = visitScheduleRepository.findById(saved.getVsId()).orElseThrow();
        assertEquals(newStart, reloaded.getStartDt());
        assertEquals(newEnd, reloaded.getEndDt());
    }

    @Test
    void updateTime_reject_when_end_not_after_start() {
        // 검증 케이스: endDt가 startDt 이후가 아니면(같거나 이전) IllegalArgumentException이 발생하는지 테스트
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0);
        VisitSchedule saved = saveFutureVisitSchedule(start, end);

        ConfirmedVisitScheduleTimeUpdateRequest req = new ConfirmedVisitScheduleTimeUpdateRequest();
        req.setStartDt(start);
        req.setEndDt(start);

        assertThrows(IllegalArgumentException.class, () -> service.updateTime(saved.getVsId(), req));
    }

    @Test
    void updateTime_reject_when_start_is_before_today() {
        // 검증 케이스: startDt 날짜가 오늘 이전이면(과거 날짜) IllegalStateException이 발생하는지 테스트
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0);
        VisitSchedule saved = saveFutureVisitSchedule(start, end);

        ConfirmedVisitScheduleTimeUpdateRequest req = new ConfirmedVisitScheduleTimeUpdateRequest();
        req.setStartDt(LocalDateTime.now().minusDays(1));
        req.setEndDt(LocalDateTime.now().minusDays(1).plusHours(1));

        assertThrows(IllegalStateException.class, () -> service.updateTime(saved.getVsId(), req));
    }

    @Test
    void delete_success_when_not_in_progress_or_done() {
        // 정상 케이스: 진행/완료가 아닌 일정은 삭제되며, DB에서 조회되지 않는지 테스트
        LocalDateTime start = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0).withSecond(0).withNano(0);
        VisitSchedule saved = saveFutureVisitSchedule(start, end);

        service.delete(saved.getVsId());

        assertTrue(visitScheduleRepository.findById(saved.getVsId()).isEmpty());
    }
}