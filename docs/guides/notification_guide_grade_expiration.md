# [Guide] 수급자 장기요양등급 만료 알림 구현 가이드

이 문서는 **수급자의 장기요양등급(LTC Grade) 만료일**이 도래했을 때, 직원에게 알림을 발송하는 기능을 구현하기 위한 가이드입니다.
기존에 구현된 **보수교육 알림 스케줄러(`EducationScheduler`)** 구조를 기반으로 설명합니다.

---

## 1. 구현 로직 개요
- **목표**: 수급자의 인정 유효기간 만료일(`end_date`)을 매일 체크하여, 만료 30일(또는 설정된 기간) 전 알림 발송.
- **대상**: 관리 책임자 (사회복지사 등) 또는 담당 요양보호사. (여기서는 `Employee` 테이블의 직원에게 발송한다고 가정)

## 2. 데이터베이스 설정 (SQL)
먼저 알림 템플릿과 규칙을 DB에 등록해야 스케줄러가 작동할 수 있습니다.

### 2.1 템플릿 등록 (`notification_template`)
`template_type`을 **`GRADE_EXPIRE`**로 정의합니다. 내용에 `{name}`, `{date}`, `{dDay}` 등 변수를 사용할 수 있습니다.

```sql
INSERT INTO notification_template 
(title, content, template_type, created_at, is_active, created_by, severity, target_type_id) 
VALUES 
(
  '장기요양등급 만료 예정', 
  '{name} 수급자님의 장기요양등급 유효기간이 {dDay}일 남았습니다. ({date} 만료)', 
  'GRADE_EXPIRE', -- ★ 타입 식별자
  CURDATE(), 
  1, 1, 2, 1
);
```

### 2.2 규칙 등록 (`notification_rule`)
위 템플릿을 사용하여 **"며칠 전"**에 알림을 보낼지 설정합니다. (예: 30일 전)

```sql
INSERT INTO notification_rule 
(template_id, channel_type_id, offset_days, is_active, created_at, created_by, target_type) 
VALUES 
(
  (SELECT template_id FROM notification_template WHERE template_type='GRADE_EXPIRE' LIMIT 1), 
  1, 
  30, -- ★ 30일 전 알림
  1, 
  CURDATE(), 
  1, 
  'EMPLOYEE'
);
```

---

## 3. 백엔드 구현 절차

### 3.1 DTO 생성 (`GradeExpireAlertDTO`)
알림 발송에 필요한 정보를 담을 DTO를 생성합니다.
- **위치**: `org.ateam.oncare.beneficiary.query.dto` (권장)

```java
@Getter
@Setter
public class GradeExpireAlertDTO {
    private Long beneficiaryId;
    private String beneficiaryName;
    private LocalDate endDate; // 만료일
    private Long managerId;    // 알림 받을 직원 ID (예: 담당 사회복지사)
    private int dDay;          // 남은 일수
}
```

### 3.2 Mapper 쿼리 작성
수급자 정보(`beneficiary`)와 등급 정보(`beneficiary_care_level`)를 조인하여 만료 임박자를 조회합니다.
- **위치**: `BeneficiaryMapper.xml` (또는 관련 매퍼)

```xml
<select id="selectGradeExpireAlerts" resultType="...GradeExpireAlertDTO">
    SELECT 
        b.id AS beneficiaryId,
        b.name AS beneficiaryName,
        cl.end_date AS endDate,
        DATEDIFF(cl.end_date, CURDATE()) AS dDay,
        -- 알림 받을 직원(manager) ID 예시 (필요시 조인)
        1 AS managerId 
    FROM beneficiary b
    JOIN beneficiary_care_level cl ON b.id = cl.beneficiary_id
    WHERE cl.end_date &lt;= #{thresholdDate}
      AND cl.end_date &gt; CURDATE() -- 이미 지난 건 제외할지 여부 결정
      AND b.status = 1 -- 서비스 중인 수급자만
</select>
```

### 3.3 스케줄러 구현 (`GradeScheduler`)
`EducationScheduler`를 참고하여 새로운 스케줄러를 만듭니다.
- **위치**: `org.ateam.oncare.beneficiary.scheduler` (패키지 생성 필요할 수음)

```java
@Component
@RequiredArgsConstructor
public class GradeScheduler {
    
    private final NotificationTemplateRepository templateRepository;
    private final NotificationRuleRepository ruleRepository;
    private final BeneficiaryMapper beneficiaryMapper; // 위에서 만든 매퍼
    private final NotificationCommandService notificationService;

    @Scheduled(cron = "0 0 9 * * *") // 매일 09:00
    @Transactional
    public void checkGradeExpiration() {
        // 1. 템플릿 조회 (GRADE_EXPIRE)
        List<NotificationTemplate> templates = templateRepository
                .findByTemplateTypeAndIsActive("GRADE_EXPIRE", 1);
        
        // 2. 규칙 조회 및 실행 Loop
        for (NotificationTemplate template : templates) {
             List<NotificationRule> rules = ruleRepository
                    .findByTemplateIdAndIsActive(template.getTemplateId(), 1);

             for (NotificationRule rule : rules) {
                 processRule(template, rule);
             }
        }
    }

    private void processRule(NotificationTemplate template, NotificationRule rule) {
        int offsetDays = rule.getOffsetDays();
        LocalDate thresholdDate = LocalDate.now().plusDays(offsetDays);

        // 3. 대상자 조회
        List<GradeExpireAlertDTO> alerts = beneficiaryMapper.selectGradeExpireAlerts(thresholdDate);

        // 4. 알림 발송
        for (GradeExpireAlertDTO alert : alerts) {
            String content = template.getContent()
                    .replace("{name}", alert.getBeneficiaryName())
                    .replace("{dDay}", String.valueOf(alert.getDDay()))
                    .replace("{date}", alert.getEndDate().toString());
            
            notificationService.sendCustom(
                alert.getManagerId(), // 수신자 ID
                template.getTitle(), 
                content, 
                template.getTemplateType(),
                template.getSeverity()
            );
        }
    }
}
```

## 4. 참고 사항
- **수신자 설정 (`managerId`)**: 위 예시에서는 임의로 `1`로 설정했지만, 실제로는 수급자와 연결된 **담당 직원(Employee) ID**를 조회해서 넣어야 합니다. (`beneficiary` 테이블이나 배정 테이블 조인 필요)
- **테스트**: `EducationScheduler`처럼 수동 트리거 API를 만들어서 테스트하면 편리합니다.
