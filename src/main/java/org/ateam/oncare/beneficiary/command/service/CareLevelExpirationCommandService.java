package org.ateam.oncare.beneficiary.command.service;

// 현재탭: noticeDate null -> NOW()
// 직접입력탭: noticeDate 값 -> 그 시간으로 업데이트

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.dto.request.CreateNoticeRequest;
import org.ateam.oncare.beneficiary.command.dto.request.UpdateExtendsStatusRequest;
import org.ateam.oncare.beneficiary.command.dto.request.UpdateNoticeRequest;
import org.ateam.oncare.beneficiary.command.mapper.CareLevelExpirationCommandMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CareLevelExpirationCommandService {

    private final CareLevelExpirationCommandMapper mapper;

    @Transactional
    public int updateExtendsStatus(Integer expirationId, UpdateExtendsStatusRequest req) {
        return mapper.updateExtendsStatus(expirationId, req.getExtendsStatus());
    }

    /**
     * 부재중/미완료 기록
     * - 안내이력 INSERT (자동문구, NOW())
     * - outbound_status = 'N'
     */
    @Transactional
    public void recordAbsent(Integer expirationId, Integer empId, String noticeDate) {
        mapper.insertNotice(expirationId, noticeDate, "연락 시도했으나 부재중. 추후 재연락 필요.", empId);
        mapper.updateOutboundStatus(expirationId, "N");
    }

    /**
     * 안내 완료 처리
     * - 안내이력 INSERT (사용자 입력)
     * - outbound_status = 'Y'
     */
    @Transactional
    public void completeNotice(Integer expirationId, CreateNoticeRequest req) {
        mapper.insertNotice(expirationId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
        mapper.updateOutboundStatus(expirationId, "Y");
    }

    @Transactional
    public int updateNotice(Integer expirationId, Integer noticeId, UpdateNoticeRequest req) {
        // 현재탭: noticeDate null -> NOW()
        // 직접입력탭: noticeDate 값 -> 그 시간으로 업데이트
        return mapper.updateNotice(expirationId, noticeId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
    }

    @Transactional
    public int deleteNotice(Integer expirationId, Integer noticeId) {
        return mapper.deleteNotice(expirationId, noticeId);
    }

    /**
     * (선택) 완료/미완료를 강제로 바꾸고 싶을 때
     */
    @Transactional
    public int updateOutboundStatus(Integer expirationId, String outboundStatus) {
        return mapper.updateOutboundStatus(expirationId, outboundStatus);
    }
}