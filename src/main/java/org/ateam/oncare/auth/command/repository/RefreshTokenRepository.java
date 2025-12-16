package org.ateam.oncare.auth.command.repository;

import org.ateam.oncare.auth.command.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByJti(String jti);
}
