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

    // 프론트와 동일한 자동문구 (부재중/미완료 판정 기준)
    private static final String AUTO_ABSENT_MEMO = "연락 시도했으나 부재중. 추후 재연락 필요.";

    @Transactional
    public int updateExtendsStatus(Integer expirationId, UpdateExtendsStatusRequest req) {
        return mapper.updateExtendsStatus(expirationId, req.getExtendsStatus());
    }

    /**
     * outbound_status 재계산
     * - 최신 기준: notice_date DESC, id DESC
     * - 최신 memo가 AUTO_ABSENT_MEMO면 'N', 아니면 'Y'
     * - 이력 없으면 'N' (미완료)
     */
    private void refreshOutboundStatus(Integer expirationId) {
        String latestMemo = mapper.selectLatestNoticeMemo(expirationId);

        String outboundStatus;
        if (latestMemo == null) {
            outboundStatus = "N";
        } else if (AUTO_ABSENT_MEMO.equals(latestMemo)) {
            outboundStatus = "N";
        } else {
            outboundStatus = "Y";
        }

        mapper.updateOutboundStatus(expirationId, outboundStatus);
    }

    /**
     * 부재중/미완료 기록
     * - 안내이력 INSERT (자동문구, NOW() 또는 직접입력)
     * - outbound_status는 "최신 notice_date" 기준으로 재계산
     */
    @Transactional
    public void recordAbsent(Integer expirationId, Integer empId, String noticeDate) {
        mapper.insertNotice(expirationId, noticeDate, AUTO_ABSENT_MEMO, empId);
        refreshOutboundStatus(expirationId);
    }

    /**
     * 안내 완료 처리
     * - 안내이력 INSERT (사용자 입력)
     * - outbound_status는 "최신 notice_date" 기준으로 재계산
     */
    @Transactional
    public void completeNotice(Integer expirationId, CreateNoticeRequest req) {
        mapper.insertNotice(expirationId, req.getNoticeDate(), req.getMemo(), req.getEmpId());
        refreshOutboundStatus(expirationId);
    }

    /**
     * 안내이력 수정
     * - 수정 후에도 outbound_status는 최신 notice_date 기준 재계산
     */
    @Transactional
    public int updateNotice(Integer expirationId, Integer noticeId, UpdateNoticeRequest req) {
        int updated = mapper.updateNotice(expirationId, noticeId, req.getNoticeDate(), req.getMemo(), req.getEmpId());

        // 소속 안 맞아서 0건 수정일 수도 있으니, 성공했을 때만 재계산
        if (updated > 0) {
            refreshOutboundStatus(expirationId);
        }
        return updated;
    }

    /**
     * 안내이력 삭제
     * - 삭제 후에도 outbound_status는 최신 notice_date 기준 재계산
     */
    @Transactional
    public int deleteNotice(Integer expirationId, Integer noticeId) {
        int deleted = mapper.deleteNotice(expirationId, noticeId);

        if (deleted > 0) {
            refreshOutboundStatus(expirationId);
        }
        return deleted;
    }

    /**
     * (선택) 완료/미완료를 강제로 바꾸고 싶을 때
     * - 기존 유지(혹시 관리자 기능 있으면 사용)
     */
    @Transactional
    public int updateOutboundStatus(Integer expirationId, String outboundStatus) {
        return mapper.updateOutboundStatus(expirationId, outboundStatus);
    }
}
