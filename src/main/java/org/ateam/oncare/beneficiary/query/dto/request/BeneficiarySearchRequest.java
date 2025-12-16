package org.ateam.oncare.beneficiary.query.dto.request;
// 요청 DTO

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiarySearchRequest {

    // paging
    private int page = 0;     // 0-base
    private int size = 10;    // 10 / 15 / 20 / 30

    // filters
    private String status;        // ACTIVE / INACTIVE / ALL
    private Integer riskLevelId;  // m_risk_level.id
    private Integer careLevelId;  // beneficiary_count.m_care_level_id

    // ✅ search
    private String keyword;       // name / phone / address 검색

    // sort
    private String sort = "NAME";       // NAME / RISK / CARE_LEVEL
    private String direction = "ASC";   // ASC / DESC

    public int getOffset() {
        return page * size;
    }
}
