package org.ateam.oncare.statistics.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.statistics.query.dto.CareLevelExpirationCountDTO;
import org.ateam.oncare.statistics.query.dto.OverdueInvoiceDTO;
import org.ateam.oncare.statistics.query.dto.StatisticsResponseDTO;
import org.ateam.oncare.statistics.query.service.StatisticsQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsQueryController {

    private final StatisticsQueryService statisticsQueryService;
    private final org.ateam.oncare.auth.security.JwtTokenProvider jwtTokenProvider;

    /**
     * 상품 수익률 통계 조회
     * - 수익률 높은 용품 Top 5
     * - 수익률 낮은 용품 Top 5
     * - 각 제품 별 구입금액 합계 (전체 목록)
     */
    @GetMapping("/products")
    public ResponseEntity<StatisticsResponseDTO> getProductStatistics() {
        return ResponseEntity.ok(statisticsQueryService.getProductStatistics());
    }

    /**
     * 장기요양등급 만료 임박 수급자 수 조회 (90, 60, 45일 전)
     */
    @GetMapping("/care-level-expiration")
    public ResponseEntity<CareLevelExpirationCountDTO> getCareLevelExpirationCounts() {
        return ResponseEntity.ok(statisticsQueryService.getCareLevelExpirationCounts());
    }

    /**
     * 월별 잠재고객 및 계약 성사 고객 수 조회 (최근 12개월)
     */
    @GetMapping("/monthly-clients")
    public ResponseEntity<List<org.ateam.oncare.statistics.query.dto.MonthlyClientStatsDTO>> getMonthlyClientStats() {
        return ResponseEntity.ok(statisticsQueryService.getMonthlyClientStats());
    }

    /**
     * 미배정 수급자 수 조회
     */
    @GetMapping("/unassigned-beneficiaries-count")
    public ResponseEntity<Integer> getUnassignedBeneficiariesCount() {
        return ResponseEntity.ok(statisticsQueryService.getUnassignedBeneficiariesCount());
    }

    /**
     * 요청 결재(대기 중인 전자 결재) 수 조회
     * - Request Header의 Authorization(Bearer Token) 필요
     */
    @GetMapping("/pending-approvals-count")
    public ResponseEntity<Integer> getPendingApprovalsCount(
            @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 토큰 없음: 0 리턴 (또는 에러 처리)
            return ResponseEntity.ok(0);
        }

        String token = authHeader.substring(7);
        try {
            if (jwtTokenProvider.validateAccessToken(token)) {
                io.jsonwebtoken.Claims claims = jwtTokenProvider.getClaimsFromAT(token);
                // "id" claim에서 직원 ID 추출
                Integer employeeId = Integer.parseInt(String.valueOf(claims.get("id")));
                return ResponseEntity.ok(statisticsQueryService.getPendingApprovalsCount(employeeId));
            }
        } catch (Exception e) {
            // 토큰 파싱 에러 등: 0 리턴
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(0);
    }

    /**
     * 월별 신규 및 탈퇴 수급자 수 조회 (최근 12개월)
     */
    @GetMapping("/monthly-beneficiaries")
    public ResponseEntity<List<org.ateam.oncare.statistics.query.dto.MonthlyBeneficiaryStatsDTO>> getMonthlyBeneficiaryStats() {
        return ResponseEntity.ok(statisticsQueryService.getMonthlyBeneficiaryStats());
    }

    /**
     * 미배정 수급자 리스트 조회
     */
    @GetMapping("/unassigned-beneficiaries")
    public ResponseEntity<List<org.ateam.oncare.statistics.query.dto.UnassignedBeneficiaryDTO>> getUnassignedBeneficiaries() {
        return ResponseEntity.ok(statisticsQueryService.getUnassignedBeneficiaries());
    }

    /**
     * 이탈 징후 수급자 리스트 조회
     * - 기준: 장기 미상담(60일 이상) 또는 최근 60일 내 상담에 부정적/이탈 키워드 포함
     */
    @GetMapping("/churn-risk-beneficiaries")
    public ResponseEntity<List<org.ateam.oncare.statistics.query.dto.ChurnRiskBeneficiaryDTO>> getChurnRiskBeneficiaries() {
        return ResponseEntity.ok(statisticsQueryService.getChurnRiskBeneficiaries());
    }

    /**
     * 위험등급별 수급자 현황 조회
     */
    @GetMapping("/risk-level-beneficiaries-count")
    public ResponseEntity<List<org.ateam.oncare.statistics.query.dto.RiskLevelBeneficiaryCountDTO>> getRiskLevelBeneficiaryCounts() {
        return ResponseEntity.ok(statisticsQueryService.getRiskLevelBeneficiaryCounts());
    }

    /**
     * 장기요양등급별 수급자 현황 조회
     */
    @GetMapping("/care-grade-beneficiaries-count")
    public ResponseEntity<List<org.ateam.oncare.statistics.query.dto.CareGradeBeneficiaryCountDTO>> getCareGradeBeneficiaryCounts() {
        return ResponseEntity.ok(statisticsQueryService.getCareGradeBeneficiaryCounts());
    }

    @GetMapping("/overdue-beneficiaries")
    public ResponseEntity<List<OverdueInvoiceDTO>> getOverdueBeneficiaries() {
        return ResponseEntity.ok(statisticsQueryService.getOverdueBeneficiaries());
    }
}
