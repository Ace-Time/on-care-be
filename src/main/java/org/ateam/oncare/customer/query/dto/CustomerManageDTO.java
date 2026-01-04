package org.ateam.oncare.customer.query.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerManageDTO {

    /**
     * 고객 검색 조건
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCondition {
        private String keyword;           // 이름 또는 전화번호
        private String category;          // 카테고리 (CHURN_RISK, EXPIRATION_RISK, MARKETING_OPPORTUNITY)
        private String subCategory;       // 서브카테고리
        private Integer page;
        private Integer size;
        private Integer offset;
    }

    /**
     * 수급자 목록 응답
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeneficiaryListItem {
        private Long beneficiaryId;
        private String name;
        private String phone;
        private String gender;
        private LocalDate birthdate;
        private String address;
        private String careLevel;
        private LocalDate careLevelEndDate;
        private String riskLevel;
        private String status;
        private LocalDateTime lastCounselDate;
        private String managerName;        // 담당 요양보호사

        // 카테고리 표시용 플래그
        private Boolean isChurnRisk;       // 이탈위험기간임박
        private Boolean hasComplaint;      // 불만상담접수
        private Boolean hasTermination;    // 해지상담등록
        private Boolean isCareLevelExpiring; // 장기요양등급만료임박
        private Boolean isContractExpiring;  // 계약만료임박
        private Boolean hasRentalCounsel;    // 렌탈상담등록
    }

    /**
     * 페이징 응답
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResponse<T> {
        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }

    /**
     * 고객 관리 프로세스 상세 (5단계용)
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerManageDetail {
        private Long beneficiaryId;
        private String name;

        // 이탈위험 관련
        private Boolean isChurnRisk;
        private Integer daysSinceLastCounsel;
        private LocalDateTime lastCounselDate;

        // 불만상담 관련
        private Boolean hasComplaint;
        private CounselSummary latestComplaint;

        // 해지상담 관련
        private Boolean hasTermination;
        private CounselSummary latestTermination;
        private LocalDate plannedTerminationDate;

        // 만료 관련
        private Boolean isCareLevelExpiring;
        private LocalDate careLevelEndDate;
        private Integer daysUntilCareLevelExpiry;

        private Boolean isContractExpiring;
        private LocalDate contractEndDate;
        private Integer daysUntilContractExpiry;

        // 마케팅 기회
        private Boolean hasRentalCounsel;
        private CounselSummary latestRentalCounsel;

        // 담당 요양보호사 정보
        private Integer careWorkerId;
        private String careWorkerName;
    }

    /**
     * 상담 요약 정보
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CounselSummary {
        private Long counselId;
        private LocalDateTime consultDate;
        private String summary;
        private String detail;
        private String followUp;
        private String followUpNecessary;
        private String counselorName;
        private String categoryName;
    }

    /**
     * 후속조치 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUpRequest {
        private Long beneficiaryId;
        private Long counselId;
        private String followUpContent;
        private String actionType;  // COUNSEL_REQUEST, COMPLAINT_RESOLVE, TERMINATION_FOLLOWUP, RENTAL_NOTIFY
    }

    /**
     * 해지 등록 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminationRequest {
        private Long beneficiaryId;
        private LocalDate plannedTerminationDate;
        private String terminationReason;
    }

    /**
     * 카테고리 카운트 응답
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCount {
        // 이탈위험징후포착
        private int churnRiskCount;
        private int complaintCount;
        private int terminationCount;

        // 만료기간위험
        private int careLevelExpiringCount;
        private int contractExpiringCount;

        // 마케팅기회포착
        private int rentalCounselCount;
    }
}
