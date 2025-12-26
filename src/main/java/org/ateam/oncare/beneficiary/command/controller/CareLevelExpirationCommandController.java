package org.ateam.oncare.beneficiary.command.controller;

/* 장기요양등급만료알림 등록,변경,삭제 부분 controller */

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.dto.request.CreateNoticeRequest;
import org.ateam.oncare.beneficiary.command.dto.request.UpdateExtendsStatusRequest;
import org.ateam.oncare.beneficiary.command.dto.request.UpdateNoticeRequest;
import org.ateam.oncare.beneficiary.command.service.CareLevelExpirationCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/care-level")
public class CareLevelExpirationCommandController {

    private final CareLevelExpirationCommandService service;

    /**
     * 4) 등급 연장 예정 체크(Y/N)
     * Y = 연장 예정(목록 포함), N = 연장 안함(목록 제외)
     */
    @PatchMapping("/expirations/{expirationId}/extends")
    public int updateExtends(@PathVariable Integer expirationId,
                             @RequestBody UpdateExtendsStatusRequest req) {
        return service.updateExtendsStatus(expirationId, req);
    }

    /**
     * 5) 부재중/미완료 기록(자동문구)
     * - empId는 현재 로그인 사용자로 받는 게 정석인데,
     *   일단 requestParam으로 받게 해둠
     */
    @PostMapping("/expirations/{expirationId}/notices/absent")
    public void recordAbsent(@PathVariable Integer expirationId,
                             @RequestParam Integer empId) {
        service.recordAbsent(expirationId, empId);
    }

    /**
     * 6) 안내 완료 처리
     */
    @PostMapping("/expirations/{expirationId}/notices/complete")
    public void completeNotice(@PathVariable Integer expirationId,
                               @RequestBody CreateNoticeRequest req) {
        service.completeNotice(expirationId, req);
    }

    /**
     * 7) 안내 이력 변경
     */
    @PutMapping("/expirations/{expirationId}/notices/{noticeId}")
    public int updateNotice(@PathVariable Integer expirationId,
                            @PathVariable Integer noticeId,
                            @RequestBody UpdateNoticeRequest req) {
        return service.updateNotice(expirationId, noticeId, req);
    }


    /**
     * 8) 안내 이력 취소(삭제)
     */
    @DeleteMapping("/expirations/{expirationId}/notices/{noticeId}")
    public int deleteNotice(@PathVariable Integer expirationId,
                            @PathVariable Integer noticeId) {
        return service.deleteNotice(expirationId, noticeId);
    }
}