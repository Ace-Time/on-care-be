//package org.ateam.oncare.beneficiary.command.repository;
//
//import org.ateam.oncare.beneficiary.command.entity.CostOfBeneficiary;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface CostOfBeneficiaryRepository extends JpaRepository<CostOfBeneficiary, Integer> {
//
//    /**
//     * 특정 수급자의 특정 월 비용 조회
//     * @param beneficiaryId 수급자 ID
//     * @param month "YYYY-MM" 형식
//     * @return 해당 월의 비용 정보
//     */
//    Optional<CostOfBeneficiary> findByBeneficiaryIdAndMonth(Long beneficiaryId, String month);
//}
