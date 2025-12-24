package org.ateam.oncare.beneficiary.query.dto.response;

/* 장기요양등급만료알림 전체 조회 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CareLevelExpirationListResponse {

    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long beneficiaryId;
        private String beneficiaryName;

        private String endDate;      // yyyy-MM-dd
        private String ddayLabel;    // D-40

        private Integer expirationId;
        private Integer expiredSection;   // 1:90, 2:60, 3:45

        private String outboundStatus;    // N/Y
        private String extendsStatus;     // N/Y

        private Integer careWorkerId;
        private String careWorkerName;

        private Integer noticeCount;
        private String lastNoticeDate;    // yyyy-MM-dd HH:mm:ss (없으면 null)
        private String noticeLabel;       // 미완료 / 완료(n회)
    }
}