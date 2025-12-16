package org.ateam.oncare.schedule.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.entity.MatchingMemo;
import org.ateam.oncare.schedule.command.repository.MatchingMemoRepository;
import org.ateam.oncare.schedule.command.dto.MatchingMemoUpsertRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingMemoCommandService {

    private final MatchingMemoRepository matchingMemoRepository;

    public void upsertMemo(MatchingMemoUpsertRequest request) {

        MatchingMemo memo = matchingMemoRepository
                .findByMatchingIdAndMemoDate(
                        request.getMatchingId(),
                        request.getMemoDate()
                )
                .orElseGet(() -> MatchingMemo.builder()
                        .matchingId(request.getMatchingId())
                        .memoDate(request.getMemoDate())
                        .build()
                );

        memo.setContent(request.getContent());

        matchingMemoRepository.save(memo);
    }
}