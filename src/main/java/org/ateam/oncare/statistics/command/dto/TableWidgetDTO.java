/* 설명.
 *  리스트/랭킹용 DTO
 *   - 수익률 Top 5", "이탈 징후 리스트" 등을 표현할 때 사용
* */
package org.ateam.oncare.statistics.command.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TableWidgetDTO {
    private String title;               // 위젯 제목 (예: "수익률 Top 5 용품")
    private List<String> headers;       // 테이블 헤더 (예: ["상품명", "판매금액", "수익률"])

    // 데이터 본문
    // Map을 사용하여 유동적인 컬럼에 대응 (Key: 컬럼명, Value: 값)
    private List<Map<String, Object>> data;
}