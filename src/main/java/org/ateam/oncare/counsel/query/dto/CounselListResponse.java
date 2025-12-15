package org.ateam.oncare.counsel.query.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CounselListResponse {
    private BigInteger counselHistoryId;
    private String counselCategoryName;
    private String detail;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime consultDate;
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime consultTime;
    private String counselorName;
}
