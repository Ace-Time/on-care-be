package org.ateam.oncare.employee.command.repository;

import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    boolean existsByPotentialCustomerId(Long potentialCustomerId);

    @Query("SELECT COUNT(b) > 0 FROM Beneficiary b " +
            "WHERE REPLACE(b.phone, '-', '') = :phone")
    boolean existsByPhoneNormalized(@Param("phone") String phone);
}
