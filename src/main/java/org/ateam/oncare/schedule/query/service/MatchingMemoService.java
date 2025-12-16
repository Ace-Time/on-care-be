package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.MatchingMemoDto;
import org.ateam.oncare.schedule.query.mapper.MatchingMemoMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MatchingMemoService {

    private final MatchingMemoMapper matchingMemoMapper;

    public MatchingMemoDto getMemo(Long matchingId, LocalDate memoDate) {
        return matchingMemoMapper.selectMemo(matchingId, memoDate);
    }
}