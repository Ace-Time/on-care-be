package org.ateam.oncare.counsel.command.dto;

import lombok.Data;

@Data
public class RegistPotentialCustomer {
    private String name;  // 필수
    private String phone;  // 필수
}
