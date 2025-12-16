package org.ateam.oncare.auth.command.dto;

import lombok.*;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ResponseToken {
    private String accessToken;
    private ResponseCookie cookie;
    private String tokenType;

    public ResponseToken(String accessToken, ResponseCookie cookie, String tokenType) {
        this.accessToken = accessToken;
        this.cookie = cookie;
        this.tokenType = tokenType;
    }
}
