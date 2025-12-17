package org.ateam.oncare.auth.command.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.command.entity.QRefreshToken;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;
    private final QRefreshToken refreshToken = QRefreshToken.refreshToken;

    @Override
    @Transactional
    public long revokeTorken(String jti) {
        long count = jpaQueryFactory
                .update(refreshToken)
                .set(refreshToken.revoked, 1)
                .set(refreshToken.lastUsedAt, LocalDateTime.now())
                .where(refreshToken.jti.eq(jti))
                .execute();

        // 벌크 연산 후에는 영속성 컨텍스트를 비워주는 것이 안전.
        // 이 트랜잭션 안에서 해당 엔티티를 다시 조회할 일이 없다면 생략해도 되지만,
        // 혹시 모를 데이터 불일치를 막기 위해 초기화하는 습관을 들이는 게 좋음.
        em.flush();
        em.clear();

        return count;
    }
}
