package org.ateam.oncare.statistics.query.service;

import org.ateam.oncare.statistics.query.dto.CareLevelExpirationCountDTO;
import org.ateam.oncare.statistics.query.dto.MonthlyClientStatsDTO;
import org.ateam.oncare.statistics.query.dto.StatisticsResponseDTO;

import java.util.List;

public interface StatisticsQueryService {
    // 상품별 수익률 조회
    StatisticsResponseDTO getProductStatistics();

    // 장기 요양 등급 만료 임박 카운트 조회
    CareLevelExpirationCountDTO getCareLevelExpirationCounts();

    // 월별 잠재고객 및 계약 성사 고객 수 조회
    List<MonthlyClientStatsDTO> getMonthlyClientStats();

    // 미배정 수급자 수 조회
    int getUnassignedBeneficiariesCount();

    // 대기 중인 전자 결재 수 조회 (내가 결재자인 경우)
    int getPendingApprovalsCount(int employeeId);

    // 월별 신규 수급자 및 탈퇴 수급자 수 조회
    List<org.ateam.oncare.statistics.query.dto.MonthlyBeneficiaryStatsDTO> getMonthlyBeneficiaryStats();

    // 미배정 수급자 리스트 조회
    List<org.ateam.oncare.statistics.query.dto.UnassignedBeneficiaryDTO> getUnassignedBeneficiaries();

    // 이탈 징후 수급자 리스트 조회
    List<org.ateam.oncare.statistics.query.dto.ChurnRiskBeneficiaryDTO> getChurnRiskBeneficiaries();

    // 위험등급별 수급자 현황 조회
    List<org.ateam.oncare.statistics.query.dto.RiskLevelBeneficiaryCountDTO> getRiskLevelBeneficiaryCounts();

    // 장기요양등급별 수급자 현황 조회
    List<org.ateam.oncare.statistics.query.dto.CareGradeBeneficiaryCountDTO> getCareGradeBeneficiaryCounts();

    /**
     * 연체 고객 정보 조회
     * 
     * @return 연체 고객 목록
     */
    List<org.ateam.oncare.statistics.query.dto.OverdueInvoiceDTO> getOverdueBeneficiaries();
}
