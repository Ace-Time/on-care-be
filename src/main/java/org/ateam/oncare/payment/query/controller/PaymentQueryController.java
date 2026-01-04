package org.ateam.oncare.payment.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.payment.query.dto.PaymentCategoryResponse;
import org.ateam.oncare.payment.query.dto.PaymentDashboardResponse;
import org.ateam.oncare.payment.query.dto.PaymentDetailResponse;
import org.ateam.oncare.payment.query.dto.PaymentListResponse;
import org.ateam.oncare.payment.query.dto.PaymentSearchCondition;
import org.ateam.oncare.payment.query.dto.PageResponse;
import org.ateam.oncare.payment.query.service.PaymentQueryService;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentQueryController {

    private final PaymentQueryService paymentQueryService;

    /**
     * 1. 대시보드 통계 조회
     * URL: GET /api/payments/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<PaymentDashboardResponse> getDashboardStats(
            @RequestParam(required = false) Long employeeId,
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        Long targetEmployeeId = resolveEmployeeId(employeeId, loginUser);
        PaymentDashboardResponse response = paymentQueryService.getDashboardStats(targetEmployeeId);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. 결재 목록 조회
     * URL: GET /api/payments
     */
    @GetMapping
    public ResponseEntity<PageResponse<PaymentListResponse>> getPaymentList(
            @ModelAttribute PaymentSearchCondition condition,
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        if (condition.getEmployeeId() == null && loginUser != null) {
            condition.setEmployeeId(loginUser.getId());
        }

        return ResponseEntity.ok(paymentQueryService.getPaymentList(condition));
    }

    private Long resolveEmployeeId(Long paramId, EmployeeImpl loginUser) {
        if (paramId != null) {
            return paramId;
        }
        if (loginUser != null) {
            return loginUser.getId();
        }
        throw new IllegalArgumentException("Employee ID is required via parameter or authentication.");
    }

    /**
     * 3. 결재 상세 조회
     * URL: GET /api/payments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailResponse> getPaymentDetail(@PathVariable Long id) {
        return ResponseEntity.ok(paymentQueryService.getPaymentDetail(id));
    }

    /**
     * 4. 결재 카테고리 목록 조회
     * URL: GET /api/payments/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<PaymentCategoryResponse>> getPaymentCategories() {
        return ResponseEntity.ok(paymentQueryService.getPaymentCategories());
    }
}