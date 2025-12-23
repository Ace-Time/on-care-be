SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS cost_of_beneficiary_record;
DROP TABLE IF EXISTS beneficiary_count;
DROP TABLE IF EXISTS care_service_count;
DROP TABLE IF EXISTS notice_expiration;
DROP TABLE IF EXISTS expiration_of_care_level;
DROP TABLE IF EXISTS invoice;
DROP TABLE IF EXISTS tag_of_beneficiary;
DROP TABLE IF EXISTS emergency;
DROP TABLE IF EXISTS beneficiary_schedule;
DROP TABLE IF EXISTS application_form;
DROP TABLE IF EXISTS riskOfMember;
DROP TABLE IF EXISTS beneficiary_care_level;
DROP TABLE IF EXISTS cost_of_beneficiary;
DROP TABLE IF EXISTS guardian;
DROP TABLE IF EXISTS contract;
DROP TABLE IF EXISTS m_form_category;
DROP TABLE IF EXISTS m_care_level;
DROP TABLE IF EXISTS beneficiary;

-- 요양사
DROP TABLE IF EXISTS care_logs;
DROP TABLE IF EXISTS counseling_logs;
DROP TABLE IF EXISTS basic_evaluations;
DROP TABLE IF EXISTS visit_schedule;
DROP TABLE IF EXISTS personal_schedule;
DROP TABLE IF EXISTS todo_for_care_worker;
DROP TABLE IF EXISTS tag_of_care_worker;
DROP TABLE IF EXISTS care_worker_certificate;
DROP TABLE IF EXISTS education;
DROP TABLE IF EXISTS care_worker_service_type;
DROP TABLE IF EXISTS matching;
DROP TABLE IF EXISTS care_worker;
DROP TABLE IF EXISTS m_service_type;
DROP TABLE IF EXISTS m_personal_type;
DROP TABLE IF EXISTS eval_templates;
DROP TABLE IF EXISTS matching_memo;

-- 직원
DROP TABLE IF EXISTS assignment_history;
DROP TABLE IF EXISTS authorities_of_employee;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS m_status;
DROP TABLE IF EXISTS m_job;
DROP TABLE IF EXISTS m_authorities;
DROP TABLE IF EXISTS m_department;
DROP TABLE IF EXISTS certificate;
DROP TABLE IF EXISTS employee_career;
DROP TABLE IF EXISTS employee_form;
DROP TABLE IF EXISTS employee_form_category;

-- 알람
DROP TABLE IF EXISTS notification_log;
DROP TABLE IF EXISTS notification_rule;
DROP TABLE IF EXISTS notification_template;
DROP TABLE IF EXISTS notification_event_type;
DROP TABLE IF EXISTS notification_channel_type;

-- 용품

DROP TABLE IF EXISTS contract_status;
DROP TABLE IF EXISTS rental_contract;
DROP TABLE IF EXISTS rental_product_status;
DROP TABLE IF EXISTS rental_product;
DROP TABLE IF EXISTS m_location_status;
DROP TABLE IF EXISTS care_product;
DROP TABLE IF EXISTS m_care_product;
DROP TABLE IF EXISTS m_product_status;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product_tasks;
DROP TABLE IF EXISTS product_tasks_for_worker;
DROP TABLE IF EXISTS product_history;

-- ai + 태그 + 위험요소
DROP TABLE IF EXISTS ai_care;
DROP TABLE IF EXISTS personal_tag;
DROP TABLE IF EXISTS m_risk_level;
DROP TABLE IF EXISTS m_risk_factor;


