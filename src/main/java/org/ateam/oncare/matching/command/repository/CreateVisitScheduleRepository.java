package org.ateam.oncare.matching.command.repository;

import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateVisitScheduleRepository extends JpaRepository<VisitSchedule, Long> {
}