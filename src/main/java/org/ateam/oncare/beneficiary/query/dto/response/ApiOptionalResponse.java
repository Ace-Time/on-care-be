package org.ateam.oncare.beneficiary.query.dto.response;

// 기초평가 공통 응답dto (데이터 있고 없고의 결과화면 출력해줌)

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiOptionalResponse<T> {

    private boolean exists;
    private String message;
    private T data;

    private ApiOptionalResponse(boolean exists, String message, T data) {
        this.exists = exists;
        this.message = message;
        this.data = data;
    }

    /** 데이터 있음 */
    public static <T> ApiOptionalResponse<T> of(T data) {
        return new ApiOptionalResponse<>(true, null, data);
    }

    /** 데이터 없음 */
    public static <T> ApiOptionalResponse<T> empty(String message) {
        return new ApiOptionalResponse<>(false, message, null);
    }
}