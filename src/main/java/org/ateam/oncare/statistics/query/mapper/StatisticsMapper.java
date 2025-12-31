package org.ateam.oncare.statistics.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.statistics.query.dto.ProductStatisticDTO;
import org.ateam.oncare.statistics.query.dto.CareLevelExpirationCountDTO;
import java.util.List;

@Mapper
public interface StatisticsMapper {
    // 상품별 수익률 조회
    List<ProductStatisticDTO> selectProductStatistics();

    // 장기 요양 등급 만료 임박 카운트 조회
    CareLevelExpirationCountDTO selectCareLevelExpirationCounts();

    // 월별 잠재고객 및 계약 성사 고객 수 조회
    List<org.ateam.oncare.statistics.query.dto.MonthlyClientStatsDTO> selectMonthlyClientStats();

    // 주간/야간 등 미배정 수급자 수 조회
    int countUnassignedBeneficiaries();

    // 대기 중인 전자 결재 수 조회 (내가 결재자인 경우)
    int countPendingApprovals(@org.apache.ibatis.annotations.Param("employeeId") int employeeId);

    // 월별 신규 및 탈퇴 수급자 수 조회
    List<org.ateam.oncare.statistics.query.dto.MonthlyBeneficiaryStatsDTO> selectMonthlyBeneficiaryStats();

    // 미배정 수급자 리스트 조회
    List<org.ateam.oncare.statistics.query.dto.UnassignedBeneficiaryDTO> selectUnassignedBeneficiaries();

    // 이탈 징후 수급자 리스트 조회
    List<org.ateam.oncare.statistics.query.dto.ChurnRiskBeneficiaryDTO> selectChurnRiskBeneficiaries();

    // 위험등급별 수급자 현황 조회
    List<org.ateam.oncare.statistics.query.dto.RiskLevelBeneficiaryCountDTO> selectRiskLevelBeneficiaryCounts();

    // 장기요양등급별 수급자 현황 조회
    List<org.ateam.oncare.statistics.query.dto.CareGradeBeneficiaryCountDTO> selectCareGradeBeneficiaryCounts();

    /**
     * 연체 고객 정보 조회
     *
     * @return 연체 고객(송장) 목록
     */
    List<org.ateam.oncare.statistics.query.dto.OverdueInvoiceDTO> selectOverdueBeneficiaries();
}
