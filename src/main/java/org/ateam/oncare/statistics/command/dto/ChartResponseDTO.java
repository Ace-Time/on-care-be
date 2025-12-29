/* 설명.
 *  차트용 표준 DTO
 *   - 막대, 꺾은선, 파이(Pie) 차트 모두
* */
package org.ateam.oncare.statistics.command.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChartResponseDTO {

    // 차트 제목 (예: "월별 매출 현황")
    private String title;

    // X축 라벨 (예: ["1월", "2월", "3월"...] 또는 ["전동침대", "휠체어"...])
    // 파이 차트의 경우 각 조각의 이름이 됩니다.
    private List<String> categories;

    // Y축 데이터 (다중 시리즈 지원을 위해 List로 선언)
    private List<SeriesDto> series;

    // 내부 정적 클래스: 실제 데이터 묶음
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SeriesDto {
        // 시리즈 이름 (예: "매출액", "순이익", "잠재고객")
        private String name;

        // 실제 데이터 값 (예: [1000, 2000, 1500...])
        // 금액(Decimal), 개수(Long) 모두 수용하기 위해 Number 타입 사용
        private List<? extends Number> data;

        // (선택사항) 혼합 차트용: 이 시리즈가 'bar'인지 'line'인지 지정
        // 필요 없으면 null 처리
        private String type;
    }
}