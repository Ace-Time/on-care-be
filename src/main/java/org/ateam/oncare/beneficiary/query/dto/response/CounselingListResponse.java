package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CounselingListResponse {

    private Long beneficiaryId;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long counselingId;
        private String counselingDate;   // yyyy-MM-dd
        private String counselingType;
        private String satisfaction;
        private String visitPurpose;
        private Long beneficiaryId;
    }
}