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

    private String careLevelEndDate;
}
