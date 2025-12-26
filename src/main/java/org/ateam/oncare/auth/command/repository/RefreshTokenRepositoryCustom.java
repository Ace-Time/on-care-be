package org.ateam.oncare.auth.command.repository;

public interface RefreshTokenRepositoryCustom {
    long revokeTorken(String jti);
}
