package org.ateam.oncare.customer.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.counsel.query.mapper.CounselQueryMapper;
import org.ateam.oncare.customer.command.dto.notifyRequest;
import org.ateam.oncare.customer.command.dto.notifyResponse;
import org.ateam.oncare.matching.query.mapper.MatchingQueryMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotifyFacadeService {
    private final NotificationCommandService notificationCommandService;
    private final MatchingQueryMapper matchingQueryMapper;
    private final CounselQueryMapper counselQueryMapper;

    public ResponseEntity<notifyResponse> notifyChurnRisk(Long beneficiaryId) {
        Long careWorkerId = matchingQueryMapper.selectAssignedCareWorkerId(beneficiaryId);
        Long lastCounselorId = counselQueryMapper.findLastCounselorId(beneficiaryId);

        Long targetId;

        if (lastCounselorId != null) {
            targetId = lastCounselorId;
        } else if (careWorkerId != null) {
            targetId = careWorkerId;
        } else {
            return ResponseEntity.ok(notifyResponse.builder()
                    .message("최근 상담사나 배정된 요양보호사가 존재하지 않는 회원입니다")
                    .build());
        }

        notificationCommandService.send(targetId, 15L);

        return ResponseEntity.ok(notifyResponse.builder()
                .message("알림이 발송되었습니다")
                .build());
    }

    public ResponseEntity<notifyResponse> notifyComplain(Long beneficiaryId) {
        Long careWorkerId = matchingQueryMapper.selectAssignedCareWorkerId(beneficiaryId);
        Long lastCounselorId = counselQueryMapper.findLastCounselorId(beneficiaryId);

        Long targetId;

        if (lastCounselorId != null) {
            targetId = lastCounselorId;
        } else if (careWorkerId != null) {
            targetId = careWorkerId;
        } else {
            return ResponseEntity.ok(notifyResponse.builder()
                    .message("최근 상담사나 배정된 요양보호사가 존재하지 않는 회원입니다")
                    .build());
        }

        notificationCommandService.send(targetId, 16L);

        return ResponseEntity.ok(notifyResponse.builder()
                .message("알림이 발송되었습니다")
                .build());
    }

    public ResponseEntity<notifyResponse> notifyTermination(Long beneficiaryId) {
        Long careWorkerId = matchingQueryMapper.selectAssignedCareWorkerId(beneficiaryId);
        Long lastCounselorId = counselQueryMapper.findLastCounselorId(beneficiaryId);

        Long targetId;

        if (lastCounselorId != null) {
            targetId = lastCounselorId;
        } else if (careWorkerId != null) {
            targetId = careWorkerId;
        } else {
            return ResponseEntity.ok(notifyResponse.builder()
                    .message("최근 상담사나 배정된 요양보호사가 존재하지 않는 회원입니다")
                    .build());
        }

        notificationCommandService.send(targetId, 17L);

        return ResponseEntity.ok(notifyResponse.builder()
                .message("알림이 발송되었습니다")
                .build());
    }

    public ResponseEntity<notifyResponse> notifyExpiration(Long beneficiaryId) {
        Long careWorkerId = matchingQueryMapper.selectAssignedCareWorkerId(beneficiaryId);
        Long lastCounselorId = counselQueryMapper.findLastCounselorId(beneficiaryId);

        Long targetId;

        if (lastCounselorId != null) {
            targetId = lastCounselorId;
        } else if (careWorkerId != null) {
            targetId = careWorkerId;
        } else {
            return ResponseEntity.ok(notifyResponse.builder()
                    .message("최근 상담사나 배정된 요양보호사가 존재하지 않는 회원입니다")
                    .build());
        }

        notificationCommandService.send(targetId, 18L);

        return ResponseEntity.ok(notifyResponse.builder()
                .message("알림이 발송되었습니다")
                .build());
    }

    public ResponseEntity<notifyResponse> notifyRental(Long beneficiaryId) {
        Long careWorkerId = matchingQueryMapper.selectAssignedCareWorkerId(beneficiaryId);
        Long lastCounselorId = counselQueryMapper.findLastCounselorId(beneficiaryId);

        Long targetId;

        if (lastCounselorId != null) {
            targetId = lastCounselorId;
        } else if (careWorkerId != null) {
            targetId = careWorkerId;
        } else {
            return ResponseEntity.ok(notifyResponse.builder()
                    .message("최근 상담사나 배정된 요양보호사가 존재하지 않는 회원입니다")
                    .build());
        }

        notificationCommandService.send(targetId, 18L);

        return ResponseEntity.ok(notifyResponse.builder()
                .message("알림이 발송되었습니다")
                .build());
    }
}
