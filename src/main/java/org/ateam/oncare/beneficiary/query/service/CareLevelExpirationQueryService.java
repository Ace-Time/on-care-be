package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
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

    public CareLevelExpirationListResponse getExpirationList(Integer section) {
        List<CareLevelExpirationListResponse.Item> items = mapper.selectExpirationList(section);
        CareLevelExpirationListResponse res = new CareLevelExpirationListResponse();
        res.setItems(items);
        return res;
    }

    public CareLevelExpirationDetailResponse getExpirationDetail(Integer expirationId) {
        return mapper.selectExpirationDetail(expirationId);
    }

    public NoticeExpirationListResponse getNoticeList(Integer expirationId) {
        List<NoticeExpirationListResponse.Item> items = mapper.selectNoticeList(expirationId);
        NoticeExpirationListResponse res = new NoticeExpirationListResponse();
        res.setItems(items);
        return res;
    }
}