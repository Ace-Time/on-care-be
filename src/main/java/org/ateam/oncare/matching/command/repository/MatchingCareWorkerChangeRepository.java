package org.ateam.oncare.matching.command.repository;

import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingCareWorkerChangeRepository extends JpaRepository<Matching, Long> {
}