package org.ateam.oncare.beneficiary.query.dto.response;

/* 장기요양등급만료알림 안내 이력 조회 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoticeExpirationListResponse {

    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Integer noticeId;
        private String noticeDate; // yyyy-MM-dd HH:mm:ss
        private String memo;
        private Integer empId;
        private String empName;
    }
}