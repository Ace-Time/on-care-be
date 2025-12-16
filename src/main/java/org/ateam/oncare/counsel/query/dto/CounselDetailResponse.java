package org.ateam.oncare.counsel.query.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CounselDetailResponse {
    private BigInteger counselHistoryId;
    private String counselCategoryName;
    private String detail;
    private String summary;
    private String followUp;
    private String followUpNecessary;
    private String counselorName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime consultDate;
}
