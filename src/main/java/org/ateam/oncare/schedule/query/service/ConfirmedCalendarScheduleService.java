package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarDayItemDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarMonthCountDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarScheduleDetailDto;
import org.ateam.oncare.schedule.query.dto.SchedulePageResponse;
import org.ateam.oncare.schedule.query.mapper.ConfirmedCalendarScheduleMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmedCalendarScheduleService {

    private final ConfirmedCalendarScheduleMapper confirmedCalendarScheduleMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<ConfirmedCalendarMonthCountDto> getConfirmedCalendarCounts(
            LocalDate start, LocalDate end,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        return confirmedCalendarScheduleMapper.selectRangeCounts(
                start, end, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField
        );
    }

    public List<ConfirmedCalendarDayItemDto> getConfirmedCalendarDayList(
            LocalDate date,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField
    ) {
        return confirmedCalendarScheduleMapper.selectDayList(
                date, beneficiaryId, careWorkerId, serviceTypeId, keyword, searchField
        );
    }

    public ConfirmedCalendarScheduleDetailDto getConfirmedCalendarDetail(Integer vsId) {
        return confirmedCalendarScheduleMapper.selectDetailByVsId(vsId);
    }


    public SchedulePageResponse<ConfirmedCalendarDayItemDto> getConfirmedCalendarDayPage_test(
            LocalDate date,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField,
            int page, int size, boolean bypassCache
    ) {
        // 1. 파라미터 정리
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = safePage * safeSize;
        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        String sf = (searchField == null || searchField.trim().isEmpty()) ? "ALL" : searchField.trim().toUpperCase();

        // ---------------------------------------------------------------
        // [캐싱 전략 시작]
        // 조건: "첫 페이지(0)이면서 검색어(q)가 없을 때"만 캐시를 탄다.
        // 이유: 사용자의 80%는 검색 없이 첫 화면만 보고 이탈하기 때문 (가정)
        // ---------------------------------------------------------------
        boolean isCacheable = (safePage == 0 && q == null && !bypassCache);

        // 캐시 키 생성: 날짜 + 필수 ID들 조합 (유니크해야 함)
        // 예: "schedule:day:2025-12-13:101:10:1"
        String cacheKey = "schedule:day:" + date + ":" + beneficiaryId + ":" + careWorkerId + ":" + serviceTypeId;

        // 2. Redis 조회 시도 (Cache Hit?)
        if (isCacheable) {
            try {
                // Redis에서 데이터 가져오기
                Object cachedData = redisTemplate.opsForValue().get(cacheKey);

                if (cachedData != null) {
                    log.info("[Cache HIT] Key: {}", cacheKey);
                    // 캐시된 데이터를 바로 반환 (DB 안 감)
                    return (SchedulePageResponse<ConfirmedCalendarDayItemDto>) cachedData;
                }
            } catch (Exception e) {
                // Redis 에러가 나도 로그만 찍고 DB 조회를 하러 감 (Fail-Safe)
                log.error("[Redis Error] Get failed", e);
            }
        }

        // ---------------------------------------------------------------
        // [DB 조회] (Cache Miss 또는 캐싱 대상이 아닐 경우)
        // ---------------------------------------------------------------
        log.info("[DB Query] Conditions: date={}, page={}", date, safePage);

        List<ConfirmedCalendarDayItemDto> list = confirmedCalendarScheduleMapper.selectDayListPaged(
                date, beneficiaryId, careWorkerId, serviceTypeId, q, sf, offset, safeSize
        );

        long total = confirmedCalendarScheduleMapper.countDayList(
                date, beneficiaryId, careWorkerId, serviceTypeId, q, sf
        );

        SchedulePageResponse<ConfirmedCalendarDayItemDto> response =
                new SchedulePageResponse<>(list, safePage, safeSize, total);

        // ---------------------------------------------------------------
        // [캐시 저장] (Write Back)
        // 조건: 캐싱 대상이었고, 데이터가 정상적으로 조회되었다면 Redis에 저장
        // ---------------------------------------------------------------
        if (isCacheable) {
            try {
                // set(키, 값, 유효시간)
                // 10분(Duration.ofMinutes(10)) 동안만 보관
                redisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(10));
                log.info("[Cache SAVE] Key: {}", cacheKey);
            } catch (Exception e) {
                log.error("[Redis Error] Set failed", e);
            }
        }

        return response;
    }


    public SchedulePageResponse<ConfirmedCalendarDayItemDto> getConfirmedCalendarDayPage(
            LocalDate date,
            Long beneficiaryId, Integer careWorkerId, Integer serviceTypeId,
            String keyword, String searchField,
            int page, int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = safePage * safeSize;

        String q = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        String sf = (searchField == null || searchField.trim().isEmpty()) ? "ALL" : searchField.trim().toUpperCase();

        List<ConfirmedCalendarDayItemDto> list =
                confirmedCalendarScheduleMapper.selectDayListPaged(
                        date, beneficiaryId, careWorkerId, serviceTypeId, q, sf, offset, safeSize
                );

        long total =
                confirmedCalendarScheduleMapper.countDayList(
                        date, beneficiaryId, careWorkerId, serviceTypeId, q, sf
                );

        return new SchedulePageResponse<>(list, safePage, safeSize, total);
    }
}