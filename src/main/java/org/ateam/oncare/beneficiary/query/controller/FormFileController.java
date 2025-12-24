package org.ateam.oncare.beneficiary.query.controller;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.FormFileInfo;
import org.ateam.oncare.beneficiary.query.service.FormFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class FormFileController {

    private final FormFileService service;

    @GetMapping("/{beneficiaryId}/forms/{formId}/preview")
    public ResponseEntity<Resource> preview(
            @PathVariable Long beneficiaryId,
            @PathVariable Long formId
    ) {
        return serve(beneficiaryId, formId, true);
    }

    @GetMapping("/{beneficiaryId}/forms/{formId}/download")
    public ResponseEntity<Resource> download(
            @PathVariable Long beneficiaryId,
            @PathVariable Long formId
    ) {
        return serve(beneficiaryId, formId, false);
    }

    private ResponseEntity<Resource> serve(Long beneficiaryId, Long formId, boolean inline) {
        // ✅ 수급자 기준 접근 가능 여부 검증 + 파일정보 조회
        FormFileInfo info = service.getAllowedFileInfo(beneficiaryId, formId);
        if (info == null) {
            // 보안상 403 대신 404로 숨기는 것도 좋음
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = service.getResource(info);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String mime = StringUtils.hasText(info.getMimeType()) ? info.getMimeType() : "application/pdf";

        String encodedName = UriUtils.encode(info.getOriginalFileName(), StandardCharsets.UTF_8);
        String disposition = (inline ? "inline" : "attachment") + "; filename*=UTF-8''" + encodedName;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mime))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .body(resource);
    }
}