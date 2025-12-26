package org.ateam.oncare.matching.query.dto;

import lombok.Data;
import java.util.List;

@Data
public class CareWorkerDetailDto {

    private Long careWorkerId;

    /* 기본 정보 */
    private String name;
    private String gender;
    private String address;
    private String phone;

    /* 제공 서비스 */
    private List<String> serviceTypes;

    /* 보유 태그 */
    private List<String> tags;

    /* 보유 자격증 */
    private List<String> certificates;

    /* 현재 근무 중인 시간 */
    private List<WorkingTimeDto> workingTimes;
}