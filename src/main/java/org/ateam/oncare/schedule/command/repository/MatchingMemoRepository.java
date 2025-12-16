package org.ateam.oncare.schedule.command.repository;

import org.ateam.oncare.schedule.command.entity.MatchingMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchingMemoRepository extends JpaRepository<MatchingMemo, Long> {

    Optional<MatchingMemo> findByMatchingIdAndMemoDate(Long matchingId, LocalDate memoDate);

    boolean existsByMatchingIdAndMemoDate(Long matchingId, LocalDate memoDate);
}