/* 설명.
 *  KPI 카드용 DTO
 *   - 카드 위젯을 위한 DTO
* */
package org.ateam.oncare.statistics.command.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KpiCardDTO {
    private String title;       // 카드 제목 (예: "총 수급자")
    private String value;       // 주요 수치 (예: "156명", "12,000,000원") - 포맷팅은 백엔드 권장
    private String unit;        // 단위 (UI 아이콘 결정용, 예: "USER", "MONEY")
    private Double growthRate;  // 전월 대비 증감률 (예: 12.5 -> +12.5%)
    private String diffText;    // 증감 텍스트 (예: "전월 대비 12% 상승")
    private boolean isPositive; // 긍정적 신호인지 여부 (초록색/빨간색 색상 결정용)
}