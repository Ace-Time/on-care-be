package org.ateam.oncare.statistics.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.statistics.query.dto.CareLevelExpirationCountDTO;
import org.ateam.oncare.statistics.query.dto.ProductStatisticDTO;
import org.ateam.oncare.statistics.query.dto.StatisticsResponseDTO;
import org.ateam.oncare.statistics.query.mapper.StatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsQueryServiceImpl implements StatisticsQueryService {

        private final StatisticsMapper statisticsMapper;

        // 상품별 수익률 조회
        @Override
        public StatisticsResponseDTO getProductStatistics() {
                List<ProductStatisticDTO> allStats = statisticsMapper.selectProductStatistics();

                // 수익률 높은 상위 5개 (SQL에서 이미 내림차순 정렬됨)
                List<ProductStatisticDTO> highReturn = allStats.stream()
                                .limit(5)
                                .collect(Collectors.toList());

                // 수익률 낮은 상위 5개
                // 수익률 오름차순 정렬
                List<ProductStatisticDTO> lowReturn = allStats.stream()
                                .sorted(Comparator.comparing(ProductStatisticDTO::getReturnRate))
                                .limit(5)
                                .collect(Collectors.toList());

                return StatisticsResponseDTO.builder()
                                .highReturnProducts(highReturn)
                                .lowReturnProducts(lowReturn)
                                .allProductStats(allStats)
                                .build();
        }

        // 장기 요양 등급 만료 임박 카운트 조회
        @Override
        public CareLevelExpirationCountDTO getCareLevelExpirationCounts() {
                return statisticsMapper.selectCareLevelExpirationCounts();
        }

        // 월별 잠재고객 및 계약 성사 고객 수 조회
        @Override
        public List<org.ateam.oncare.statistics.query.dto.MonthlyClientStatsDTO> getMonthlyClientStats() {
                return statisticsMapper.selectMonthlyClientStats();
        }

        // 미배정 수급자 수 조회
        @Override
        public int getUnassignedBeneficiariesCount() {
                return statisticsMapper.countUnassignedBeneficiaries();
        }

        // 대기 중인 전자 결재 수 조회 (내가 결재자인 경우)
        @Override
        public int getPendingApprovalsCount(int employeeId) {
                return statisticsMapper.countPendingApprovals(employeeId);
        }

        // 월별 신규 수급자 및 탈퇴 수급자 수 조회
        @Override
        public List<org.ateam.oncare.statistics.query.dto.MonthlyBeneficiaryStatsDTO> getMonthlyBeneficiaryStats() {
                return statisticsMapper.selectMonthlyBeneficiaryStats();
        }

        // 미배정 수급자 리스트 조회
        @Override
        public List<org.ateam.oncare.statistics.query.dto.UnassignedBeneficiaryDTO> getUnassignedBeneficiaries() {
                return statisticsMapper.selectUnassignedBeneficiaries();
        }

        // 이탈 징후 수급자 리스트 조회
        @Override
        public List<org.ateam.oncare.statistics.query.dto.ChurnRiskBeneficiaryDTO> getChurnRiskBeneficiaries() {
                return statisticsMapper.selectChurnRiskBeneficiaries();
        }

        // 위험등급별 수급자 현황 조회
        @Override
        public List<org.ateam.oncare.statistics.query.dto.RiskLevelBeneficiaryCountDTO> getRiskLevelBeneficiaryCounts() {
                return statisticsMapper.selectRiskLevelBeneficiaryCounts();
        }

        // 장기요양등급별 수급자 현황 조회
        @Override
        public List<org.ateam.oncare.statistics.query.dto.CareGradeBeneficiaryCountDTO> getCareGradeBeneficiaryCounts() {
                return statisticsMapper.selectCareGradeBeneficiaryCounts();
        }

        /**
         * 연체 고객 정보 조회
         * 
         * @return 연체 고객 목록
         */
        @Override
        public List<org.ateam.oncare.statistics.query.dto.OverdueInvoiceDTO> getOverdueBeneficiaries() {
                return statisticsMapper.selectOverdueBeneficiaries();
        }
}
