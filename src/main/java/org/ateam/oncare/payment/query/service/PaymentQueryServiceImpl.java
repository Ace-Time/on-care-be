package org.ateam.oncare.payment.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.payment.query.dto.*;
import org.ateam.oncare.payment.query.mapper.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentMapper paymentMapper;

    // 대시보드 통계 조회
    @Override
    public PaymentDashboardResponse getDashboardStats(Long employeeId) {
        return paymentMapper.selectDashboardStats(employeeId);
    }

    // 결재 문서 목록 조회
    @Override
    public PageResponse<PaymentListResponse> getPaymentList(PaymentSearchCondition condition) {
        long total = paymentMapper.selectPaymentListCount(condition);
        List<PaymentListResponse> list = paymentMapper.selectPaymentList(condition);

        return new PageResponse<>(list, total, condition.getPage(), condition.getSize());
    }

    // 결재 문서 상세 조회
    @Override
    public PaymentDetailResponse getPaymentDetail(Long id) {
        // 1. 상세 정보 조회
        PaymentDetailResponse detail = paymentMapper.selectPaymentDetail(id);

        // 2. 결재 라인 조회 및 주입 (데이터가 있을 경우에만)
        if (detail != null) {
            List<PaymentDetailResponse.ApproverInfo> approvers = paymentMapper.selectApproverList(id);
            detail.setApproverList(approvers);
        }

        return detail;
    }

    // 결재 카테고리(유형) 목록 조회
    @Override
    public List<PaymentCategoryResponse> getPaymentCategories() {
        return paymentMapper.selectPaymentCategories();
    }

    // --- 직원 검색 및 조직도 관련 기능 ---
    // 직원 검색
    @Override
    public List<PaymentUserResponse> searchUsers(String keyword, boolean excludeSelf, Long myId) {
        return paymentMapper.selectUsersByKeyword(keyword, excludeSelf, myId);
    }

    // 최근 결재선 조회
    @Override
    public List<PaymentUserResponse> getRecentApprovers(Long myId, int limit) {
        return paymentMapper.selectRecentApprovers(myId, limit);
    }

    // 우리 부서원 조회
    @Override
    public List<PaymentUserResponse> getMyDepartmentMembers(Long myId) {
        return paymentMapper.selectMyDepartmentMembers(myId);
    }

    // 자주 찾는 승인자(즐겨찾기) 조회
    @Override
    public List<PaymentUserResponse> getFavoriteApprovers(Long myId) {
        return paymentMapper.selectFavoriteApprovers(myId);
    }

    // 조직도 트리 조회
    @Override
    public List<DepartmentTreeResponse> getDepartmentTree() {
        // 1. 부서 목록 조회
        List<DepartmentTreeResponse> departments = paymentMapper.selectAllDepartments();

        // 2. 각 부서별 직원 조회 (N+1 문제 발생 가능하나, 부서 개수가 적다고 가정하고 단순 구현)
        for (DepartmentTreeResponse dept : departments) {
            List<PaymentUserResponse> members = paymentMapper.selectUsersByDeptId(dept.getDeptId());
            dept.setMembers(members);
            // dept.setChildrenDepts(...); // 하위 부서 로직은 현재 DB 구조 불명확하여 생략
        }

        return departments;
    }
}