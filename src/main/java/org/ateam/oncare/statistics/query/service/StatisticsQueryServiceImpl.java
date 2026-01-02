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

        @Override
        public CareLevelExpirationCountDTO getCareLevelExpirationCounts() {
                return statisticsMapper.selectCareLevelExpirationCounts();
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.MonthlyClientStatsDTO> getMonthlyClientStats() {
                return statisticsMapper.selectMonthlyClientStats();
        }

        @Override
        public int getUnassignedBeneficiariesCount() {
                return statisticsMapper.countUnassignedBeneficiaries();
        }

        @Override
        public int getPendingApprovalsCount(int employeeId) {
                return statisticsMapper.countPendingApprovals(employeeId);
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.MonthlyBeneficiaryStatsDTO> getMonthlyBeneficiaryStats() {
                return statisticsMapper.selectMonthlyBeneficiaryStats();
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.UnassignedBeneficiaryDTO> getUnassignedBeneficiaries() {
                return statisticsMapper.selectUnassignedBeneficiaries();
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.ChurnRiskBeneficiaryDTO> getChurnRiskBeneficiaries() {
                return statisticsMapper.selectChurnRiskBeneficiaries();
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.RiskLevelBeneficiaryCountDTO> getRiskLevelBeneficiaryCounts() {
                return statisticsMapper.selectRiskLevelBeneficiaryCounts();
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.CareGradeBeneficiaryCountDTO> getCareGradeBeneficiaryCounts() {
                return statisticsMapper.selectCareGradeBeneficiaryCounts();
        }

        @Override
        public List<org.ateam.oncare.statistics.query.dto.OverdueInvoiceDTO> getOverdueBeneficiaries() {
                return statisticsMapper.selectOverdueBeneficiaries();
        }
}