DROP TABLE IF EXISTS electronic_payment_process;
DROP TABLE IF EXISTS electronic_payment;
DROP TABLE IF EXISTS electronic_payment_category;
DROP TABLE IF EXISTS beneficiary_significant;
DROP TABLE IF EXISTS m_significant_category;
DROP TABLE IF EXISTS m_significant;
DROP TABLE IF EXISTS potential_customer;
DROP TABLE IF EXISTS potential_stage;
DROP TABLE IF EXISTS counsel_history;
DROP TABLE IF EXISTS m_reservation_channel;
DROP TABLE IF EXISTS m_counsel_category;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS certificate_status;

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE m_status
(
    status INT NOT NULL AUTO_INCREMENT,
    field  VARCHAR(50),
    CONSTRAINT pk_m_status_status PRIMARY KEY (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_job
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_m_job_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_authorities
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_m_authorities_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_department
(
    id        INT          NOT NULL AUTO_INCREMENT,
    dept_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_m_department_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE certificate
(
    id               INT          NOT NULL AUTO_INCREMENT,
    certificate_name VARCHAR(255) NOT NULL,
    organization     VARCHAR(255) NOT NULL,
    edu_cycle_years  INT          NOT NULL,
    CONSTRAINT pk_certificate_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE electronic_payment_category
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '전자결재유형이름 (급여, 구매 등)',
    CONSTRAINT pk_electronic_payment_category_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE employee
(
    id               INT          NOT NULL AUTO_INCREMENT,
    name             VARCHAR(40),
    pw               VARCHAR(255),
    birth            DATE,
    gender           VARCHAR(1)   NOT NULL,
    address          VARCHAR(500) NOT NULL,
    email            VARCHAR(50),
    phone            VARCHAR(14)  NOT NULL,
    emergency_number VARCHAR(14),
    hire_date        DATE,
    end_date         DATE,
    dept_code        INT,
    job_code         INT          NOT NULL,
    manager_id       INT,
    status_id        INT          NOT NULL,
    CONSTRAINT pk_employee_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE employee_career
(
    id           INT          NOT NULL AUTO_INCREMENT,
    employee_id  INT          NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    work_period  VARCHAR(50)  NOT NULL,
    task         VARCHAR(255) NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE authorities_of_employee
(
    employee_id    INT NOT NULL,
    authority_code INT NOT NULL,
    -- 복합키 설정: employee_id가 앞에 와야 해당 컬럼 기준 조회 시 인덱스를 탑니다.
    CONSTRAINT pk_authorities_of_employee PRIMARY KEY (employee_id, authority_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE employee_form_category
(
    id   INT          NOT NULL AUTO_INCREMENT COMMENT '카테고리 번호',
    name VARCHAR(255) NULL COMMENT '카테고리 이름',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE employee_form
(
    id                        INT          NOT NULL AUTO_INCREMENT COMMENT '파일 업로드_번호',
    paper_type                VARCHAR(255) NULL COMMENT '서류명',
    mime_type                 VARCHAR(255) NULL COMMENT 'MIME_타입',
    file_path                 VARCHAR(255) NULL COMMENT '파일경로',
    created_at                DATETIME     NULL COMMENT '파일 등록일자',
    original_file_name        VARCHAR(255) NULL COMMENT '원본파일명',
    re_file_name              VARCHAR(255) NULL COMMENT '리네임파일명',
    employee_id               INT          NOT NULL COMMENT '직원번호',
    employee_form_category_id INT          NOT NULL COMMENT '카테고리 번호',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE assignment_history
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    start_date DATE,
    end_date   DATE,
    status     VARCHAR(255),
    reason     VARCHAR(255),
    created_at DATETIME,
    id2        INT    NOT NULL,
    id3        BIGINT NOT NULL,
    CONSTRAINT pk_assignment_history_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE notification_channel_type
(
    channel_type_id INT          NOT NULL AUTO_INCREMENT,
    code            VARCHAR(50)  NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255),
    CONSTRAINT pk_notification_channel_type_id PRIMARY KEY (channel_type_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE notification_event_type
(
    target_type_id INT          NOT NULL AUTO_INCREMENT,
    code           VARCHAR(50)  NOT NULL,
    name           VARCHAR(255) NOT NULL,
    description    VARCHAR(2000),
    severity       INT,
    CONSTRAINT pk_notification_event_type_target_type_id PRIMARY KEY (target_type_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE notification_template
(
    template_id    INT           NOT NULL AUTO_INCREMENT,
    title          VARCHAR(50)   NOT NULL,
    content        VARCHAR(2000) NOT NULL,
    template_type  VARCHAR(50)   NOT NULL,
    created_at     DATE          NOT NULL,
    updated_at     DATE,
    is_active      INT           NOT NULL,
    created_by     INT,
    updated_by     INT,
    target_type_id INT           NOT NULL,
    CONSTRAINT pk_notification_template_template_id PRIMARY KEY (template_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE notification_rule
(
    rule_id     INT                                    NOT NULL AUTO_INCREMENT,
    template_id INT                                    NOT NULL,
    offset_days INT,
    is_active   INT                                    NOT NULL,
    created_at  DATE                                   NOT NULL,
    updated_at  DATE,
    created_by  INT,
    updated_by  INT,
    target_type ENUM ('RECIPIENT', 'EMPLOYEE', 'BOTH') NOT NULL,
    CONSTRAINT pk_notification_rule_rule_id PRIMARY KEY (rule_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE notification_log
(
    alarm_id        INT                             NOT NULL AUTO_INCREMENT,
    rule_id         INT                             NOT NULL,
    template_id     INT                             NOT NULL,
    receiver_id     INT                             NOT NULL,
    receiver_type   ENUM ('RECIPIENT', 'EMPLOYEE')  NOT NULL,
    sent_at         DATE                            NOT NULL,
    status          ENUM ('SENT', 'FAILED', 'READ') NOT NULL,
    read_at         DATE,
    channel_type_id INT                             NOT NULL,
    CONSTRAINT pk_notification_log_alarm_id PRIMARY KEY (alarm_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS m_service_type
(
    id    INT         NOT NULL AUTO_INCREMENT,
    name  VARCHAR(30) NOT NULL COMMENT '방문요양, 방문간호, 방문목욕',
    money INT         NOT NULL COMMENT '(1시간 기준) 33,000/50,000/76,000',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 개인 일정 유형 마스터 테이블
CREATE TABLE IF NOT EXISTS m_personal_type
(
    id          INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL COMMENT '유형명 (점심, 휴식, 휴가 등)',
    description VARCHAR(200) NULL COMMENT '설명',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='개인일정 유형 마스터';

CREATE TABLE IF NOT EXISTS care_worker
(
    id          INT NOT NULL AUTO_INCREMENT,
    employee_id INT NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS matching
(
    id             INT           NOT NULL AUTO_INCREMENT,
    start_date     DATE          NULL,
    end_date       DATE          NULL,
    status         VARCHAR(1)    NULL COMMENT 'Y: 매칭 상태, N: 배정 종료',
    reason         VARCHAR(1000) NULL,
    create_at      DATETIME      NULL DEFAULT CURRENT_TIMESTAMP,
    beneficiary_id BIGINT        NOT NULL,
    care_worker_id INT           NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS matching_memo
(
    matching_memo_id INT           NOT NULL AUTO_INCREMENT COMMENT '매칭 메모 id',
    matching_id      INT           NOT NULL COMMENT '매칭 번호 (matching.id)',
    memo_date        DATE          NOT NULL COMMENT '메모 대상 날짜(일정 날짜)',
    content          VARCHAR(2000) NULL COMMENT '내용',
    created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    PRIMARY KEY (matching_memo_id),
    UNIQUE KEY uk_matching_memo (matching_id, memo_date)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS care_worker_service_type
(
    m_service_type_id INT NOT NULL,
    care_worker_id    INT NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS care_worker_certificate
(
    id             INT          NOT NULL AUTO_INCREMENT,
    care_worker_id INT          NOT NULL,
    certificate_id INT          NOT NULL,
    license_no     VARCHAR(255) NOT NULL,
    issue_date     DATE         NOT NULL,
    expire_date    DATE         NULL,
    status         INT          NOT NULL DEFAULT 0 COMMENT '0:대기, 1:반려, 2:승인',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS education
(
    id                         INT          NOT NULL AUTO_INCREMENT,
    care_worker_certificate_id INT          NOT NULL, -- care_worker_certificate_id
    edu_name                   VARCHAR(255) NOT NULL,
    institution                VARCHAR(255) NOT NULL,
    edu_date                   DATE         NOT NULL,
    next_edu_date              DATE         NOT NULL,
    is_overdue                 BOOLEAN      NOT NULL,
    status                     INT          NOT NULL DEFAULT 0 COMMENT '0:대기, 1:반려, 2:승인',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS tag_of_care_worker
(
    care_worker_id INT NOT NULL,
    tag_id         INT NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS todo_for_care_worker
(
    id             INT           NOT NULL AUTO_INCREMENT,
    todo           VARCHAR(2000) NULL,
    todo_date      DATE          NOT NULL,
    type           VARCHAR(50)   NULL,
    clear_st       BOOLEAN       NULL DEFAULT 0 COMMENT '0: 미완료, 1: 완료',
    create_at      DATETIME      NULL DEFAULT CURRENT_TIMESTAMP,
    beneficiary_id BIGINT        NOT NULL,
    care_worker_id INT           NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS visit_schedule
(
    vs_id           INT                                       NOT NULL AUTO_INCREMENT,
    start_dt        DATETIME                                  NOT NULL,
    end_dt          DATETIME                                  NOT NULL,
    visit_status    ENUM ('SCHEDULED', 'IN_PROGRESS', 'DONE') NOT NULL,
    rfid_start_time DATETIME                                  NULL COMMENT '실제 시작',
    rfid_end_time   DATETIME                                  NULL COMMENT '실제 종료',
    is_log_written  BOOLEAN                                   NOT NULL COMMENT '활동일지 보기 버튼 활성 상태',
    note            TEXT                                      NULL COMMENT '특이사항',
    service_type_id INT                                       NOT NULL,
    care_worker_id  INT                                       NOT NULL,
    beneficiary_id  BIGINT                                    NOT NULL,
    PRIMARY KEY (vs_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 개인 일정 테이블 (visit_schedule 구조 참고)
CREATE TABLE IF NOT EXISTS personal_schedule
(
    ps_id            INT                                       NOT NULL AUTO_INCREMENT,
    start_dt         DATETIME                                  NOT NULL,
    end_dt           DATETIME                                  NOT NULL,
    schedule_status  ENUM ('SCHEDULED', 'IN_PROGRESS', 'DONE') NOT NULL DEFAULT 'SCHEDULED',
    title            VARCHAR(200)                              NOT NULL,
    personal_type_id INT                                       NOT NULL,
    custom_type      VARCHAR(100)                              NULL COMMENT '기타 유형 직접 입력',
    location         VARCHAR(255)                              NULL,
    notes            TEXT                                      NULL COMMENT '특이사항',
    care_worker_id   INT                                       NOT NULL,
    PRIMARY KEY (ps_id),
    INDEX idx_care_worker_start_dt (care_worker_id, start_dt)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='요양보호사 개인 일정';


CREATE TABLE IF NOT EXISTS basic_evaluations
(
    eval_id        BIGINT      NOT NULL AUTO_INCREMENT,
    eval_type      VARCHAR(20) NOT NULL COMMENT '유형: FALL(낙상), BEDSORE(욕창), COGNITIVE(인지), NEEDS(욕구)',
    template_id    BIGINT      NOT NULL COMMENT '어떤 템플릿으로 평가했는지',
    eval_date      DATE        NOT NULL COMMENT '최신순 정렬용',
    eval_data      JSON        NOT NULL COMMENT '평가 상세 내용 (문항, 점수, 총점, 등급 등)',
    created_at     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    beneficiary_id BIGINT      NOT NULL, -- beneficiary_id
    care_worker_id INT         NOT NULL, -- care_worker_id
    PRIMARY KEY (eval_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS eval_templates
(
    template_id   BIGINT       NOT NULL AUTO_INCREMENT,
    eval_type     VARCHAR(20)  NOT NULL COMMENT 'FALL, BEDSORE, COGNITIVE, NEEDS',
    version       INT          NOT NULL DEFAULT 1 COMMENT '템플릿 버전',
    name          VARCHAR(100) NOT NULL COMMENT '폼 이름 (예: 낙상위험도 평가지 v1)',
    description   VARCHAR(255) NULL COMMENT '설명 / 개정 사유 등',
    template_json JSON         NOT NULL COMMENT '문항 정의, 점수 규칙, 판정 기준 등',
    total_score   INT          NULL COMMENT '이 평가지의 최대 점수',
    is_active     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '1: 사용, 0: 사용 안 함',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (template_id),
    UNIQUE KEY uk_eval_templates_type_version (eval_type, version)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS counseling_logs
(
    counseling_id      BIGINT       NOT NULL AUTO_INCREMENT,
    counseling_date    DATE         NOT NULL,
    counseling_type    VARCHAR(50)  NOT NULL COMMENT '(정기, 수시 등)',
    satisfaction       VARCHAR(20)  NOT NULL COMMENT '(만족, 보통 등)',
    visit_purpose      VARCHAR(255) NOT NULL,
    attendees          VARCHAR(100) NOT NULL,
    discussion_content TEXT         NOT NULL,
    agreement_content  TEXT         NOT NULL,
    next_visit_date    DATE         NULL,
    counselor_sign_url VARCHAR(255) NOT NULL,
    guardian_sign_url  VARCHAR(255) NOT NULL,
    created_at         DATETIME     NULL,
    care_worker_id     INT          NOT NULL, -- care_worker_id
    beneficiary_id     BIGINT       NOT NULL, -- beneficiary_id
    PRIMARY KEY (counseling_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS care_logs
(
    log_id               BIGINT      NOT NULL AUTO_INCREMENT,
    vs_id                INT         NOT NULL,
    service_date         DATE        NOT NULL,
    start_time           TIME        NOT NULL,
    end_time             TIME        NOT NULL,
    service_type         VARCHAR(20) NOT NULL,
    created_at           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME    NOT NULL,
    is_deleted           TINYINT(1)  NOT NULL COMMENT '0: 사용, 1: 논리삭제',
    is_breakfast         BOOLEAN     NOT NULL DEFAULT 0,
    is_lunch             BOOLEAN     NOT NULL DEFAULT 0,
    is_dinner            BOOLEAN     NOT NULL DEFAULT 0,
    is_snack             BOOLEAN     NOT NULL DEFAULT 0,
    diaper_count         INT         NOT NULL DEFAULT 0,
    toilet_count         INT         NOT NULL DEFAULT 0,
    is_portable_toilet   BOOLEAN     NOT NULL DEFAULT 0,
    is_urine             BOOLEAN     NOT NULL DEFAULT 0,
    is_stool             BOOLEAN     NOT NULL DEFAULT 0,
    stool_normal         BOOLEAN     NOT NULL DEFAULT 0,
    stool_diarrhea       BOOLEAN     NOT NULL DEFAULT 0,
    stool_constipation   BOOLEAN     NOT NULL DEFAULT 0,
    is_face_wash         BOOLEAN     NOT NULL DEFAULT 0,
    is_oral_care         BOOLEAN     NOT NULL DEFAULT 0,
    is_hair_wash         BOOLEAN     NOT NULL DEFAULT 0,
    is_body_wash         BOOLEAN     NOT NULL DEFAULT 0,
    is_change_clothes    BOOLEAN     NOT NULL DEFAULT 0,
    is_meal_prep         BOOLEAN     NOT NULL DEFAULT 0,
    is_bed_care          BOOLEAN     NOT NULL DEFAULT 0,
    is_position_change   BOOLEAN     NOT NULL DEFAULT 0,
    is_get_up_help       BOOLEAN     NOT NULL DEFAULT 0,
    is_indoor_move       BOOLEAN     NOT NULL DEFAULT 0,
    is_walk_help         BOOLEAN     NOT NULL DEFAULT 0,
    is_emotional_talk    BOOLEAN     NOT NULL DEFAULT 0,
    is_communication     BOOLEAN     NOT NULL DEFAULT 0,
    is_counseling        BOOLEAN     NOT NULL DEFAULT 0,
    is_cognitive_care    BOOLEAN     NOT NULL DEFAULT 0,
    is_behavior_care     BOOLEAN     NOT NULL DEFAULT 0,
    is_health_good       BOOLEAN     NOT NULL DEFAULT 0,
    is_pain              BOOLEAN     NOT NULL DEFAULT 0,
    is_edema             BOOLEAN     NOT NULL DEFAULT 0,
    is_skin_issue        BOOLEAN     NOT NULL DEFAULT 0,
    is_body_etc          BOOLEAN     NOT NULL DEFAULT 0,
    is_mood_calm         BOOLEAN     NOT NULL DEFAULT 0,
    is_mood_anxious      BOOLEAN     NOT NULL DEFAULT 0,
    is_mood_depressed    BOOLEAN     NOT NULL DEFAULT 0,
    is_mood_angry        BOOLEAN     NOT NULL DEFAULT 0,
    is_mood_etc          BOOLEAN     NOT NULL DEFAULT 0,
    is_excretion_mistake BOOLEAN     NOT NULL DEFAULT 0,
    is_sleep_lack        BOOLEAN     NOT NULL DEFAULT 0,
    is_nap_excess        BOOLEAN     NOT NULL DEFAULT 0,
    special_note         TEXT        NULL,
    care_worker_id       INT         NOT NULL,
    beneficiary_id       BIGINT      NOT NULL,
    PRIMARY KEY (log_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



CREATE TABLE IF NOT EXISTS contract_status
(
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '접수, 유지, 종료',
    constraint pk_contract_status_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS rental_contract
(
    id                 INT         NOT NULL AUTO_INCREMENT,
    wanted_date        date        not null,
    start_date         date        NULL,
    end_date           date        NULL,
    created_at         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    term_month         int         NOT NULL,
    beneficiary_id     BIGINT      NOT NULL,
    emp_id             int         NULL,
    product_cd         varchar(50) NOT NULL,
    contract_status_cd INT         NOT NULL,
    constraint pk_rental_contract_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS rental_product_status
(
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '유지, 회수접수, 회수완료',
    constraint pk_rental_product_status_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS rental_product
(
    id                 INT         NOT NULL AUTO_INCREMENT,
    start_date         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_date           datetime    NULL,
    rental_contract_cd INT         NOT NULL,
    rental_status_id   INT         NOT NULL,
    product_id         varchar(50) NOT NULL,
    constraint pk_rental_product_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS m_location_status
(
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '센터, 출고, 수급자, as 센터, 회수,',
    constraint pk_m_location_status_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS care_product
(
    id                   varchar(50)    NOT NULL,
    amount               decimal(10, 2) NOT NULL,
    rental_amount        decimal(10, 2) NOT NULL,
    cumulative_revenue   decimal(15, 2) NOT NULL default 0, /*수익금 누계 성능을 위해서 추가*/
    update_date          datetime       NULL     DEFAULT CURRENT_TIMESTAMP,
    bought_date          datetime       NULL     DEFAULT CURRENT_TIMESTAMP,
    product_cd           varchar(50)    NOT NULL,
    product_status       INT            NOT NULL DEFAULT 0,
    m_location_status_cd INT            NOT NULL,
    last_id              int            not null,
    constraint pk_care_product_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS m_care_product
(
    id            varchar(50)    NOT NULL,
    name          VARCHAR(255)   NOT NULL,
    explanation   varchar(255)   null,
    amount        decimal(10, 2) NULL,
    rental_amount decimal(10, 2) NULL,
    created_at    datetime       NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    datetime       NULL DEFAULT CURRENT_TIMESTAMP,
    category_cd   varchar(50)    NOT NULL,
    constraint pk_m_care_product_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS m_product_status
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '보관, 대여, AS , 폐기',
    constraint pk_m_product_status_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS product_category
(
    id         varchar(50)  NOT NULL,
    name       varchar(500) NULL,
    created_at datetime     NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime     NULL DEFAULT CURRENT_TIMESTAMP,
    parent_id  varchar(50)  NULL,
    constraint pk_product_category_id primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS product_tasks_for_worker
(
    id             INT         NOT NULL AUTO_INCREMENT,
    status         int         NOT NULL COMMENT '0: 회수, 1: 수령',
    expected_date  DATE        NULL,
    is_confirmed   varchar(1)  NULL COMMENT 'N: 예정, Y: 완료',
    completed_at   datetime    NULL,
    created_at     datetime    NULL,
    product_id     varchar(50) NULL,
    care_worker_id INT         NULL,
    beneficiary_id BIGINT      NOT NULL,
    constraint pk_product_tasks_for_worker_id primary key (id),
    constraint ck_product_tasks_for_worker_is_confirmed check (is_confirmed in ('Y', 'N'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS product_tasks
(
    id            INT         NOT NULL AUTO_INCREMENT,
    status        int         NOT NULL COMMENT '0: 입고, 1: 출고',
    expected_date DATE        NULL,
    is_confirmed  varchar(1)  NULL COMMENT 'N: 예정, Y: 완료',
    completed_at  datetime    NULL,
    created_at    datetime    NULL,
    product_id    varchar(50) NULL,
    employee_id   int         NULL,
    constraint pk_product_tasks_id primary key (id),
    constraint ck_product_tasks_is_confirmed check (is_confirmed in ('Y', 'N'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- 1. m_risk_factor: 위험 요인 정의 테이블
CREATE TABLE m_risk_factor
(
    id    INT          NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255) NOT NULL COMMENT '뇌졸증, 치매, 거동불편, 당뇨, 고혈압, 공격성, 몽유병, 낙상위험, 욕창위험',
    score INT          NOT NULL COMMENT '15,15,3,5,5,10,10,5,5',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- 2. m_risk_level: 위험 등급 정의 테이블
CREATE TABLE m_risk_level
(
    id    INT          NOT NULL AUTO_INCREMENT,
    level INT          NOT NULL COMMENT '등급(예: 1=저위험, 2=중위험, 3=고위험)',
    score VARCHAR(255) NOT NULL COMMENT '저위험:10점미만, 중위험:10점, 고위험:15점이상',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- 3. personal_tag: 개인적 특성 태그 테이블
CREATE TABLE personal_tag
(
    id  INT          NOT NULL AUTO_INCREMENT,
    tag VARCHAR(255) NOT NULL COMMENT '말벗, 산책, 음악, 영화, 게임, 서예, 요리',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- 4. ai_care: AI 케어 리포트/요약 테이블
CREATE TABLE ai_care
(
    ai_id          VARCHAR(255) NOT NULL COMMENT 'AI 리포트 고유 ID',
    ai_content     VARCHAR(255) NOT NULL COMMENT 'AI 요약 내용',
    ai_month       DATE         NULL COMMENT '대상 월 (예: 2025-04-01)',
    ai_create_at   DATETIME     NULL COMMENT 'AI 요약 생성 일시',
    beneficiary_id BIGINT       NOT NULL COMMENT '요양일지 수급자번호와 조인(FK 대상)',
    PRIMARY KEY (ai_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE invoice
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    beneficiary_id BIGINT       NOT NULL,
    bill           VARCHAR(255) NULL,

    period_start   DATE         NOT NULL,
    period_end     DATE         NOT NULL,

    is_sent        ENUM ('초안','보류','전송','지불됨','취소됨') DEFAULT '초안',
    total_count    INT          NULL,
    own_count      INT          NULL,

    payment_status ENUM ('미납','완납')                  DEFAULT '미납',
    billing_month  CHAR(7)      NULL COMMENT 'YYYY-MM',

    CONSTRAINT pk_invoice_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE tag_of_beneficiary
(
    beneficiary_id BIGINT NOT NULL,
    tag_id         INT    NOT NULL,
    CONSTRAINT pk_tag_of_beneficiary_id PRIMARY KEY (beneficiary_id, tag_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE contract
(
    id              BIGINT                 NOT NULL AUTO_INCREMENT,
    beneficiary_id  BIGINT                 NOT NULL,
    start_date      DATE                   NOT NULL,
    end_date        DATE                   NULL,
    file_path       VARCHAR(255)           NULL,
    contract_status ENUM ('진행중','만료','해지') NULL,
    CONSTRAINT pk_contract_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE emergency
(
    id             INT        NOT NULL AUTO_INCREMENT,
    emergency_st   VARCHAR(1) NOT NULL DEFAULT 'N',
    action_st      VARCHAR(1) NULL     DEFAULT 'N',
    emergency_time DATETIME   NULL     DEFAULT CURRENT_TIMESTAMP,
    action_time    DATETIME   NULL     DEFAULT CURRENT_TIMESTAMP,
    beneficiary_id BIGINT     NOT NULL,
    CONSTRAINT pk_emergency_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE beneficiary
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    name                  VARCHAR(255) NOT NULL,
    gender                VARCHAR(1)   NOT NULL COMMENT 'M:남자, F:여자',
    birthdate             DATE         NOT NULL,
    address               VARCHAR(255) NOT NULL,
    phone                 VARCHAR(255) NULL,
    out_of_poket_ratio    DOUBLE       NULL,
    status                BOOLEAN      NOT NULL DEFAULT 0 COMMENT '0:서비스 해지, 1:서비스 중',
    potential_customer_id BIGINT       NOT NULL,
    risk_id               INT          NOT NULL,
    CONSTRAINT pk_beneficiary_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE beneficiary_schedule
(
    id              BIGINT  NOT NULL AUTO_INCREMENT,
    day             TINYINT NOT NULL COMMENT '1:월~7:일',
    start_time      TIME    NOT NULL,
    end_time        TIME    NOT NULL,
    beneficiary_id  BIGINT  NOT NULL,
    service_type_id INT     NOT NULL,
    CONSTRAINT pk_beneficiary_schedule_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE application_form
(
    id                 INT          NOT NULL AUTO_INCREMENT,
    mime_type          VARCHAR(255) NULL,
    file_path          VARCHAR(255) NULL,
    created_at         DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    original_file_name VARCHAR(255) NULL,
    re_file_name       VARCHAR(255) NULL,
    form_category_id   INT          NOT NULL,
    size               VARCHAR(255) NULL,
    CONSTRAINT pk_application_form_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE m_form_category
(
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NULL COMMENT '의사소견서, 요양일지제공계획서 등',
    CONSTRAINT pk_m_form_category_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE riskOfMember
(
    beneficiary_id BIGINT NOT NULL,
    risk_id        INT    NOT NULL,
    CONSTRAINT pk_riskOfMember_id PRIMARY KEY (beneficiary_id, risk_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE beneficiary_care_level
(
    id             INT         NOT NULL AUTO_INCREMENT,
    start_date     DATE        NOT NULL,
    end_date       DATE        NOT NULL,
    number         VARCHAR(20) NOT NULL,
    renewal        VARCHAR(1)  NULL COMMENT 'Y:갱신, N:미갱신',
    beneficiary_id BIGINT      NOT NULL,
    CONSTRAINT pk_beneficiary_care_level_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE expiration_of_care_level
(
    id              INT        NOT NULL AUTO_INCREMENT,
    expired_section INT        NOT NULL COMMENT '1:90일전, 2:60일전, 3:45일전',
    outbound_status VARCHAR(1) NOT NULL DEFAULT 'N',
    extends_status  VARCHAR(1) NULL     DEFAULT 'N',
    beneficiary_id  BIGINT     NOT NULL,
    emp_id          INT        NOT NULL,
    CONSTRAINT pk_expiration_of_care_level_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE notice_expiration
(
    id            INT           NOT NULL AUTO_INCREMENT,
    notice_date   DATETIME      NOT NULL,
    memo          VARCHAR(2000) NULL,
    expiration_id INT           NOT NULL,
    emp_id        INT           NOT NULL,
    CONSTRAINT pk_notice_expiration_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE cost_of_beneficiary
(
    id             INT            NOT NULL AUTO_INCREMENT,
    monthly_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    month          CHAR(7)        NOT NULL COMMENT 'YYYY-MM',
    beneficiary_id BIGINT         NOT NULL,
    CONSTRAINT pk_cost_of_beneficiary_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE m_care_level
(
    id            INT         NOT NULL AUTO_INCREMENT,
    level         VARCHAR(50) NOT NULL,
    validity      INT         NOT NULL,
    monthly_limit INT         NOT NULL,
    CONSTRAINT pk_m_care_level_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE guardian
(
    id             INT         NOT NULL AUTO_INCREMENT,
    name           VARCHAR(50) NULL,
    phone          VARCHAR(50) NULL,
    relation       VARCHAR(50) NULL,
    is_primary     VARCHAR(1)  NULL COMMENT 'Y/N',
    beneficiary_id BIGINT      NOT NULL,
    CONSTRAINT pk_guardian_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE care_service_count
(
    id              INT        NOT NULL AUTO_INCREMENT,
    m_care_level_id INT        NOT NULL,
    name            VARCHAR(5) NOT NULL,
    money           INT        NOT NULL,
    CONSTRAINT pk_care_service_count_id PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE beneficiary_count
(
    beneficiary_care_level_id INT NOT NULL,
    m_care_level_id           INT NOT NULL,
    CONSTRAINT pk_beneficiary_count_id PRIMARY KEY (beneficiary_care_level_id, m_care_level_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



CREATE TABLE electronic_payment_process
(
    id                    INT            NOT NULL AUTO_INCREMENT,
    approve               ENUM ('0','1') NOT NULL COMMENT '0:미승인, 1:승인',
    drafter_id            INT            NOT NULL,
    approver_id           INT            NOT NULL,
    electronic_payment_id INT            NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE electronic_payment
(
    id                             INT            NOT NULL AUTO_INCREMENT,
    number                         VARCHAR(50)    NOT NULL,
    approve                        ENUM ('0','1') NOT NULL COMMENT '0:미승인, 1:승인',
    employee_id                    INT            NOT NULL,
    electronic_payment_category_id INT            NOT NULL COMMENT '전자결재유형번호',
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE beneficiary_significant
(
    id             INT    NOT NULL AUTO_INCREMENT,
    beneficiary_id BIGINT NOT NULL,
    significant_id INT    NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_significant_category
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_significant
(
    id                      INT         NOT NULL AUTO_INCREMENT,
    name                    VARCHAR(50) NOT NULL,
    significant_category_id INT         NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE potential_customer
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    phone       VARCHAR(255) NOT NULL,
    gender      VARCHAR(1)   NULL,
    birthdate   DATETIME     NULL,
    address     VARCHAR(255) NULL,
    willingness VARCHAR(1)   NULL     DEFAULT 'Y' COMMENT 'N: 가입의사 없음, Y: 초기값',
    create_at   DATETIME     NOT NULL DEFAULT current_timestamp,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE potential_stage
(
    id                    INT        NOT NULL AUTO_INCREMENT,
    stage                 INT        NOT NULL COMMENT '1,2,3,4',
    process_status        VARCHAR(1) NOT NULL COMMENT 'P or F',
    process_time          DATETIME   NULL,
    month                 DATETIME   NOT NULL,
    html_code             TEXT       NOT NULL,
    potential_customer_id BIGINT     NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE counsel_history
(
    id                       BIGINT        NOT NULL AUTO_INCREMENT,
    consult_date             DATETIME      NULL,
    summary                  VARCHAR(2000) NULL,
    detail                   VARCHAR(2000) NOT NULL,
    guardian_st              INT           NULL COMMENT '0: 본인, 1: 보호자',
    follow_up                VARCHAR(2000) NULL,
    follow_up_necessary      VARCHAR(1)    NOT NULL COMMENT 'Y, N',
    churn                    VARCHAR(1)    NOT NULL DEFAULT 'N' COMMENT 'Y, N',
    churn_reason             VARCHAR(2000) NULL,
    m_reservation_channel_id INT           NULL,
    potential_id             BIGINT        NULL,
    beneficiary_id           BIGINT        NULL,
    counsel_category_id      INT           NOT NULL,
    counselor_id             INT           NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_reservation_channel
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE m_counsel_category
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    INDEX (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE `refresh_token`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `token_hash`   varchar(128) NOT NULL,
    `jti`          varchar(64)  NULL COMMENT 'unique 제약조건',
    `issued_at`    datetime     NULL,
    `expires_at`   datetime     NULL,
    `revoked`      tinyint      NULL DEFAULT 0,
    `revoked_at`   datetime     NULL,
    `device_fp`    varchar(255) NULL,
    `ip`           varchar(255) NULL,
    `last_used_at` datetime     NULL,
    `employee_id`  int          NOT NULL,
    constraint pk_refresh_token primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE cost_of_beneficiary_record
(
    id                INT    NOT NULL AUTO_INCREMENT COMMENT 'PK',
    visit_schedule_id INT    NOT NULL COMMENT 'FK: visit_schedule.id',
    beneficiary_id    BIGINT NOT NULL COMMENT 'FK: beneficiary.id (visit_schedule.beneficiary_id와 동일)',
    service_type_id   INT    NOT NULL COMMENT 'FK: m_service_type.id (visit_schedule.service_type_id와 동일)',
    calculated_amount INT    NULL DEFAULT 0 COMMENT '이번 방문 금액(원)', -- ✅ 이번 방문 금액(실적)
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE `product_history`
(
    `id`               int           NOT NULL auto_increment,
    `product_id`       varchar(50)   NOT NULL,
    `product_name`     varchar(255)  NULL,
    `start_date`       datetime      NULL,
    `end_date`         datetime      NULL,
    `status`           varchar(20)   NOT NULL,
    `content`          varchar(2000) NULL,
    `employee_id`      int           NULL,
    `employee_name`    varchar(255)  NULL,
    `beneficiary_id`   BIGINT        NULL,
    `beneficiary_name` varchar(255)  NULL,
    `created_at`       datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    constraint pk_product_history_id primary key (id),
    constraint ck_product_history_status check (status in ('PURCHASED', 'RENTAL', 'REPAIR', 'DISCARD'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


-- --------------------------------------------- 외래키 제 조건 -----------------------------------------------------------------------

-- employee
ALTER TABLE employee
    ADD CONSTRAINT fk_employee_m_job
        FOREIGN KEY (job_code) REFERENCES m_job (id);

ALTER TABLE employee
    ADD CONSTRAINT fk_employee_m_department
        FOREIGN KEY (dept_code) REFERENCES m_department (id);

ALTER TABLE employee
    ADD CONSTRAINT fk_employee_m_status
        FOREIGN KEY (status_id) REFERENCES m_status (status);

-- employee_career
ALTER TABLE employee_career
    ADD CONSTRAINT fk_employee_career_employee
        FOREIGN KEY (employee_id) REFERENCES employee (id)
            ON DELETE CASCADE;

-- 상급자(manager) / self-reference
ALTER TABLE employee
    ADD CONSTRAINT fk_employee_employee
        FOREIGN KEY (manager_id)
            REFERENCES employee (id)
            ON DELETE SET NULL;

-- 직원별 권한
ALTER TABLE authorities_of_employee
    ADD CONSTRAINT fk_authorities_of_employee_employee
        FOREIGN KEY (employee_id) REFERENCES employee (id)
            ON DELETE CASCADE;

ALTER TABLE authorities_of_employee
    ADD CONSTRAINT fk_authorities_of_employee_m_authorities
        FOREIGN KEY (authority_code) REFERENCES m_authorities (id);

-- FK 설정: 직원 서류 -> 카테고리
ALTER TABLE employee_form
    ADD CONSTRAINT fk_employee_form_category
        FOREIGN KEY (employee_form_category_id) REFERENCES employee_form_category (id);

-- FK 설정: 직원 서류 -> 직원
ALTER TABLE employee_form
    ADD CONSTRAINT fk_employee_form_owner
        FOREIGN KEY (employee_id) REFERENCES employee (id);

-- 배정이력
ALTER TABLE assignment_history
    ADD CONSTRAINT fk_assignment_history_care_worker
        FOREIGN KEY (id2)
            REFERENCES care_worker (id)
            ON DELETE CASCADE;

ALTER TABLE assignment_history
    ADD CONSTRAINT fk_assignment_history_beneficiary
        FOREIGN KEY (id3)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

-- 알람
ALTER TABLE notification_template
    ADD CONSTRAINT fk_notification_template_notification_event_type
        FOREIGN KEY (target_type_id)
            REFERENCES notification_event_type (target_type_id);

-- 알림 템플릿 생성자(직원)
ALTER TABLE notification_template
    ADD CONSTRAINT fk_notification_template_employee
        FOREIGN KEY (created_by)
            REFERENCES employee (id)
            ON DELETE SET NULL;

-- 알림 템플릿 수정자(직원)
ALTER TABLE notification_template
    ADD CONSTRAINT fk_notification_template_employee2
        FOREIGN KEY (updated_by)
            REFERENCES employee (id)
            ON DELETE SET NULL;

ALTER TABLE notification_rule
    ADD CONSTRAINT fk_notification_rule_notification_template
        FOREIGN KEY (template_id)
            REFERENCES notification_template (template_id)
            ON DELETE CASCADE;

-- 알림 규칙 생성자(직원)
ALTER TABLE notification_rule
    ADD CONSTRAINT fk_notification_rule_employee
        FOREIGN KEY (created_by)
            REFERENCES employee (id)
            ON DELETE SET NULL;

-- 알림 규칙 수정자(직원)
ALTER TABLE notification_rule
    ADD CONSTRAINT fk_notification_rule_employee2
        FOREIGN KEY (updated_by)
            REFERENCES employee (id)
            ON DELETE SET NULL;

ALTER TABLE notification_log
    ADD CONSTRAINT fk_notification_log_notification_rule
        FOREIGN KEY (rule_id)
            REFERENCES notification_rule (rule_id)
            ON DELETE CASCADE;

ALTER TABLE notification_log
    ADD CONSTRAINT fk_notification_log_notification_template
        FOREIGN KEY (template_id)
            REFERENCES notification_template (template_id)
            ON DELETE CASCADE;

ALTER TABLE notification_log
    ADD CONSTRAINT fk_notification_log_notification_channel_type
        FOREIGN KEY (channel_type_id)
            REFERENCES notification_channel_type (channel_type_id);

-- care_worker
ALTER TABLE care_worker
    ADD CONSTRAINT FK_care_worker_employee
        FOREIGN KEY (employee_id) REFERENCES employee (id);

-- matching
ALTER TABLE matching
    ADD CONSTRAINT FK_matching_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id);

ALTER TABLE matching
    ADD CONSTRAINT FK_matching_care_worker
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE matching_memo
    ADD CONSTRAINT FK_matching_memo_matching
        FOREIGN KEY (matching_id) REFERENCES matching (id) ON DELETE CASCADE;

-- care_worker_service_type
ALTER TABLE care_worker_service_type
    ADD CONSTRAINT FK_care_worker_service_type_m_service_type
        FOREIGN KEY (m_service_type_id) REFERENCES m_service_type (id);

ALTER TABLE care_worker_service_type
    ADD CONSTRAINT FK_care_worker_service_type_care_worker
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

-- care_worker_certificate
ALTER TABLE care_worker_certificate
    ADD CONSTRAINT FK_care_worker_certificate_care_worker
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE care_worker_certificate
    ADD CONSTRAINT FK_care_worker_certificate_certificate
        FOREIGN KEY (certificate_id) REFERENCES certificate (id);

-- education
ALTER TABLE education
    ADD CONSTRAINT FK_education_care_worker_certificate
        FOREIGN KEY (care_worker_certificate_id) REFERENCES care_worker_certificate (id);

-- tag_of_care_worker
ALTER TABLE tag_of_care_worker
    ADD CONSTRAINT FK_tag_of_care_worker_care_worker
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE tag_of_care_worker
    ADD CONSTRAINT FK_tag_of_care_worker_personal_tag
        FOREIGN KEY (tag_id) REFERENCES personal_tag (id);

-- todo_for_care_worker
ALTER TABLE todo_for_care_worker
    ADD CONSTRAINT FK_todo_for_care_worker_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id);

ALTER TABLE todo_for_care_worker
    ADD CONSTRAINT FK_todo_for_care_worker_care_worker_id
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

-- visit_schedule
ALTER TABLE visit_schedule
    ADD CONSTRAINT FK_visit_schedule_m_service_type
        FOREIGN KEY (service_type_id) REFERENCES m_service_type (id);

ALTER TABLE visit_schedule
    ADD CONSTRAINT FK_visit_schedule_care_worker
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE visit_schedule
    ADD CONSTRAINT FK_visit_schedule_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id);

-- personal_schedule
ALTER TABLE personal_schedule
    ADD CONSTRAINT FK_personal_schedule_care_worker
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id)
            ON DELETE CASCADE;

ALTER TABLE personal_schedule
    ADD CONSTRAINT FK_personal_schedule_m_personal_type
        FOREIGN KEY (personal_type_id) REFERENCES m_personal_type (id);


-- basic_evaluations
ALTER TABLE basic_evaluations
    ADD CONSTRAINT FK_basic_evaluations_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id);

ALTER TABLE basic_evaluations
    ADD CONSTRAINT FK_basic_evaluations_care_worker_id
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE basic_evaluations
    ADD CONSTRAINT FK_basic_evaluations_eval_templates
        FOREIGN KEY (template_id) REFERENCES eval_templates (template_id);

-- counseling_logs
ALTER TABLE counseling_logs
    ADD CONSTRAINT FK_counseling_logs_employee
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE counseling_logs
    ADD CONSTRAINT FK_counseling_logs_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id);

-- care_logs
ALTER TABLE care_logs
    ADD CONSTRAINT FK_care_logs_visit_schedule
        FOREIGN KEY (vs_id) REFERENCES visit_schedule (vs_id);

ALTER TABLE care_logs
    ADD CONSTRAINT FK_care_logs_
        FOREIGN KEY (care_worker_id) REFERENCES care_worker (id);

ALTER TABLE care_logs
    ADD CONSTRAINT FK_care_logs_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id);


-- 렌탈 계약
alter table rental_contract
    add constraint fk_rental_contract_contract_status foreign key (contract_status_cd)
        references contract_status (id) ON DELETE CASCADE;

alter table rental_contract
    add constraint fk_rental_contract_m_care_product foreign key (product_cd)
        references m_care_product (id) ON DELETE CASCADE;

alter table rental_contract
    add constraint fk_rental_contract_employee foreign key (emp_id)
        references employee (id) ON DELETE CASCADE;

alter table rental_contract
    add constraint fk_rental_contract_beneficiary foreign key (beneficiary_id)
        references beneficiary (id) ON DELETE CASCADE;

-- 렌탈
alter table rental_product
    add constraint fk_rental_product_rental_product foreign key (rental_contract_cd)
        references rental_contract (id) ON DELETE CASCADE;

alter table rental_product
    add constraint fk_rental_product_care_product foreign key (product_id)
        references care_product (id) ON DELETE CASCADE;

alter table rental_product
    add constraint fk_rental_product_rental_product_status foreign key (rental_status_id)
        references rental_product_status (id) ON DELETE CASCADE;

-- 용품리스트
alter table care_product
    add constraint fk_care_product_m_care_product foreign key (product_cd)
        references m_care_product (id) ON DELETE CASCADE;

alter table care_product
    add constraint fk_care_product_m_product_status foreign key (product_status)
        references m_product_status (id) ON DELETE CASCADE;

alter table care_product
    add constraint fk_care_product_m_location_status foreign key (m_location_status_cd)
        references m_location_status (id) ON DELETE CASCADE;

-- 입,출고 예정리스트
alter table product_tasks
    add constraint fk_product_tasks_care_product foreign key (product_id)
        references care_product (id) ON DELETE CASCADE;


alter table product_tasks
    add constraint fk_product_tasks_employee foreign key (employee_id)
        references employee (id) ON DELETE CASCADE;

-- 요양사 용품 회수, 수령 예정
alter table product_tasks_for_worker
    add constraint fk_product_tasks_for_care_product foreign key (product_id)
        references care_product (id) ON DELETE CASCADE;

alter table product_tasks_for_worker
    add constraint fk_product_tasks_for_worker_care_worker foreign key (care_worker_id)
        references care_worker (id) ON DELETE CASCADE;

alter table product_tasks_for_worker
    add constraint fk_product_tasks_for_worker_beneficiary foreign key (beneficiary_id)
        references beneficiary (id) ON DELETE CASCADE;


/* invoice ↔ beneficiary */
ALTER TABLE invoice
    ADD CONSTRAINT fk_invoice_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

/* contract ↔ beneficiary */
ALTER TABLE contract
    ADD CONSTRAINT fk_contract_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

/* tag_of_beneficiary ↔ beneficiary, personal_tag */
ALTER TABLE tag_of_beneficiary
    ADD CONSTRAINT fk_tag_of_beneficiary_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT fk_tag_of_beneficiary_tag
        FOREIGN KEY (tag_id)
            REFERENCES personal_tag (id);

/* emergency ↔ beneficiary */
ALTER TABLE emergency
    ADD CONSTRAINT fk_emergency_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

/* beneficiary ↔ potential_customer, m_risk_level */
ALTER TABLE beneficiary
    ADD CONSTRAINT fk_beneficiary_potential_customer
        FOREIGN KEY (potential_customer_id)
            REFERENCES potential_customer (id),
    ADD CONSTRAINT fk_beneficiary_risk
        FOREIGN KEY (risk_id)
            REFERENCES m_risk_level (id)
            ON DELETE CASCADE;

/* beneficiary_schedule ↔ beneficiary, m_service_type */
ALTER TABLE beneficiary_schedule
    ADD CONSTRAINT fk_beneficiary_schedule_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id),
    ADD CONSTRAINT fk_beneficiary_schedule_service_type
        FOREIGN KEY (service_type_id)
            REFERENCES m_service_type (id)
            ON DELETE CASCADE;

/* application_form ↔ m_form_category, beneficiary */
ALTER TABLE application_form
    ADD CONSTRAINT fk_application_form_category
        FOREIGN KEY (form_category_id)
            REFERENCES m_form_category (id);

/* riskOfMember ↔ beneficiary, m_risk_level */
ALTER TABLE riskOfMember
    ADD CONSTRAINT fk_riskOfMember_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id),
    ADD CONSTRAINT fk_riskOfMember_factor
        FOREIGN KEY (risk_id) REFERENCES m_risk_factor (id)
            ON DELETE CASCADE;

/* beneficiary_care_level ↔ beneficiary */
ALTER TABLE beneficiary_care_level
    ADD CONSTRAINT fk_beneficiary_care_level_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

/* expiration_of_care_level ↔ beneficiary, employee */
ALTER TABLE expiration_of_care_level
    ADD CONSTRAINT fk_expiration_of_care_level_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id),
    ADD CONSTRAINT fk_expiration_of_care_level_emp
        FOREIGN KEY (emp_id)
            REFERENCES employee (id)
            ON DELETE CASCADE;

/* notice_expiration ↔ expiration_of_care_level, employee */
ALTER TABLE notice_expiration
    ADD CONSTRAINT fk_notice_expiration_expiration
        FOREIGN KEY (expiration_id)
            REFERENCES expiration_of_care_level (id),
    ADD CONSTRAINT fk_notice_expiration_emp
        FOREIGN KEY (emp_id)
            REFERENCES employee (id)
            ON DELETE CASCADE;

/* cost_of_beneficiary ↔ beneficiary */
ALTER TABLE cost_of_beneficiary
    ADD CONSTRAINT fk_cost_of_beneficiary_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

/* guardian ↔ beneficiary */
ALTER TABLE guardian
    ADD CONSTRAINT fk_guardian_beneficiary
        FOREIGN KEY (beneficiary_id)
            REFERENCES beneficiary (id)
            ON DELETE CASCADE;

ALTER TABLE guardian
    ADD UNIQUE KEY uk_guardian_beneficiary (beneficiary_id);


/* care_service_count ↔ m_care_level */
ALTER TABLE care_service_count
    ADD CONSTRAINT fk_care_service_count_m_care_level
        FOREIGN KEY (m_care_level_id)
            REFERENCES m_care_level (id)
            ON DELETE CASCADE;

/* beneficiary_count ↔ beneficiary_care_level, m_care_level */
ALTER TABLE beneficiary_count
    ADD CONSTRAINT fk_beneficiary_count_beneficiary_care_level
        FOREIGN KEY (beneficiary_care_level_id)
            REFERENCES beneficiary_care_level (id),
    ADD CONSTRAINT fk_beneficiary_count_m_care_level
        FOREIGN KEY (m_care_level_id)
            REFERENCES m_care_level (id)
            ON DELETE CASCADE;

ALTER TABLE electronic_payment_process
    ADD CONSTRAINT PK_ELECTRONIC_PAYMENT_PROCESS PRIMARY KEY (
                                                              id
        );

ALTER TABLE electronic_payment
    ADD CONSTRAINT PK_ELECTRONIC_PAYMENT PRIMARY KEY (
                                                      id
        );

ALTER TABLE beneficiary_significant
    ADD CONSTRAINT PK_BENEFICIARY_SIGNIFICANT PRIMARY KEY (
                                                           id
        );

ALTER TABLE m_significant_category
    ADD CONSTRAINT PK_M_SIGNIFICANT_CATEGORY PRIMARY KEY (
                                                          id
        );

ALTER TABLE m_significant
    ADD CONSTRAINT PK_M_SIGNIFICANT PRIMARY KEY (
                                                 id
        );

ALTER TABLE potential_customer
    ADD CONSTRAINT PK_POTENTIAL_CUSTOMER PRIMARY KEY (
                                                      id
        );

ALTER TABLE potential_stage
    ADD CONSTRAINT PK_POTENTIAL_STAGE PRIMARY KEY (
                                                   id
        );

ALTER TABLE counsel_history
    ADD CONSTRAINT PK_COUNSEL_HISTORY PRIMARY KEY (
                                                   id
        );

ALTER TABLE m_reservation_channel
    ADD CONSTRAINT PK_M_RESERVATION_CHANNEL PRIMARY KEY (
                                                         id
        );

ALTER TABLE m_counsel_category
    ADD CONSTRAINT PK_M_COUNSEL_CATEGORY PRIMARY KEY (
                                                      id
        );

ALTER TABLE electronic_payment
    ADD CONSTRAINT FK_employee_TO_electronic_payment_1
        FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE electronic_payment
    ADD CONSTRAINT FK_payment_category
        FOREIGN KEY (electronic_payment_category_id) REFERENCES electronic_payment_category (id);

ALTER TABLE electronic_payment_process
    ADD CONSTRAINT FK_employee_TO_electronic_payment_process_1 FOREIGN KEY (
                                                                            drafter_id
        )
        REFERENCES employee (
                             id
            );

ALTER TABLE electronic_payment_process
    ADD CONSTRAINT FK_employee_TO_electronic_payment_process_2 FOREIGN KEY (
                                                                            approver_id
        )
        REFERENCES employee (
                             id
            );

ALTER TABLE electronic_payment_process
    ADD CONSTRAINT FK_electronic_payment_TO_electronic_payment_process_1 FOREIGN KEY (
                                                                                      electronic_payment_id
        )
        REFERENCES electronic_payment (
                                       id
            );

ALTER TABLE beneficiary_significant
    ADD CONSTRAINT FK_beneficiary_TO_beneficiary_significant_1 FOREIGN KEY (
                                                                            beneficiary_id
        )
        REFERENCES beneficiary (
                                id
            );

ALTER TABLE beneficiary_significant
    ADD CONSTRAINT FK_m_significant_TO_beneficiary_significant_1 FOREIGN KEY (
                                                                              significant_id
        )
        REFERENCES m_significant (
                                  id
            );

ALTER TABLE m_significant
    ADD CONSTRAINT FK_m_significant_category_TO_m_significant_1 FOREIGN KEY (
                                                                             significant_category_id
        )
        REFERENCES m_significant_category (
                                           id
            );

ALTER TABLE potential_stage
    ADD CONSTRAINT FK_potential_customer_TO_potential_stage_1 FOREIGN KEY (
                                                                           potential_customer_id
        )
        REFERENCES potential_customer (
                                       id
            );

ALTER TABLE counsel_history
    ADD CONSTRAINT FK_m_reservation_channel_TO_counsel_history_1 FOREIGN KEY (
                                                                              m_reservation_channel_id
        )
        REFERENCES m_reservation_channel (
                                          id
            );

ALTER TABLE counsel_history
    ADD CONSTRAINT FK_potential_customer_TO_counsel_history_1 FOREIGN KEY (
                                                                           potential_id
        )
        REFERENCES potential_customer (
                                       id
            );

ALTER TABLE counsel_history
    ADD CONSTRAINT FK_m_counsel_category_TO_counsel_history_1 FOREIGN KEY (
                                                                           counsel_category_id
        )
        REFERENCES m_counsel_category (
                                       id
            );

ALTER TABLE counsel_history
    ADD CONSTRAINT FK_employee_TO_counsel_history_counselor
        FOREIGN KEY (counselor_id)
            REFERENCES employee (id);


ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refresh_token_employee foreign key (employee_id) references employee (id);

-- 1. 수급자 테이블에 최근 상담일 추가
ALTER TABLE beneficiary
    ADD COLUMN last_counsel_date DATETIME NULL COMMENT '이탈 판단용 최근 상담일';

-- 2. 잠재고객 테이블에 최근 상담일 추가
ALTER TABLE potential_customer
    ADD COLUMN last_counsel_date DATETIME NULL COMMENT '이탈 판단용 최근 상담일';

-- 인덱스 추가 (필수: 배치 작업 및 조회 속도 향상)
CREATE INDEX idx_beneficiary_last_date ON beneficiary (last_counsel_date);
CREATE INDEX idx_potential_last_date ON potential_customer (last_counsel_date);

ALTER TABLE potential_customer
    ADD COLUMN current_stage INT DEFAULT 1 COMMENT '현재 가입 진행 단계 (1~4)';

-- 검색 속도를 위해 복합 인덱스 추천
CREATE INDEX idx_potential_churn_check ON potential_customer (current_stage, last_counsel_date);


-- cost_of_beneficiary_record
ALTER TABLE cost_of_beneficiary_record
    ADD CONSTRAINT FK_cost_of_beneficiary_record_m_service_type
        FOREIGN KEY (service_type_id) REFERENCES m_service_type (id)
            ON DELETE CASCADE;

ALTER TABLE cost_of_beneficiary_record
    ADD CONSTRAINT FK_cost_of_beneficiary_record_care_worker
        FOREIGN KEY (visit_schedule_id) REFERENCES visit_schedule (vs_id)
            ON DELETE CASCADE;

ALTER TABLE cost_of_beneficiary_record
    ADD CONSTRAINT FK_cost_of_beneficiary_record_beneficiary
        FOREIGN KEY (beneficiary_id) REFERENCES beneficiary (id)
            ON DELETE CASCADE;



alter table product_history
    add constraint fk_product_history_employee foreign key (employee_id) references employee (id),
    add constraint fk_product_history_care_product foreign key (product_id) references care_product (id),
    add constraint fk_product_beneficiary foreign key (beneficiary_id) references beneficiary (id);



alter table m_care_product
    add constraint fk_m_care_product_product_category FOREIGN key (category_cd) references product_category (id);