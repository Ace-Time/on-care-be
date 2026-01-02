package org.ateam.oncare.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnassignedBeneficiaryDTO {
    private Long id;
    private String name;
    private String gender; // "M", "F"
    private LocalDate birthdate; // 생년월일
    private String address;
    private String phone;
    private Integer age; // 나이 (계산 필요할 수 있음, 또는 생년월일만 보냄)
}
