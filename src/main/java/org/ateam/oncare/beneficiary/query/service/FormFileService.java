package org.ateam.oncare.beneficiary.query.service;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.FormFileInfo;
import org.ateam.oncare.beneficiary.query.mapper.FormAccessMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FormFileService {

    private final FormAccessMapper accessMapper;

    /**
     * beneficiaryId 기준으로 formId 접근 가능하면 파일 정보 반환, 아니면 null
     */
    public FormFileInfo getAllowedFileInfo(Long beneficiaryId, Long formId) {
        return accessMapper.selectAllowedFormFileInfo(beneficiaryId, formId);
    }

    public Resource getResource(FormFileInfo info) {
        String filePath = info.getFilePath();

        Path absPath = Paths.get(System.getProperty("user.dir"))
                .resolve(filePath.startsWith("/") ? filePath.substring(1) : filePath);

        return new FileSystemResource(absPath);
    }
}