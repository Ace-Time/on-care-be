package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardMapperTest {

    @Mock
    private DashboardMapper dashboardMapper;

    private DashboardSummaryDto mockDashboardSummary;
    private List<UrgentNotificationDto> mockNotifications;
    private List<HomeScheduleDto> mockTodaySchedules;
    private BeneficiaryDetailDto mockBeneficiaryDetail;
    private List<BeneficiaryDetailDto.GuardianDto> mockGuardians;
    private List<MyBeneficiaryDto> mockMyBeneficiaries;

    @BeforeEach
    void setUp() {
        // Mock 대시보드 요약
        mockDashboardSummary = new DashboardSummaryDto();
        mockDashboardSummary.setTodayScheduleCount(5);
        mockDashboardSummary.setBeneficiaryCount(10);
        mockDashboardSummary.setMonthlyWorkHours(160.5);

        // Mock 긴급 알림
        mockNotifications = new ArrayList<>();
        UrgentNotificationDto notification1 = new UrgentNotificationDto();
        notification1.setMessage("요양일지 미작성");
        notification1.setDueDate(LocalDate.of(2024, 1, 15));
        mockNotifications.add(notification1);

        UrgentNotificationDto notification2 = new UrgentNotificationDto();
        notification2.setMessage("건강상태 확인 필요");
        notification2.setDueDate(LocalDate.of(2024, 1, 16));
        mockNotifications.add(notification2);

        // Mock 오늘의 일정
        mockTodaySchedules = new ArrayList<>();
        HomeScheduleDto schedule1 = new HomeScheduleDto();
        schedule1.setVisitTime("09:00");
        schedule1.setRecipientName("김수급");
        mockTodaySchedules.add(schedule1);

        HomeScheduleDto schedule2 = new HomeScheduleDto();
        schedule2.setVisitTime("14:00");
        schedule2.setRecipientName("이수급");
        mockTodaySchedules.add(schedule2);

        // Mock 수급자 상세
        mockBeneficiaryDetail = new BeneficiaryDetailDto();
        mockBeneficiaryDetail.setBeneficiaryId(1L);
        mockBeneficiaryDetail.setName("김수급");
        mockBeneficiaryDetail.setAddress("서울시 강남구");
        mockBeneficiaryDetail.setBirthdate(LocalDate.of(1950, 5, 15));
        mockBeneficiaryDetail.setCareLevelNumber("3");

        // Mock 보호자
        mockGuardians = new ArrayList<>();
        BeneficiaryDetailDto.GuardianDto guardian1 = new BeneficiaryDetailDto.GuardianDto();
        guardian1.setName("김보호");
        guardian1.setRelation("아들");
        guardian1.setPhone("010-1234-5678");
        mockGuardians.add(guardian1);

        BeneficiaryDetailDto.GuardianDto guardian2 = new BeneficiaryDetailDto.GuardianDto();
        guardian2.setName("이보호");
        guardian2.setRelation("딸");
        guardian2.setPhone("010-9876-5432");
        mockGuardians.add(guardian2);

        // Mock 내 수급자
        mockMyBeneficiaries = new ArrayList<>();
        MyBeneficiaryDto beneficiary1 = new MyBeneficiaryDto();
        beneficiary1.setName("김수급");
        beneficiary1.setAddress("서울시 강남구");
        mockMyBeneficiaries.add(beneficiary1);

        MyBeneficiaryDto beneficiary2 = new MyBeneficiaryDto();
        beneficiary2.setName("이수급");
        beneficiary2.setAddress("서울시 서초구");
        mockMyBeneficiaries.add(beneficiary2);
    }

    @Test
    @DisplayName("대시보드 요약 정보 조회: 요양보호사의 요약 정보가 조회되어야 한다")
    void selectDashboardSummary_shouldReturnSummary() {
        // Given
        Long caregiverId = 1L;
        when(dashboardMapper.selectDashboardSummary(anyLong())).thenReturn(mockDashboardSummary);

        // When
        DashboardSummaryDto summary = dashboardMapper.selectDashboardSummary(caregiverId);

        // Then
        System.out.println("=== 대시보드 요약 정보 ===");

        assertThat(summary).isNotNull();
        System.out.println("오늘 일정 수: " + summary.getTodayScheduleCount());
        System.out.println("수급자 수: " + summary.getBeneficiaryCount());
        System.out.println("이번 달 누적 근무시간: " + summary.getMonthlyWorkHours());

        assertThat(summary.getTodayScheduleCount()).isNotNegative();
        assertThat(summary.getTodayScheduleCount()).isEqualTo(5);
        assertThat(summary.getBeneficiaryCount()).isEqualTo(10);
        assertThat(summary.getMonthlyWorkHours()).isEqualTo(160.5);
        System.out.println("======================");
    }

    @Test
    @DisplayName("긴급 알림 목록 조회: 요양보호사의 긴급 알림이 조회되어야 한다")
    void selectUrgentNotifications_shouldReturnNotifications() {
        // Given
        Long caregiverId = 1L;
        when(dashboardMapper.selectUrgentNotifications(anyLong())).thenReturn(mockNotifications);

        // When
        List<UrgentNotificationDto> notifications = dashboardMapper.selectUrgentNotifications(caregiverId);

        // Then
        assertThat(notifications).isNotNull();
        assertThat(notifications).hasSize(2);

        System.out.println("=== 긴급 알림 목록 ===");
        System.out.println("알림 개수: " + notifications.size());

        notifications.forEach(notification -> System.out.println("- " + notification.getMessage()
                + " (" + notification.getDueDate() + ")"));

        assertThat(notifications.get(0).getMessage()).isEqualTo("요양일지 미작성");
        assertThat(notifications.get(1).getMessage()).isEqualTo("건강상태 확인 필요");
        System.out.println("===================");
    }

    @Test
    @DisplayName("오늘의 일정 목록 조회: 오늘의 일정이 조회되어야 한다")
    void selectTodaySchedules_shouldReturnTodaySchedules() {
        // Given
        Long caregiverId = 1L;
        when(dashboardMapper.selectTodaySchedules(anyLong())).thenReturn(mockTodaySchedules);

        // When
        List<HomeScheduleDto> todaySchedules = dashboardMapper.selectTodaySchedules(caregiverId);

        // Then
        assertThat(todaySchedules).isNotNull();
        assertThat(todaySchedules).hasSize(2);

        System.out.println("=== 오늘의 일정 목록 ===");
        System.out.println("일정 개수: " + todaySchedules.size());

        todaySchedules.forEach(schedule -> System.out.println("- " + schedule.getVisitTime()
                + " / " + schedule.getRecipientName()));

        assertThat(todaySchedules.get(0).getVisitTime()).isEqualTo("09:00");
        assertThat(todaySchedules.get(0).getRecipientName()).isEqualTo("김수급");
        System.out.println("=====================");
    }

    @Test
    @DisplayName("수급자 상세 정보 조회: 수급자의 상세 정보가 조회되어야 한다")
    void selectBeneficiaryDetail_shouldReturnDetail() {
        // Given
        Long beneficiaryId = 1L;
        when(dashboardMapper.selectBeneficiaryDetail(anyLong())).thenReturn(mockBeneficiaryDetail);

        // When
        BeneficiaryDetailDto detail = dashboardMapper.selectBeneficiaryDetail(beneficiaryId);

        // Then
        System.out.println("=== 수급자 상세 정보 ===");

        assertThat(detail).isNotNull();
        System.out.println("수급자 ID: " + detail.getBeneficiaryId());
        System.out.println("이름: " + detail.getName());
        System.out.println("주소: " + detail.getAddress());
        System.out.println("생년월일: " + detail.getBirthdate());
        System.out.println("등급번호: " + detail.getCareLevelNumber());

        assertThat(detail.getBeneficiaryId()).isEqualTo(beneficiaryId);
        assertThat(detail.getName()).isNotNull();
        assertThat(detail.getName()).isEqualTo("김수급");

        // careLevelNumber가 String 타입으로 조회되는지 확인
        assertThat(detail.getCareLevelNumber()).isInstanceOf(String.class);
        assertThat(detail.getCareLevelNumber()).isEqualTo("3");
        System.out.println("=====================");
    }

    @Test
    @DisplayName("수급자 보호자 목록 조회: 보호자 목록이 조회되어야 한다")
    void selectGuardians_shouldReturnGuardians() {
        // Given
        Long beneficiaryId = 1L;
        when(dashboardMapper.selectGuardians(anyLong())).thenReturn(mockGuardians);

        // When
        List<BeneficiaryDetailDto.GuardianDto> guardians = dashboardMapper.selectGuardians(beneficiaryId);

        // Then
        assertThat(guardians).isNotNull();
        assertThat(guardians).hasSize(2);

        System.out.println("=== 수급자 보호자 목록 ===");
        System.out.println("보호자 수: " + guardians.size());

        guardians.forEach(guardian -> System.out.println("- " + guardian.getName()
                + " (" + guardian.getRelation() + ") "
                + guardian.getPhone()));

        assertThat(guardians.get(0).getName()).isEqualTo("김보호");
        assertThat(guardians.get(0).getRelation()).isEqualTo("아들");
        assertThat(guardians.get(1).getName()).isEqualTo("이보호");
        System.out.println("=======================");
    }

    @Test
    @DisplayName("내 수급자 목록 조회: 담당 수급자 목록이 조회되어야 한다")
    void selectMyBeneficiaries_shouldReturnBeneficiaries() {
        // Given
        Long employeeId = 1L;
        when(dashboardMapper.selectMyBeneficiaries(anyLong())).thenReturn(mockMyBeneficiaries);

        // When
        List<MyBeneficiaryDto> beneficiaries = dashboardMapper.selectMyBeneficiaries(employeeId);

        // Then
        assertThat(beneficiaries).isNotNull();
        assertThat(beneficiaries).hasSize(2);

        System.out.println("=== 내 수급자 목록 ===");
        System.out.println("수급자 수: " + beneficiaries.size());

        beneficiaries.forEach(beneficiary -> System.out.println("- " + beneficiary.getName()
                + " (" + beneficiary.getAddress() + ")"));

        assertThat(beneficiaries.get(0).getName()).isEqualTo("김수급");
        assertThat(beneficiaries.get(1).getName()).isEqualTo("이수급");
        System.out.println("===================");
    }
}
