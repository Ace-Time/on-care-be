package org.ateam.oncare.payment.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.payment.query.dto.PaymentCategoryResponse;
import org.ateam.oncare.payment.query.dto.PaymentDashboardResponse;
import org.ateam.oncare.payment.query.dto.PaymentDetailResponse;
import org.ateam.oncare.payment.query.dto.PaymentListResponse;
import org.ateam.oncare.payment.query.dto.PaymentSearchCondition;
import org.ateam.oncare.payment.query.dto.PaymentUserResponse;
import org.ateam.oncare.payment.query.dto.DepartmentTreeResponse;

import java.util.List;

@Mapper
public interface PaymentMapper {

    // 대시보드 통계 조회
    PaymentDashboardResponse selectDashboardStats(@Param("employeeId") Long employeeId);

    // 결재 목록 조회 (검색 및 필터링 포함)
    List<PaymentListResponse> selectPaymentList(PaymentSearchCondition condition);

    long selectPaymentListCount(PaymentSearchCondition condition);

    // 결재 상세 조회
    PaymentDetailResponse selectPaymentDetail(@Param("id") Long id);

    // 결재 상세 조회 시 결재 라인 가져오기
    List<PaymentDetailResponse.ApproverInfo> selectApproverList(@Param("paymentId") Long paymentId);

    // 결재 카테고리 목록 조회
    List<PaymentCategoryResponse> selectPaymentCategories();

    // 1. 직원 검색
    List<PaymentUserResponse> selectUsersByKeyword(@Param("keyword") String keyword,
            @Param("excludeSelf") boolean excludeSelf,
            @Param("myId") Long myId);

    // 2. 최근 결재선 조회
    List<PaymentUserResponse> selectRecentApprovers(@Param("myId") Long myId, @Param("limit") int limit);

    // 3. 우리 부서원 조회
    List<PaymentUserResponse> selectMyDepartmentMembers(@Param("myId") Long myId);

    // 4. 즐겨찾기 조회 (추후 구현 시 테이블 필요, 현재는 빈 리스트 반환용으로 선언만 하거나 더미 데이터 쿼리 작성)
    List<PaymentUserResponse> selectFavoriteApprovers(@Param("myId") Long myId);

    // 5. 부서 목록 조회 (조직도용)
    List<DepartmentTreeResponse> selectAllDepartments();

    // 6. 특정 부서 직원 조회 (조직도용)
    List<PaymentUserResponse> selectUsersByDeptId(@Param("deptId") Long deptId);
}