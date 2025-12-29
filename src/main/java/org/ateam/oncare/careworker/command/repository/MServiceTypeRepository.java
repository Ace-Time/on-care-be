package org.ateam.oncare.careworker.command.repository;

import org.ateam.oncare.careworker.command.entity.MServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MServiceTypeRepository extends JpaRepository<MServiceType, Long> {
}
