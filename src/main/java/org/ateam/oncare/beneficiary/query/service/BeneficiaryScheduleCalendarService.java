package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.ScheduleCalendarResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryScheduleCalendarMapper;
import org.ateam.oncare.beneficiary.query.mapper.CalendarFixedVisitRow;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryScheduleCalendarService {

    private final BeneficiaryScheduleCalendarMapper mapper;

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter D  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HM = DateTimeFormatter.ofPattern("HH:mm");

    // 월 캘린더 조회
    public ScheduleCalendarResponse getMonthlyCalendar(Long beneficiaryId, int year, int month) {

        // 월 범위 계산
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEndExclusive = monthStart.plusMonths(1);

        LocalDateTime rangeStart = monthStart.atStartOfDay();
        LocalDateTime rangeEnd = monthEndExclusive.atStartOfDay();

        // DB 조회(visit_schedule)
        List<CalendarFixedVisitRow> fixed = mapper.selectFixedVisitsInRange(beneficiaryId, rangeStart, rangeEnd);

        // DB Row -> 캘린터 item 변환
        List<ScheduleCalendarResponse.Item> items = fixed.stream()
                .map(this::toItem)

                // 정렬
                // 1차 : 시작 시간 순, 2차: 같은 시간일 경우 ID 순
                // 하루에 일정 여러 개일 때 순서 보장
                .sorted(Comparator.comparing(ScheduleCalendarResponse.Item::getStartDt)
                        .thenComparing(ScheduleCalendarResponse.Item::getVisitScheduleId))
                .collect(Collectors.toList());

        // 날짜별 그룹핑
        Map<String, List<ScheduleCalendarResponse.Item>> byDate = items.stream()
                .collect(Collectors.groupingBy(i -> i.getStartDt().substring(0, 10), LinkedHashMap::new, Collectors.toList()));

        ScheduleCalendarResponse res = new ScheduleCalendarResponse();
        res.setYear(year);
        res.setMonth(month);
        res.setBeneficiaryId(beneficiaryId);

        // 모든 날짜 채우기
        LocalDate cur = monthStart;
        while (cur.isBefore(monthEndExclusive)) {
            ScheduleCalendarResponse.Day day = new ScheduleCalendarResponse.Day();
            String dateKey = cur.format(D);
            day.setDate(dateKey);
            // 일정이 없는 날짜도 빈 배열로 생성
            day.setItems(byDate.getOrDefault(dateKey, new ArrayList<>()));
            res.getDays().add(day);
            cur = cur.plusDays(1);
        }

        return res;
    }

    // DB에서 가져온 visit_schedule 1건을 캘린더 화면에 바로 쓸 수 있는 Item 객체로 “변환(매핑 + 가공)”하는 메서드야.
    private ScheduleCalendarResponse.Item toItem(CalendarFixedVisitRow r) {

        LocalDateTime st = LocalDateTime.parse(r.getStartDt(), DT);
        LocalDateTime et = LocalDateTime.parse(r.getEndDt(), DT);

        ScheduleCalendarResponse.Item item = new ScheduleCalendarResponse.Item();
        item.setVisitScheduleId(r.getVisitScheduleId());    // 일정번호

        item.setStartDt(r.getStartDt());                    // 계획시간시간
        item.setEndDt(r.getEndDt());                        // 계획종료시간

        item.setServiceTypeId(r.getServiceTypeId());        // 서비스타입 id
        item.setServiceTypeName(r.getServiceTypeName());    // 서비스타입 명

        item.setVisitStatus(r.getVisitStatus());            // 상태
        item.setCareWorkerName(r.getCareWorkerName());      // 요양보호사 이름
        item.setNote(r.getNote());                          // 메모

        //  제목(title) 포맷 변경
        item.setTitle(
                st.format(HM) + "-" +
                        et.format(HM) + " " +
                        r.getCareWorkerName() +
                        "(" + r.getServiceTypeName() + ")"
        );

        return item;
    }
}