package org.ateam.oncare.beneficiary.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormListItem {

    private Long formId;
    private String originalFileName;
    private String createdAt;     // yyyy-MM-dd (SQL DATE_FORMAT 결과)
    private String mimeType;
    private String reFileName;
    private String filePath;
    private String categoryName;
    private String fileSize;
}