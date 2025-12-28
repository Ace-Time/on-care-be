package org.ateam.oncare.matching.command.repository;

import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitScheduleCareWorkerChangeRepository extends JpaRepository<VisitSchedule, Long> {
}