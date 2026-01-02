package org.ateam.oncare.payment.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUserResponse {
    private Long id;
    private String name;
    private String departmentName; // 부서명 (없으면 "소속없음" 등)
    private String jobTitle; // 직위/직책 (예: 과장, 팀장)

    // 필요 시 추가 (프로필 이미지 등)
    // private String profileImageUrl;
}
