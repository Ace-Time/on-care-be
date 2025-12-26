package org.ateam.oncare.beneficiary.query.dto.response;

/* 장기요양등급만료알림 상세 조회 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareLevelExpirationDetailResponse {

    private Integer expirationId;

    private Long beneficiaryId;
    private String beneficiaryName;

    private String endDate;       // yyyy-MM-dd
    private String ddayLabel;     // D-40

    private String careLevel;      // 예: "3등급" 또는 "인지지원등급"

    private Integer expiredSection;
    private String extendsStatus;     // N/Y
    private String outboundStatus;    // N/Y

    private Integer careWorkerId;
    private String careWorkerName;

    private String guardianName;
    private String guardianPhone;
    private String guardianRelation;
}
