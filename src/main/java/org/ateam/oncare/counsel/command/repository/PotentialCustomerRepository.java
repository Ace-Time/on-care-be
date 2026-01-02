package org.ateam.oncare.counsel.command.repository;


import org.ateam.oncare.counsel.command.entity.PotentialCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PotentialCustomerRepository extends JpaRepository<PotentialCustomer,Long> {
    @Modifying(clearAutomatically = true) // ★ 필수: UPDATE/DELETE 쿼리임을 명시
    @Query("UPDATE PotentialCustomer p SET p.lastCounselDate = :date WHERE p.id = :id")
    void updateLastCounselDate(@Param("id") Long id, @Param("date") LocalDateTime date);
}
