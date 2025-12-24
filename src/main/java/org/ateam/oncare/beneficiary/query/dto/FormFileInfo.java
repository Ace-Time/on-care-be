package org.ateam.oncare.beneficiary.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormFileInfo {
    private Long formId;
    private String originalFileName;
    private String mimeType;
    private String filePath;
    private String fileSize;

}