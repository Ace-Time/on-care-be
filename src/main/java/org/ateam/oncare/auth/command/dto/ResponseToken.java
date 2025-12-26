package org.ateam.oncare.auth.command.dto;

import lombok.*;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseToken {
    private String accessToken;
    private ResponseCookie cookie;
    private String tokenType;
    private int employeeId;
}
