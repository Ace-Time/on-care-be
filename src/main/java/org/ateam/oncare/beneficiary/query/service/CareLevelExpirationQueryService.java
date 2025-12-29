package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationListResponse;
import org.ateam.oncare.beneficiary.query.dto.response.NoticeExpirationListResponse;
import org.ateam.oncare.beneficiary.query.mapper.CareLevelExpirationQueryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareLevelExpirationQueryService {

    private final CareLevelExpirationQueryMapper mapper;
//    private final NotificationCommandService commandService;

    /**
     * 1) 만료 예정 전체조회
     * - section: 1(90일), 2(60일), 3(45일), null이면 전체
     */
    public CareLevelExpirationListResponse getExpirationList(Integer section) {
        List<CareLevelExpirationListResponse.Item> items = mapper.selectExpirationList(section);
        CareLevelExpirationListResponse res = new CareLevelExpirationListResponse();
        res.setItems(items);

        return res;
    }

    /**
     * 2) 특정 만료알림 상세조회(기본정보)
     */
    public CareLevelExpirationDetailResponse getExpirationDetail(Integer expirationId) {
        return mapper.selectExpirationDetail(expirationId);
    }

    /**
     * 3) 안내이력 목록
     */
    public NoticeExpirationListResponse getNoticeList(Integer expirationId) {
        List<NoticeExpirationListResponse.Item> items = mapper.selectNoticeList(expirationId);
        NoticeExpirationListResponse res = new NoticeExpirationListResponse();
        res.setItems(items);
        return res;
    }
}