package org.ateam.oncare.auth.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 롬복 생성자
public class ResponseTokenDTO {
    private String accessToken;
    private String tokenType; // 보통 "Bearer"
}
