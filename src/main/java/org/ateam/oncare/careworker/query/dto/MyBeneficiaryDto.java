package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MyBeneficiaryDto {
    private Long beneficiaryId;
    private String name;
    private String gender;
    private LocalDate birthdate;
    private Integer age;
    private String address;
    private String phone;
    private String careLevel;      // 요양등급
}
