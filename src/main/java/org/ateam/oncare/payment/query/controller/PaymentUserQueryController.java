package org.ateam.oncare.payment.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import org.ateam.oncare.payment.query.dto.DepartmentTreeResponse;
import org.ateam.oncare.payment.query.dto.PaymentUserResponse;
import org.ateam.oncare.payment.query.service.PaymentQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments/users")
@RequiredArgsConstructor
public class PaymentUserQueryController {

    private final PaymentQueryService paymentQueryService;

    /**
     * 직원 검색 (키워드)
     * GET /api/payments/users/search
     */
    @GetMapping("/search")
    public ResponseEntity<List<PaymentUserResponse>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "true") boolean excludeSelf,
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        Long myId = (loginUser != null) ? loginUser.getId() : null;
        return ResponseEntity.ok(paymentQueryService.searchUsers(keyword, excludeSelf, myId));
    }

    /**
     * 최근 결재선 조회
     * GET /api/payments/users/recent-approvers
     */
    @GetMapping("/recent-approvers")
    public ResponseEntity<List<PaymentUserResponse>> getRecentApprovers(
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(paymentQueryService.getRecentApprovers(loginUser.getId(), limit));
    }

    /**
     * 우리 부서원 조회
     * GET /api/payments/users/my-department
     */
    @GetMapping("/my-department")
    public ResponseEntity<List<PaymentUserResponse>> getMyDepartmentMembers(
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(paymentQueryService.getMyDepartmentMembers(loginUser.getId()));
    }

    /**
     * 즐겨찾기 목록 조회
     * GET /api/payments/favorites/approvers
     */
    @GetMapping("/favorites/approvers")
    public ResponseEntity<List<PaymentUserResponse>> getFavoriteApprovers(
            @AuthenticationPrincipal EmployeeImpl loginUser) {

        if (loginUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(paymentQueryService.getFavoriteApprovers(loginUser.getId()));
    }

    /**
     * 조직도 트리 조회
     * GET /api/payments/departments/tree
     */
    @GetMapping("/department-tree")
    public ResponseEntity<List<DepartmentTreeResponse>> getDepartmentTree() {
        return ResponseEntity.ok(paymentQueryService.getDepartmentTree());
    }
}
