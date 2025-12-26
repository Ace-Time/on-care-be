package org.ateam.oncare.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // -------------------------------------------------------
        // 1. 기본 설정 (Default Configuration)
        // 별도 설정이 없는 모든 캐시 이름에 적용됨
        // -------------------------------------------------------
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS) // 1시간 뒤 만료
                .maximumSize(1000));                 // 최대 1000개

        // -------------------------------------------------------
        // 2. 저장소별 개별 설정 (Custom Configuration)
        // -------------------------------------------------------

        // 마스터 정보 프로그램이 실행 되는 동안 유지
        cacheManager.registerCustomCache("masterData",
                Caffeine.newBuilder()
                        .maximumSize(100) // 개수 제한은 필수
                        .recordStats()
                        .build());

        // 짧은 만료 시간
        cacheManager.registerCustomCache("tempData",
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(5000)
                        .build());

        return cacheManager;
    }
}
