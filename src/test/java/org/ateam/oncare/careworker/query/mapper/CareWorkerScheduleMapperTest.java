package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CalendarScheduleDto;
import org.ateam.oncare.careworker.query.dto.PersonalTypeDto;
import org.ateam.oncare.careworker.query.dto.ScheduleDetailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CareWorkerScheduleMapperTest {

    @Autowired
    private CareWorkerScheduleMapper careWorkerScheduleMapper;

    @Test
    @DisplayName("기간별 일정 조회: 요양보호사의 기간별 일정이 조회되어야 한다")
    void selectSchedulesByPeriod_shouldReturnSchedules() {
        // Given
        Long caregiverId = 1L; // dev DB에 존재하는 요양보호사 ID
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().plusDays(30);

        // When
        List<CalendarScheduleDto> schedules = careWorkerScheduleMapper.selectSchedulesByPeriod(
                caregiverId, startDate, endDate
        );

        // Then
        assertThat(schedules).isNotNull();

        System.out.println("=== 기간별 일정 조회 ===");
        System.out.println("조회 기간: " + startDate + " ~ " + endDate);
        System.out.println("일정 개수: " + schedules.size());

        if (!schedules.isEmpty()) {
            CalendarScheduleDto firstSchedule = schedules.get(0);
            System.out.println("첫 번째 일정: " + firstSchedule);

            // 서비스 유형이 배열로 조회되는지 확인
            if (firstSchedule.getServiceTypes() != null) {
                System.out.println("서비스 유형들: " + firstSchedule.getServiceTypes());
                assertThat(firstSchedule.getServiceTypes()).isNotEmpty();
            }
        }
        System.out.println("======================");
    }

    @Test
    @DisplayName("일정 상세 조회: 특정 일정의 상세 정보가 조회되어야 한다")
    void selectScheduleDetail_shouldReturnDetail() {
        // Given
        Long scheduleId = 1L; // dev DB에 존재하는 일정 ID
        Long caregiverId = 1L; // dev DB에 존재하는 요양보호사 ID

        // When
        Optional<ScheduleDetailDto> scheduleDetail = careWorkerScheduleMapper.selectScheduleDetail(
                scheduleId, caregiverId
        );

        // Then
        System.out.println("=== 일정 상세 조회 ===");
        if (scheduleDetail.isPresent()) {
            ScheduleDetailDto detail = scheduleDetail.get();
            System.out.println("일정 상세: " + detail);

            assertThat(detail.getScheduleId()).isEqualTo(scheduleId);
            assertThat(detail.getTimeRange()).isNotNull();
        } else {
            System.out.println("해당 일정을 찾을 수 없습니다.");
        }
        System.out.println("====================");
    }

    @Test
    @DisplayName("개인 일정 유형 목록 조회: 드롭다운용 개인 일정 유형이 조회되어야 한다")
    void selectPersonalTypes_shouldReturnTypes() {
        // When
        List<PersonalTypeDto> personalTypes = careWorkerScheduleMapper.selectPersonalTypes();

        // Then
        assertThat(personalTypes).isNotNull();

        System.out.println("=== 개인 일정 유형 목록 ===");
        System.out.println("유형 개수: " + personalTypes.size());

        for (PersonalTypeDto type : personalTypes) {
            System.out.println("- ID: " + type.getId() + ", 이름: " + type.getName());
        }
        System.out.println("========================");

        // 최소한 하나 이상의 개인 일정 유형이 있어야 함
        assertThat(personalTypes).isNotEmpty();
    }

    @Test
    @DisplayName("여러 서비스 유형 조회: 동일 수급자의 여러 서비스 유형이 배열로 그룹핑되어야 한다")
    void selectSchedulesByPeriod_shouldGroupServiceTypes() {
        // Given
        Long caregiverId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().plusDays(7);

        // When
        List<CalendarScheduleDto> schedules = careWorkerScheduleMapper.selectSchedulesByPeriod(
                caregiverId, startDate, endDate
        );

        // Then
        System.out.println("=== 서비스 유형 그룹핑 확인 ===");

        // 여러 서비스 유형을 가진 일정 찾기
        boolean foundMultipleServiceTypes = schedules.stream()
                .anyMatch(schedule -> schedule.getServiceTypes() != null
                        && schedule.getServiceTypes().size() > 1);

        if (foundMultipleServiceTypes) {
            System.out.println("✓ 여러 서비스 유형이 그룹핑된 일정이 존재합니다.");

            schedules.stream()
                    .filter(s -> s.getServiceTypes() != null && s.getServiceTypes().size() > 1)
                    .forEach(s -> {
                        System.out.println("수급자: " + s.getRecipientName()
                                + ", 서비스 유형: " + s.getServiceTypes());
                    });
        } else {
            System.out.println("현재 데이터에는 여러 서비스 유형을 가진 일정이 없습니다.");
        }
        System.out.println("============================");
    }
}
