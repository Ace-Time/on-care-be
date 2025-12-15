package org.ateam.oncare.counsel.query.service;

import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.query.mapper.CounselQueryMapper;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@Slf4j
public class CounselQueryServiceImpl implements CounselQueryService {
    private final CounselQueryMapper counselQueryMapper;

    @Autowired
    public CounselQueryServiceImpl(CounselQueryMapper counselQueryMapper) {
        this.counselQueryMapper = counselQueryMapper;
    }



}
