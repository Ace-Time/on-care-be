package org.ateam.oncare.beneficiary.query.dto.response;
// 공동 페이징 응답

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {

    private List<T> content;

    private int page;
    private int size;

    private long totalElements;
    private int totalPages;
}