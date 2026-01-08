package org.ateam.oncare.schedule.command;

import org.ateam.oncare.schedule.command.dto.ConfirmedScheduleMemoUpsertRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedScheduleMemoUpsertResponse;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.ateam.oncare.schedule.command.service.ConfirmedScheduleMemoCommandService;
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
class ConfirmedScheduleMemoCommandServiceTest {

    @Autowired private ConfirmedScheduleMemoCommandService service;
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
    void upsert_trim_and_save() {
        // 메모(note) 앞/뒤 공백이 trim 처리되어 저장되는지 + 응답에도 trim된 값이 내려오는지 테스트
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        VisitSchedule saved = saveFutureVisitSchedule(start, end);

        ConfirmedScheduleMemoUpsertRequest req = new ConfirmedScheduleMemoUpsertRequest();
        req.setVsId(saved.getVsId());
        req.setNote("   hello memo   ");

        ConfirmedScheduleMemoUpsertResponse res = service.upsert(req);

        assertNotNull(res);
        assertEquals(saved.getVsId(), res.getVsId());
        assertEquals("hello memo", res.getNote());

        VisitSchedule reloaded = visitScheduleRepository.findById(saved.getVsId()).orElseThrow();
        assertEquals("hello memo", reloaded.getNote());
    }

    @Test
    void upsert_null_note_becomes_empty_string() {
        // 메모(note)가 null로 들어오면 ""(빈 문자열)로 저장/응답되는지 테스트
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        VisitSchedule saved = saveFutureVisitSchedule(start, end);

        ConfirmedScheduleMemoUpsertRequest req = new ConfirmedScheduleMemoUpsertRequest();
        req.setVsId(saved.getVsId());
        req.setNote(null);

        ConfirmedScheduleMemoUpsertResponse res = service.upsert(req);

        assertNotNull(res);
        assertEquals("", res.getNote());

        VisitSchedule reloaded = visitScheduleRepository.findById(saved.getVsId()).orElseThrow();
        assertEquals("", reloaded.getNote());
    }

    @Test
    void upsert_vsId_required() {
        // vsId가 없으면(필수값 누락) IllegalArgumentException이 발생하는지 테스트
        ConfirmedScheduleMemoUpsertRequest req = new ConfirmedScheduleMemoUpsertRequest();
        req.setVsId(null);
        req.setNote("x");

        assertThrows(IllegalArgumentException.class, () -> service.upsert(req));
    }
}