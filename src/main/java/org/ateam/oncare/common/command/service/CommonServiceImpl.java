package org.ateam.oncare.common.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.common.command.entity.CareLevel;
import org.ateam.oncare.common.command.repository.CareLevelRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommonServiceImpl implements CommonService {

    private final CareLevelRepository repository;

    @Override
    public CareLevel getCareLevel(int i) {
        return repository.findById(i).orElseThrow();
    }
}
