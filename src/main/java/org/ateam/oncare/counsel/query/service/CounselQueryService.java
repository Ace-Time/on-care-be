package org.ateam.oncare.counsel.query.service;


import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.jspecify.annotations.Nullable;

public interface CounselQueryService {

    @Nullable CounselListResponse searchCounsels(String keyword);
}
