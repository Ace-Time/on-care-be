package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ateam.oncare.schedule.query.dto.ConfirmedScheduleMemoDto;
import org.ateam.oncare.schedule.query.mapper.ConfirmedScheduleMemoMapper;

@Service
@RequiredArgsConstructor
public class ConfirmedScheduleMemoService {

    private final ConfirmedScheduleMemoMapper confirmedScheduleMemoMapper;

    public ConfirmedScheduleMemoDto getMemo(Integer vsId) {
        ConfirmedScheduleMemoDto dto = confirmedScheduleMemoMapper.selectMemoByVsId(vsId);
        if (dto == null) {
            return ConfirmedScheduleMemoDto.builder()
                    .vsId(vsId)
                    .note("")
                    .build();
        }
        if (dto.getNote() == null) dto.setNote("");
        return dto;
    }
}