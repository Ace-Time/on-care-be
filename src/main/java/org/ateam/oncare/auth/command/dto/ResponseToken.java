package org.ateam.oncare.auth.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public ResponseToken(String accessToken, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }
}
