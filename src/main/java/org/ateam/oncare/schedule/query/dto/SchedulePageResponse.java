package org.ateam.oncare.schedule.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SchedulePageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long total;
}