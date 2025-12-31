package org.ateam.oncare.beneficiary.command.repository;

import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.command.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BeneficiaryUpdateRepository extends JpaRepository<Beneficiary, Long> {
    @Modifying(clearAutomatically = true) // 상담 작성 시 자동으로 update
    @Query("UPDATE Beneficiary b SET b.lastCounselDate = :date WHERE b.id = :id")
    void updateLastCounselDate(@Param("id") Long id, @Param("date") LocalDateTime date);

    List<Beneficiary> findByName(String name);

    List<Beneficiary> findByNameAndStatus(String name, Boolean i);
}