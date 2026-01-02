package org.ateam.oncare.counsel.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class SaveStageDataResponse {

    private String message;
    private boolean success;

    public SaveStageDataResponse(String message) {
        this.message = message;
        this.success = true;
    }

    // 기본 생성자로 성공 응답 반환
    public SaveStageDataResponse() {
        this.message = "단계 데이터가 성공적으로 저장되었습니다.";
        this.success = true;
    }
}
