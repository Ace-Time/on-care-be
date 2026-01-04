package org.ateam.oncare.beneficiary.command.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BeneficiaryUpdateResponse {

    private Long beneficiaryId;
    private String message;

    // 요청한 값 그대로 echo
    private String name;
    private String gender;
    private String birthdate;
    private String phone;
    private String address;
    private Integer status;

    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;

    private List<Long> tagIds;
    private List<Integer> riskFactorIds;

    // ✅ 기존
    private String careLevelEndDate;

    // ✅ 추가
    private Long careLevelNumber;  // 인정번호
    private Integer careLevelId;   // 장기요양등급 id (m_care_level.id)
}
