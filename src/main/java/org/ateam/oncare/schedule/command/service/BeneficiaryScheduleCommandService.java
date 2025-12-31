package org.ateam.oncare.schedule.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.entity.BeneficiarySchedule;
import org.ateam.oncare.matching.command.repository.MatchingRepository;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleCreateRequest;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleResponse;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleUpdateRequest;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.ateam.oncare.schedule.command.repository.BeneficiaryScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BeneficiaryScheduleCommandService {

    private final BeneficiaryScheduleRepository beneficiaryScheduleRepository;
    private final MatchingRepository matchingRepository;

    public BeneficiaryScheduleResponse create(BeneficiaryScheduleCreateRequest req) {
        validateBasic(req.getDay(), req.getStartTime(), req.getEndTime());

        boolean duplicated = beneficiaryScheduleRepository
                .existsByBeneficiaryIdAndServiceTypeIdAndDayAndStartTimeAndEndTime(
                        req.getBeneficiaryId(),
                        req.getServiceTypeId(),
                        req.getDay(),
                        req.getStartTime(),
                        req.getEndTime()
                );

        if (duplicated) {
            throw new IllegalStateException("이미 동일한 희망 시간이 등록되어 있습니다.");
        }

        Matching active = matchingRepository
                .findByBeneficiaryIdAndStatus(req.getBeneficiaryId(), "Y")
                .orElse(null);

        if (active != null) {
            Long careWorkerId = active.getCareWorkerId();

            boolean overlap = beneficiaryScheduleRepository.existsOverlapWithOtherBeneficiaries(
                    careWorkerId,
                    req.getBeneficiaryId(),
                    req.getDay(),
                    req.getStartTime(),
                    req.getEndTime(),
                    null
            );

            if (overlap) {
                throw new IllegalStateException("추가/수정된 시간대에 담당 요양보호사의 일정이 존재합니다.");
            }
        }

        BeneficiarySchedule bs = new BeneficiarySchedule();
        bs.setBeneficiaryId(req.getBeneficiaryId());
        bs.setServiceTypeId(req.getServiceTypeId());
        bs.setDay(req.getDay());
        bs.setStartTime(req.getStartTime());
        bs.setEndTime(req.getEndTime());

        BeneficiarySchedule saved = beneficiaryScheduleRepository.save(bs);
        return toResponse(saved);
    }

    public BeneficiaryScheduleResponse update(Long id, BeneficiaryScheduleUpdateRequest req) {
        validateBasic(req.getDay(), req.getStartTime(), req.getEndTime());

        BeneficiarySchedule bs = beneficiaryScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("희망 주기를 찾을 수 없습니다. id=" + id));

        boolean duplicated = beneficiaryScheduleRepository
                .existsByBeneficiaryIdAndServiceTypeIdAndDayAndStartTimeAndEndTimeAndIdNot(
                        bs.getBeneficiaryId(),
                        req.getServiceTypeId(),
                        req.getDay(),
                        req.getStartTime(),
                        req.getEndTime(),
                        id
                );

        if (duplicated) {
            throw new IllegalStateException("이미 동일한 희망 시간이 등록되어 있습니다.");
        }

        Matching active = matchingRepository
                .findByBeneficiaryIdAndStatus(bs.getBeneficiaryId(), "Y")
                .orElse(null);

        if (active != null) {
            Long careWorkerId = active.getCareWorkerId();

            boolean overlap = beneficiaryScheduleRepository.existsOverlapWithOtherBeneficiaries(
                    careWorkerId,
                    bs.getBeneficiaryId(),
                    req.getDay(),
                    req.getStartTime(),
                    req.getEndTime(),
                    id
            );

            if (overlap) {
                throw new IllegalStateException("추가/수정된 시간대에 담당 요양보호사의 일정이 존재합니다.");
            }
        }

        bs.setServiceTypeId(req.getServiceTypeId());
        bs.setDay(req.getDay());
        bs.setStartTime(req.getStartTime());
        bs.setEndTime(req.getEndTime());

        return toResponse(bs);
    }

    public void delete(Long id) {
        BeneficiarySchedule bs = beneficiaryScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("희망 주기를 찾을 수 없습니다. id=" + id));

        long cnt = beneficiaryScheduleRepository.countByBeneficiaryId(bs.getBeneficiaryId());
        if (cnt <= 1) {
            throw new IllegalStateException("희망 시간은 최소 1개 이상 유지되어야 합니다.");
        }

        beneficiaryScheduleRepository.deleteById(id);
    }

    private void validateBasic(Integer day, LocalTime startTime, LocalTime endTime) {
        if (day == null || day < 1 || day > 7) {
            throw new IllegalArgumentException("day는 1~7이어야 합니다.");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime/endTime은 필수입니다.");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("startTime은 endTime보다 빨라야 합니다.");
        }
    }

    private BeneficiaryScheduleResponse toResponse(BeneficiarySchedule bs) {
        return new BeneficiaryScheduleResponse(
                bs.getId(),
                bs.getBeneficiaryId(),
                bs.getServiceTypeId(),
                bs.getDay(),
                bs.getStartTime(),
                bs.getEndTime()
        );
    }
}