package org.ateam.oncare.payment.query.service;

import org.ateam.oncare.payment.query.dto.*;
import org.ateam.oncare.payment.query.dto.PaymentUserResponse;
import org.ateam.oncare.payment.query.dto.DepartmentTreeResponse;

import java.util.List;

public interface PaymentQueryService {

    // 대시보드 통계 조회
    PaymentDashboardResponse getDashboardStats(Long employeeId);

    // 결재 목록 조회
    PageResponse<PaymentListResponse> getPaymentList(PaymentSearchCondition condition);

    // 결재 상세 조회
    PaymentDetailResponse getPaymentDetail(Long id);

    List<PaymentCategoryResponse> getPaymentCategories();

    // --- 직원 검색 및 조직도 관련 ---
    List<PaymentUserResponse> searchUsers(String keyword, boolean excludeSelf, Long myId);

    List<PaymentUserResponse> getRecentApprovers(Long myId, int limit);

    List<PaymentUserResponse> getMyDepartmentMembers(Long myId);

    List<PaymentUserResponse> getFavoriteApprovers(Long myId);

    List<DepartmentTreeResponse> getDepartmentTree();
}