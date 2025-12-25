package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CreatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.dto.UpdatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.mapper.PersonalScheduleCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalScheduleCommandService {

    private final PersonalScheduleCommandMapper personalScheduleCommandMapper;

    @Transactional
    public void createPersonalSchedule(Long careWorkerId, CreatePersonalScheduleRequest request) {
        log.info("개인 일정 작성 시작 - careWorkerId: {}, title: {}", careWorkerId, request.getTitle());
        int inserted = personalScheduleCommandMapper.insertPersonalSchedule(careWorkerId, request);

        if (inserted == 0) {
            throw new IllegalStateException("개인 일정 작성에 실패했습니다.");
        }

        log.info("개인 일정 작성 완료");
    }

    @Transactional
    public void updatePersonalSchedule(Long psId, UpdatePersonalScheduleRequest request) {
        log.info("개인 일정 수정 시작 - psId: {}", psId);
        int updated = personalScheduleCommandMapper.updatePersonalSchedule(psId, request);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 개인 일정을 찾을 수 없습니다. psId: " + psId);
        }

        log.info("개인 일정 수정 완료 - psId: {}", psId);
    }

    @Transactional
    public void deletePersonalSchedule(Long psId) {
        log.info("개인 일정 삭제 시작 - psId: {}", psId);
        int deleted = personalScheduleCommandMapper.deletePersonalSchedule(psId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 개인 일정을 찾을 수 없습니다. psId: " + psId);
        }

        log.info("개인 일정 삭제 완료 - psId: {}", psId);
    }
}
