package org.ateam.oncare.common.command.repository;

import org.ateam.oncare.common.command.entity.CareLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareLevelRepository extends JpaRepository<CareLevel,Integer> {
}
