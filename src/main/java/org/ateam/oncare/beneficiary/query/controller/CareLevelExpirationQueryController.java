package org.ateam.oncare.beneficiary.query.controller;

/* 장기요양등급만료알림 조회부분 controller */

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationListResponse;
import org.ateam.oncare.beneficiary.query.dto.response.NoticeExpirationListResponse;
import org.ateam.oncare.beneficiary.query.service.CareLevelExpirationQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/care-level/expirations")
public class CareLevelExpirationQueryController {

    private final CareLevelExpirationQueryService service;

    /**
     * 1) 만료 예정 전체조회
     * - section: 1(90일), 2(60일), 3(45일), null이면 전체
     */
    @GetMapping
    public CareLevelExpirationListResponse getList(@RequestParam(required = false) Integer section) {
        return service.getExpirationList(section);
    }

    /**
     * 2) 특정 만료알림 상세조회(기본정보)
     */
    @GetMapping("/{expirationId}")
    public CareLevelExpirationDetailResponse getDetail(@PathVariable Integer expirationId) {
        return service.getExpirationDetail(expirationId);
    }

    /**
     * 3) 안내이력 목록
     */
    @GetMapping("/{expirationId}/notices")
    public NoticeExpirationListResponse getNotices(@PathVariable Integer expirationId) {
        return service.getNoticeList(expirationId);
    }
}