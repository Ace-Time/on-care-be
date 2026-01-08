package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CalendarScheduleDto;
import org.ateam.oncare.careworker.query.dto.PersonalTypeDto;
import org.ateam.oncare.careworker.query.dto.ScheduleDetailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CareWorkerScheduleMapperTest {

    @Mock
    private CareWorkerScheduleMapper careWorkerScheduleMapper;

    private CalendarScheduleDto mockCalendarSchedule;
    private CalendarScheduleDto mockMultipleServiceSchedule;
    private ScheduleDetailDto mockScheduleDetail;
    private List<PersonalTypeDto> mockPersonalTypes;

    @BeforeEach
    void setUp() {
        // Mock 데이터 준비
        mockCalendarSchedule = new CalendarScheduleDto();
        mockCalendarSchedule.setScheduleId(1L);
        mockCalendarSchedule.setRecipientName("김수급");
        mockCalendarSchedule.setDate(LocalDate.of(2024, 1, 15));
        mockCalendarSchedule.setStartTime(java.time.LocalTime.of(9, 0));
        mockCalendarSchedule.setEndTime(java.time.LocalTime.of(17, 0));
        mockCalendarSchedule.setServiceTypes(List.of("방문요양"));

        // 여러 서비스 유형을 가진 일정
        mockMultipleServiceSchedule = new CalendarScheduleDto();
        mockMultipleServiceSchedule.setScheduleId(2L);
        mockMultipleServiceSchedule.setRecipientName("이수급");
        mockMultipleServiceSchedule.setDate(LocalDate.of(2024, 1, 16));
        mockMultipleServiceSchedule.setStartTime(java.time.LocalTime.of(9, 0));
        mockMultipleServiceSchedule.setEndTime(java.time.LocalTime.of(17, 0));
        mockMultipleServiceSchedule.setServiceTypes(List.of("방문요양", "방문목욕"));

        mockScheduleDetail = new ScheduleDetailDto();
        mockScheduleDetail.setScheduleId(1L);
        mockScheduleDetail.setTimeRange("09:00 - 17:00");

        mockPersonalTypes = new ArrayList<>();
        PersonalTypeDto type1 = new PersonalTypeDto();
        type1.setId(1);
        type1.setName("휴가");
        mockPersonalTypes.add(type1);

        PersonalTypeDto type2 = new PersonalTypeDto();
        type2.setId(2);
        type2.setName("교육");
        mockPersonalTypes.add(type2);
    }

    @Test
    @DisplayName("기간별 일정 조회: 요양보호사의 기간별 일정이 조회되어야 한다")
    void selectSchedulesByPeriod_shouldReturnSchedules() {
        // Given
        Long caregiverId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().plusDays(30);

        when(careWorkerScheduleMapper.selectSchedulesByPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(mockCalendarSchedule));

        // When
        List<CalendarScheduleDto> schedules = careWorkerScheduleMapper.selectSchedulesByPeriod(
                caregiverId, startDate, endDate
        );

        // Then
        assertThat(schedules).isNotNull();
        assertThat(schedules).hasSize(1);

        System.out.println("=== 기간별 일정 조회 ===");
        System.out.println("조회 기간: " + startDate + " ~ " + endDate);
        System.out.println("일정 개수: " + schedules.size());

        CalendarScheduleDto firstSchedule = schedules.get(0);
        System.out.println("첫 번째 일정: " + firstSchedule);

        // 서비스 유형이 배열로 조회되는지 확인
        assertThat(firstSchedule.getServiceTypes()).isNotNull();
        assertThat(firstSchedule.getServiceTypes()).isNotEmpty();
        System.out.println("서비스 유형들: " + firstSchedule.getServiceTypes());
        System.out.println("======================");
    }

    @Test
    @DisplayName("일정 상세 조회: 특정 일정의 상세 정보가 조회되어야 한다")
    void selectScheduleDetail_shouldReturnDetail() {
        // Given
        Long scheduleId = 1L;
        Long caregiverId = 1L;

        when(careWorkerScheduleMapper.selectScheduleDetail(anyLong(), anyLong()))
                .thenReturn(Optional.of(mockScheduleDetail));

        // When
        Optional<ScheduleDetailDto> scheduleDetail = careWorkerScheduleMapper.selectScheduleDetail(
                scheduleId, caregiverId
        );

        // Then
        System.out.println("=== 일정 상세 조회 ===");
        assertThat(scheduleDetail).isPresent();

        ScheduleDetailDto detail = scheduleDetail.get();
        System.out.println("일정 상세: " + detail);

        assertThat(detail.getScheduleId()).isEqualTo(scheduleId);
        assertThat(detail.getTimeRange()).isNotNull();
        assertThat(detail.getTimeRange()).isEqualTo("09:00 - 17:00");
        System.out.println("====================");
    }

    @Test
    @DisplayName("개인 일정 유형 목록 조회: 드롭다운용 개인 일정 유형이 조회되어야 한다")
    void selectPersonalTypes_shouldReturnTypes() {
        // Given
        when(careWorkerScheduleMapper.selectPersonalTypes()).thenReturn(mockPersonalTypes);

        // When
        List<PersonalTypeDto> personalTypes = careWorkerScheduleMapper.selectPersonalTypes();

        // Then
        assertThat(personalTypes).isNotNull();
        assertThat(personalTypes).hasSize(2);

        System.out.println("=== 개인 일정 유형 목록 ===");
        System.out.println("유형 개수: " + personalTypes.size());

        for (PersonalTypeDto type : personalTypes) {
            System.out.println("- ID: " + type.getId() + ", 이름: " + type.getName());
        }
        System.out.println("========================");

        // 최소한 하나 이상의 개인 일정 유형이 있어야 함
        assertThat(personalTypes).isNotEmpty();
        assertThat(personalTypes.get(0).getName()).isEqualTo("휴가");
        assertThat(personalTypes.get(1).getName()).isEqualTo("교육");
    }

    @Test
    @DisplayName("여러 서비스 유형 조회: 동일 수급자의 여러 서비스 유형이 배열로 그룹핑되어야 한다")
    void selectSchedulesByPeriod_shouldGroupServiceTypes() {
        // Given
        Long caregiverId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().plusDays(7);

        when(careWorkerScheduleMapper.selectSchedulesByPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(mockCalendarSchedule, mockMultipleServiceSchedule));

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

        assertThat(foundMultipleServiceTypes).isTrue();
        System.out.println("✓ 여러 서비스 유형이 그룹핑된 일정이 존재합니다.");

        schedules.stream()
                .filter(s -> s.getServiceTypes() != null && s.getServiceTypes().size() > 1)
                .forEach(s -> System.out.println("수급자: " + s.getRecipientName()
                        + ", 서비스 유형: " + s.getServiceTypes()));

        System.out.println("============================");
    }
}
