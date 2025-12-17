package org.ateam.oncare.auth.query.dto;



import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RefreshTokenOfEmployeeDTO {
    private Long id;
    private String tokenHash;
    private String jti;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private Integer revoked;
    private LocalDateTime revokedAt;
    private String deviceFp;
    private String ip;
    private LocalDateTime lastUsedAt;
    private Integer employeeId;
    private String email;
    private String name;
    private String phone;
    private List<String> authorities;
}
