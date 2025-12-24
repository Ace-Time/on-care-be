package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class CounselHistoryListResponse {
    private Long beneficiaryId;
    private List<Item> items;

    @Getter @Setter
    public static class Item {
        private Long counselHistoryId;
        private String consultDate;     // yyyy-MM-dd
        private String summary;
        private Long beneficiaryId;
        private String categoryName;
        private String detail;           // 상담 내용

    }
}