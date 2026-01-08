package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class DashboardMapperTest {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Test
    @DisplayName("대시보드 요약 정보 조회: 요양보호사의 요약 정보가 조회되어야 한다")
    void selectDashboardSummary_shouldReturnSummary() {
        // Given
        Long caregiverId = 1L; // dev DB에 존재하는 요양보호사 ID

        // When
        DashboardSummaryDto summary = dashboardMapper.selectDashboardSummary(caregiverId);

        // Then
        System.out.println("=== 대시보드 요약 정보 ===");

        if (summary != null) {
            System.out.println("오늘 일정 수: " + summary.getTodayScheduleCount());
            System.out.println("수급자 수: " + summary.getBeneficiaryCount());
            System.out.println("이번 달 누적 근무시간: " + summary.getMonthlyWorkHours());

            assertThat(summary.getTodayScheduleCount()).isNotNegative();
        } else {
            System.out.println("요약 정보를 찾을 수 없습니다.");
        }
        System.out.println("======================");
    }

    @Test
    @DisplayName("긴급 알림 목록 조회: 요양보호사의 긴급 알림이 조회되어야 한다")
    void selectUrgentNotifications_shouldReturnNotifications() {
        // Given
        Long caregiverId = 1L;

        // When
        List<UrgentNotificationDto> notifications = dashboardMapper.selectUrgentNotifications(caregiverId);

        // Then
        assertThat(notifications).isNotNull();

        System.out.println("=== 긴급 알림 목록 ===");
        System.out.println("알림 개수: " + notifications.size());

        if (!notifications.isEmpty()) {
            notifications.forEach(notification -> System.out.println("- " + notification.getMessage()
                    + " (" + notification.getDueDate() + ")"));
        } else {
            System.out.println("긴급 알림이 없습니다.");
        }
        System.out.println("===================");
    }

    @Test
    @DisplayName("오늘의 일정 목록 조회: 오늘의 일정이 조회되어야 한다")
    void selectTodaySchedules_shouldReturnTodaySchedules() {
        // Given
        Long caregiverId = 1L;

        // When
        List<HomeScheduleDto> todaySchedules = dashboardMapper.selectTodaySchedules(caregiverId);

        // Then
        assertThat(todaySchedules).isNotNull();

        System.out.println("=== 오늘의 일정 목록 ===");
        System.out.println("일정 개수: " + todaySchedules.size());

        if (!todaySchedules.isEmpty()) {
            todaySchedules.forEach(schedule -> System.out.println("- " + schedule.getVisitTime()
                    + " / " + schedule.getRecipientName()));
        } else {
            System.out.println("오늘 일정이 없습니다.");
        }
        System.out.println("=====================");
    }

    @Test
    @DisplayName("수급자 상세 정보 조회: 수급자의 상세 정보가 조회되어야 한다")
    void selectBeneficiaryDetail_shouldReturnDetail() {
        // Given
        Long beneficiaryId = 1L;

        // When
        BeneficiaryDetailDto detail = dashboardMapper.selectBeneficiaryDetail(beneficiaryId);

        // Then
        System.out.println("=== 수급자 상세 정보 ===");

        if (detail != null) {
            System.out.println("수급자 ID: " + detail.getBeneficiaryId());
            System.out.println("이름: " + detail.getName());
            System.out.println("주소: " + detail.getAddress());
            System.out.println("생년월일: " + detail.getBirthdate());
            System.out.println("등급번호: " + detail.getCareLevelNumber());

            assertThat(detail.getBeneficiaryId()).isEqualTo(beneficiaryId);
            assertThat(detail.getName()).isNotNull();

            // careLevelNumber가 String 타입으로 조회되는지 확인
            assertThat(detail.getCareLevelNumber()).isInstanceOf(String.class);
        } else {
            System.out.println("수급자 정보를 찾을 수 없습니다.");
        }
        System.out.println("=====================");
    }

    @Test
    @DisplayName("수급자 보호자 목록 조회: 보호자 목록이 조회되어야 한다")
    void selectGuardians_shouldReturnGuardians() {
        // Given
        Long beneficiaryId = 1L;

        // When
        List<BeneficiaryDetailDto.GuardianDto> guardians = dashboardMapper.selectGuardians(beneficiaryId);

        // Then
        assertThat(guardians).isNotNull();

        System.out.println("=== 수급자 보호자 목록 ===");
        System.out.println("보호자 수: " + guardians.size());

        if (!guardians.isEmpty()) {
            guardians.forEach(guardian -> System.out.println("- " + guardian.getName()
                    + " (" + guardian.getRelation() + ") "
                    + guardian.getPhone()));
        } else {
            System.out.println("등록된 보호자가 없습니다.");
        }
        System.out.println("=======================");
    }

    @Test
    @DisplayName("내 수급자 목록 조회: 담당 수급자 목록이 조회되어야 한다")
    void selectMyBeneficiaries_shouldReturnBeneficiaries() {
        // Given
        Long employeeId = 1L; // dev DB에 존재하는 요양보호사(employee) ID

        // When
        List<MyBeneficiaryDto> beneficiaries = dashboardMapper.selectMyBeneficiaries(employeeId);

        // Then
        assertThat(beneficiaries).isNotNull();

        System.out.println("=== 내 수급자 목록 ===");
        System.out.println("수급자 수: " + beneficiaries.size());

        if (!beneficiaries.isEmpty()) {
            beneficiaries.forEach(beneficiary -> System.out.println("- " + beneficiary.getName()
                    + " (" + beneficiary.getAddress() + ")"));
        } else {
            System.out.println("담당 수급자가 없습니다.");
        }
        System.out.println("===================");
    }
}
