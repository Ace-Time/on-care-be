package org.ateam.oncare.auth.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "jti")
    private String jti;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "revoked")
    private Integer revoked;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "device_fp")
    private String deviceFp;

    @Column(name = "ip")
    private String ip;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "employee_id")
    private Integer employeeId;

}