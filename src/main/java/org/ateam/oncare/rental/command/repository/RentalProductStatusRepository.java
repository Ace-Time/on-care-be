package org.ateam.oncare.rental.command.repository;

import org.ateam.oncare.rental.command.entity.RentalProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalProductStatusRepository extends JpaRepository<RentalProductStatus, Long> {
}
