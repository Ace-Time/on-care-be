package org.ateam.oncare.schedule.command.repository;

import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitScheduleRepository extends JpaRepository<VisitSchedule, Long> {
}