-- 1. 외래 키 제약 조건 검사 비활성화 (순서 상관없이 삭제하기 위해 필수)
SET FOREIGN_KEY_CHECKS = 0;

-- ==========================================
-- 수급자 관련
-- ==========================================
TRUNCATE TABLE cost_of_beneficiary_record;
TRUNCATE TABLE beneficiary_count;
TRUNCATE TABLE care_service_count;
TRUNCATE TABLE notice_expiration;
TRUNCATE TABLE expiration_of_care_level;
TRUNCATE TABLE invoice;
TRUNCATE TABLE tag_of_beneficiary;
TRUNCATE TABLE emergency;
TRUNCATE TABLE beneficiary_schedule;
TRUNCATE TABLE application_form;
TRUNCATE TABLE riskOfMember;
TRUNCATE TABLE beneficiary_care_level;
TRUNCATE TABLE cost_of_beneficiary;
TRUNCATE TABLE guardian;
TRUNCATE TABLE contract;
TRUNCATE TABLE m_form_category;
TRUNCATE TABLE m_care_level;
TRUNCATE TABLE beneficiary;

-- ==========================================
-- 요양사 관련
-- ==========================================
TRUNCATE TABLE care_logs;
TRUNCATE TABLE counseling_logs;
TRUNCATE TABLE basic_evaluations;
TRUNCATE TABLE visit_schedule;
TRUNCATE TABLE todo_for_care_worker;
TRUNCATE TABLE tag_of_care_worker;
TRUNCATE TABLE care_worker_certificate;
TRUNCATE TABLE education;
TRUNCATE TABLE care_worker_service_type;
TRUNCATE TABLE matching;
TRUNCATE TABLE care_worker;
TRUNCATE TABLE m_service_type;
TRUNCATE TABLE m_personal_type;
TRUNCATE TABLE eval_templates;

-- ==========================================
-- 직원 관련
-- ==========================================
TRUNCATE TABLE assignment_history;
TRUNCATE TABLE authorities_of_employee;
TRUNCATE TABLE employee;
TRUNCATE TABLE m_status;
TRUNCATE TABLE m_job;
TRUNCATE TABLE m_authorities;
TRUNCATE TABLE m_department;
TRUNCATE TABLE certificate;
TRUNCATE TABLE employee_career;

-- ==========================================
-- 알람 관련
-- ==========================================
TRUNCATE TABLE notification_log;
TRUNCATE TABLE notification_rule;
TRUNCATE TABLE notification_template;
TRUNCATE TABLE notification_event_type;
TRUNCATE TABLE notification_channel_type;

-- ==========================================
-- 용품 관련
-- ==========================================
TRUNCATE TABLE contract_status;
TRUNCATE TABLE rental_contract;
TRUNCATE TABLE rental_product_status;
TRUNCATE TABLE rental_product;
TRUNCATE TABLE m_location_status;
TRUNCATE TABLE care_product;
TRUNCATE TABLE m_care_product;
TRUNCATE TABLE m_product_status;
TRUNCATE TABLE product_category;
TRUNCATE TABLE product_tasks;
TRUNCATE TABLE product_tasks_for_worker;
TRUNCATE TABLE product_history;

-- ==========================================
-- AI + 태그 + 위험요소 관련
-- ==========================================
TRUNCATE TABLE ai_care;
TRUNCATE TABLE personal_tag;
TRUNCATE TABLE m_risk_level;
TRUNCATE TABLE m_risk_factor;

-- ==========================================
-- 결제 및 기타 상담 관련
-- ==========================================
TRUNCATE TABLE electronic_payment_process;
TRUNCATE TABLE electronic_payment;
TRUNCATE TABLE electronic_payment_category;
TRUNCATE TABLE beneficiary_significant;
TRUNCATE TABLE m_significant_category;
TRUNCATE TABLE m_significant;
TRUNCATE TABLE potential_customer;
TRUNCATE TABLE potential_stage;
TRUNCATE TABLE counsel_history;
TRUNCATE TABLE m_reservation_channel;
TRUNCATE TABLE m_counsel_category;

-- 2. 외래 키 제약 조건 검사 다시 활성화 (필수)
SET FOREIGN_KEY_CHECKS = 1;


/* [파트 1] 기초 마스터 데이터
 - 다른 테이블들이 참조하는 기준 정보들입니다.
 - 외래키 의존성이 거의 없어 가장 먼저 실행되어야 합니다.
*/

-- 1. 장기요양등급
INSERT INTO m_care_level (id, level, validity, monthly_limit) VALUES
                                                                  (1, '1등급', 60, 2300000),
                                                                  (2, '2등급', 48, 2080000),
                                                                  (3, '3등급', 48, 1480000),
                                                                  (4, '4등급', 48, 1370000),
                                                                  (5, '5등급', 24, 1170000),
                                                                  (6, '인지지원등급', 24, 650000);

-- 2. 서류 카테고리
INSERT INTO m_form_category (id, name) VALUES
                                           (1, '의사소견서'),
                                           (2, '장기요양인정 신청/갱신/변경서'),
                                           (3, '장기요양인정조사표'),
                                           (4, '장기요양인정서'),
                                           (5, '장기요양급여 제공 계획서'),
                                           (6, '장기요양급여 제공기록지(방문요양)'),
                                           (7, '장기요양급여 제공기록지(방문목욕)'),
                                           (8, '장기요양급여 제공기록지(방문간호)'),
                                           (9, '건강보험(중증치매 등)산정특례 신청서');

-- 3. 예약/상담/특이사항 관련 카테고리
INSERT INTO m_reservation_channel (name) VALUES ('전화'), ('방문');
INSERT INTO m_counsel_category (name) VALUES ('가입'), ('렌탈'), ('문의'), ('컴플레인');
INSERT INTO m_significant_category (name) VALUES ('렌탈성사도움'), ('매칭도움'), ('문의해결도움'), ('컴플레인해결도움');

-- 4. 특이사항 상세 (FK: m_significant_category)
-- ID는 자동증가(Auto Increment)를 따르되 순서대로 입력합니다.
INSERT INTO m_significant (name, significant_category_id) VALUES
                                                              ('거동불편', 1), ('휠체어이용여부', 1), ('목욕불편', 1), ('거주환경불편', 1), ('동물털알레르기유무', 4),
                                                              ('인지장애', 2), ('낙상위험', 2), ('복약도움필요', 3), ('식사도움필요', 3), ('배변도움필요', 3);

-- 5. 직무/부서/상태/권한
-- 초기 데이터 + DML에 등장하는 추가 데이터 매핑
INSERT INTO m_job (name) VALUES
                             ('센터장'), ('관리자'), ('사원'), ('영업상담'), ('요양보호사');

INSERT INTO m_department (dept_name) VALUES
                                         ('영업팀'), ('자재팀');

INSERT INTO m_status (field) VALUES
                                 ('재직'), ('휴직'), ('퇴사'), ('대기'), ('승인'),
                                 ('ACTIVE'), ('LEAVE'), ('RESIGNED'); -- ID 6~8 추가

INSERT INTO m_authorities (name) VALUES
                                     ('ROLE_CAREGIVER'), ('ROLE_EMPLOYEE'), ('ROLE_CONSULTANT'), ('ROLE_TEAM_LEAD'), ('ROLE_CENTER_MANAGER'), ('ROLE_SALES_TEAM'), ('ROLE_MATERIAL_TEAM');

-- 6. 위험도/태그/서비스 유형
INSERT INTO m_risk_level (level, score) VALUES
                                            (1, '10점 미만'), (2, '10점~15점미만'), (3, '15점이상');

INSERT INTO m_risk_factor (name, score) VALUES
                                            ('낙상', 5), ('욕창', 10), ('치매', 20), ('고혈압', 5), ('당뇨', 5),
                                            ('뇌졸증', 15), ('거동불편', 3), ('공격성', 10), ('몽유병', 10);

INSERT INTO personal_tag (tag) VALUES
                                   ('말벗'), ('산책'), ('음악'), ('종교'), ('운동'), ('영화'), ('게임'), ('서예'), ('요리');

INSERT INTO m_service_type (name, money) VALUES
                                             ('방문요양', 33000), ('방문목욕', 76000), ('방문간호', 50000);

-- 개인 일정 유형 마스터 데이터
INSERT INTO m_personal_type (name, description) VALUES
                                                    ('점심', '점심 식사 시간'),
                                                    ('휴식', '휴식 시간'),
                                                    ('휴가', '연차/휴가'),
                                                    ('병원', '병원 방문'),
                                                    ('교육', '교육/연수'),
                                                    ('회의', '회의/미팅'),
                                                    ('개인용무', '개인 업무'),
                                                    ('기타', '기타 일정');


-- 7. 계약/물류 상태
INSERT INTO contract_status (name) VALUES
                                       ('접수'), ('유지'), ('종료');

INSERT INTO m_location_status (name) VALUES
                                         ('창고'), ('배송중'), ('대여중'), ('회수중'), ('수리중'),
                                         ('센터'), ('출고'), ('수급자'), ('AS센터'), ('회수'); -- ID 6~10 추가

INSERT INTO m_product_status (name) VALUES
                                        ('보관'), ('대여'), ('폐기'),('AS');

INSERT INTO rental_product_status (name) VALUES
                                             ('렌탈'), ('회수접수'), ('회수완료');

INSERT INTO product_category (id, name, parent_id) VALUES
                                                       ('C001', '침상용품', NULL),
                                                       ('C002', '이동보조', NULL),
                                                       ('C003', '위생용품', NULL),
                                                       ('C004', '안전용품', NULL);

-- 8. 알람 관련 마스터
INSERT INTO notification_channel_type (code, name, description) VALUES
                                                                    ('APP_PUSH', '앱 푸시', '모바일 앱 푸시 알림'),
                                                                    ('SMS', '문자 메시지', '핸드폰 문자(SMS) 전송'),
                                                                    ('EMAIL', '이메일', '이메일로 알림 발송');

INSERT INTO notification_event_type (code, name, description, severity) VALUES
                                                                            ('CERT_EXPIRE', '자격증 만료 예정', '요양보호사 자격증이 만료되기 전에 알려주는 이벤트', 3),
                                                                            ('RECIPIENT_BIRTHDAY','수급자 생일 알림', '수급자의 생일을 직원에게 알림', 1),
                                                                            ('VISIT_REMINDER', '방문 일정 알림', '요양보호사의 오늘 방문 일정을 알려줌', 1),
                                                                            ('DOCUMENT_MISSING', '서류 미제출 알림', '필수 서류가 제출되지 않았을 때 발생하는 이벤트', 2),
                                                                            ('CONTRACT_EXPIRE', '서비스 계약 만료', '수급자의 서비스 계약 만료가 다가옴', 3),
                                                                            ('EMERGENCY_REPORT', '응급 상황 보고', '수급자에게 응급 상황 발생 시 직원에게 알림', 5),
                                                                            ('CARELOG_SUBMIT', '요양일지 제출 요청', '요양보호사에게 일지 제출 요청', 2),
                                                                            ('NEW_ASSIGNMENT', '새 배정 안내', '새로운 수급자가 요양보호사에게 배정됨', 2),
                                                                            ('PAYROLL_NOTICE', '급여 안내', '직원에게 급여 지급 사실을 알림', 1),
                                                                            ('SYSTEM_ANNOUNCE', '시스템 공지', '관리자 공지사항', 1),
                                                                            ('CARE_LEVEL_EXPIRE', '장기요양등급 만료 예정', '장기요양등급 만료 60/30/15일 전에 알림', 3);

-- 9. 자격증 및 서비스 금액
INSERT INTO certificate (certificate_name, organization, edu_cycle_years) VALUES
                                                                              ('요양보호사 자격증', '보건복지부', 3),
                                                                              ('치매전문교육 이수증', '국립치매센터', 2),
                                                                              ('노인돌봄 전문교육 이수증', '한국사회복지사협회', 2),
                                                                              ('장기요양 실무교육 수료증', '국민건강보험공단', 1),
                                                                              ('응급처치 및 심폐소생술 자격증', '대한적십자사', 2),
                                                                              ('감염관리·위생 안전교육 이수증', '질병관리청', 1);

INSERT INTO care_service_count (id, m_care_level_id, name, money) VALUES
                                                                      (1, 1, '방문요양', 33000), (2, 1, '방문간호', 50000), (3, 1, '방문목욕', 76000),
                                                                      (4, 2, '방문요양', 33000), (5, 2, '방문간호', 50000), (6, 2, '방문목욕', 76000),
                                                                      (7, 3, '방문요양', 33000), (8, 3, '방문간호', 50000), (9, 3, '방문목욕', 76000),
                                                                      (10,4, '방문요양', 33000), (11,4, '방문간호', 50000), (12,4, '방문목욕', 76000),
                                                                      (13,5, '방문요양', 33000), (14,5, '방문간호', 50000), (15,5, '방문목욕', 76000),
                                                                      (16,6, '방문요양', 33000), (17,6, '방문간호', 50000), (18,6, '방문목욕', 76000);

-- 10. 용품 마스터
INSERT INTO m_care_product (id, name,explanation, category_cd, amount, rental_amount, created_at, updated_at) VALUES
                                                                                                                  ('EM001', '전동침대','3모터 전동침대', 'C001', 800000.00, 35000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM002', '휠체어','경량 알루미늄 휠체어', 'C002', 500000.00, 25000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM003', '에어매트','욕창방지 에어매트', 'C001', 400000.00, 28000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM004', '보행보조기','4발 보행보조기', 'C002', 150000.00, 18000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM005', '이동식 리프트','전동 이동식 리프트', 'C002', 1200000.00, 45000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM006', '욕창방지 쿠션','메모리폼 욕창방지 쿠션', 'C001', 80000.00, 10000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM007', '목욕의자','높이조절 목욕의자', 'C003', 120000.00, 15000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
                                                                                                                  ('EM008', '안전손잡이','화장실 안전손잡이', 'C004', 50000.00, 8000.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());



-- 전자결재 마스터
INSERT INTO electronic_payment_category (name) VALUES
                                                   ('급여'), -- ID: 1
                                                   ('구매'), -- ID: 2
                                                   ('휴가'), -- ID: 3
                                                   ('기타'); -- ID: 4

/* [파트 2] 독립 주체 데이터
   - 잠재 고객 (FK 참조 없음)
   - 직원 (FK: m_job, m_department, m_status + Self Referencing manager_id)
   - 직원 테이블은 상급자(manager_id) 참조 무결성을 위해 계층 순서대로 입력합니다.
*/

-- -----------------------------------------------------------
-- 1. 잠재 고객 (Potential Customer)
-- -----------------------------------------------------------
INSERT INTO potential_customer (name, phone, gender, birthdate, address, willingness, create_at) VALUES
                                                                                                     ('김철수', '010-1111-2222', 'M', '1945-05-01', '서울시 강남구 역삼동 101', 'Y', NOW()),
                                                                                                     ('이영희', '010-3333-4444', 'F', '1950-08-15', '서울시 서초구 방배동 202', 'Y', NOW()),
                                                                                                     ('박민수', '010-5555-6666', 'M', '1948-12-25', '서울시 송파구 잠실동 303', 'Y', NOW()),
                                                                                                     ('최순자', '010-7777-8888', 'F', '1942-03-10', '서울시 강동구 천호동 404', 'N', NOW()),
                                                                                                     ('정상훈', '010-9999-0000', 'M', '1955-07-07', '서울시 마포구 합정동 505', 'Y', NOW()),
                                                                                                     ('강미자', '010-1212-3434', 'F', '1949-11-11', '서울시 영등포구 여의도동', 'Y', NOW()),
                                                                                                     ('조현우', '010-5656-7878', 'M', '1940-01-30', '서울시 관악구 신림동', 'Y', NOW()),
                                                                                                     ('윤지혜', '010-9090-1212', 'F', '1952-06-20', '서울시 동작구 사당동', 'N', NOW()),
                                                                                                     ('임동현', '010-3434-5656', 'M', '1947-09-09', '서울시 구로구 구로동', 'Y', NOW()),
                                                                                                     ('한수진', '010-7878-9090', 'F', '1943-02-14', '서울시 금천구 가산동', 'Y', NOW()),
                                                                                                     ('오재원', '010-1122-3344', 'M', '1951-10-03', '서울시 성동구 성수동', 'Y', NOW()),
                                                                                                     ('서영숙', '010-5566-7788', 'F', '1946-04-05', '서울시 광진구 자양동', 'Y', NOW()),
                                                                                                     ('신경수', '010-9900-1122', 'M', '1953-08-22', '서울시 중랑구 면목동', 'N', NOW()),
                                                                                                     ('권혜진', '010-3344-5566', 'F', '1944-12-01', '서울시 동대문구 장안동', 'Y', NOW()),
                                                                                                     ('황보성', '010-7788-9900', 'M', '1954-05-18', '서울시 성북구 안암동', 'Y', NOW()),
                                                                                                     ('안미경', '010-1234-1234', 'F', '1941-09-28', '서울시 강북구 수유동', 'Y', NOW()),
                                                                                                     ('송태섭', '010-5678-5678', 'M', '1956-01-15', '서울시 도봉구 창동', 'Y', NOW()),
                                                                                                     ('전효성', '010-9012-9012', 'F', '1948-07-12', '서울시 노원구 상계동', 'N', NOW()),
                                                                                                     ('홍길동', '010-1111-1111', 'M', '1950-01-01', '서울시 은평구 불광동', 'Y', NOW()),
                                                                                                     ('김개똥', '010-2222-2222', 'F', '1955-12-31', '서울시 서대문구 신촌동', 'Y', NOW());

-- -----------------------------------------------------------
-- 2. 직원 (Employee) - 계층 구조 순서 입력 (관리자 -> 팀장 -> 팀원)
-- 암호는 암호화 했으며 모든 암호는 pwd123 으로 통일 함
-- -----------------------------------------------------------

INSERT INTO employee (name, pw, birth, gender, address, email, phone, emergency_number, hire_date, dept_code, job_code, manager_id, status_id)
VALUES
    -- [ID: 1] 최상위 관리자 (센터장)
    ('김관리', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1980-01-01', 'M', '서울특별시 강남구 역삼동', 'admin1@example.com', '010-1000-0001', NULL, '2024-01-01', NULL, 1, NULL, 1),

    -- [ID: 2] 영업팀장 (부서: 1 영업팀 / 직급: 2 관리자)
    ('박영업팀장', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1985-02-01', 'M', '서울특별시 서초구 방배동', 'sales.lead1@example.com', '010-1000-0002', NULL, '2024-01-01', 1, 2, 1, 1),

    -- [ID: 3] 자재팀장 (부서: 2 자재팀 / 직급: 2 관리자)
    ('이자재팀장', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1986-03-01', 'F', '서울특별시 송파구 잠실동', 'material.lead1@example.com', '010-1000-0003', NULL, '2024-01-01', 2, 2, 1, 1),

    -- [ID: 4~8] 자재팀 직원 (부서: 2 자재팀 / 직급: 2 관리자) -> 매니저: 3(자재팀장)
    ('김자재1', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1990-01-01', 'M', '서울특별시 강동구 천호동', 'material.staff1@example.com', '010-2000-0001', NULL, '2024-01-01', 2, 2, 3, 1),
    ('이자재2', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1990-02-01', 'F', '서울특별시 영등포구 여의도동', 'material.staff2@example.com', '010-2000-0002', NULL, '2024-01-01', 2, 2, 3, 1),
    ('박자재3', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1990-03-01', 'M', '서울특별시 마포구 합정동', 'material.staff3@example.com', '010-2000-0003', NULL, '2024-01-01', 2, 2, 3, 1),
    ('최자재4', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1990-04-01', 'F', '서울특별시 용산구 이태원동', 'material.staff4@example.com', '010-2000-0004', NULL, '2024-01-01', 2, 2, 3, 1),
    ('정자재5', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1990-05-01', 'M', '서울특별시 성동구 성수동', 'material.staff5@example.com', '010-2000-0005', NULL, '2024-01-01', 2, 2, 3, 1),

    -- [ID: 9~13] 영업팀 직원 (부서: 1 영업팀 / 직급: 4 영업상담) -> 매니저: 2(영업팀장)
    ('김영업1', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1991-01-01', 'M', '서울특별시 강북구 미아동', 'sales.staff1@example.com', '010-3000-0001', NULL, '2024-01-01', 1, 4, 2, 1),
    ('이영업2', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1991-02-01', 'F', '서울특별시 은평구 불광동', 'sales.staff2@example.com', '010-3000-0002', NULL, '2024-01-01', 1, 4, 2, 1),
    ('박영업3', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1991-03-01', 'M', '서울특별시 동대문구 장안동', 'sales.staff3@example.com', '010-3000-0003', NULL, '2024-01-01', 1, 4, 2, 1),
    ('최영업4', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1991-04-01', 'F', '서울특별시 중랑구 상봉동', 'sales.staff4@example.com', '010-3000-0004', NULL, '2024-01-01', 1, 4, 2, 1),
    ('정영업5', '$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS', '1991-05-01', 'M', '서울특별시 성북구 돈암동', 'sales.staff5@example.com', '010-3000-0005', NULL, '2024-01-01', 1, 4, 2, 1),

    -- [ID: 14~48] 요양보호사 (부서: 1 영업팀 소속 / 직급: 5 요양보호사) -> 매니저: 2(영업팀장)
    ('김요양1','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-01-01','F','서울특별시 강남구 논현동','cg1@example.com','010-4000-0001',NULL,'2024-01-01',1,5,2,1),
    ('이요양2','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-02-01','F','서울특별시 강남구 삼성동','cg2@example.com','010-4000-0002',NULL,'2024-01-01',1,5,2,1),
    ('박요양3','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-03-01','F','서울특별시 송파구 가락동','cg3@example.com','010-4000-0003',NULL,'2024-01-01',1,5,2,1),
    ('최요양4','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-04-01','F','서울특별시 송파구 문정동','cg4@example.com','010-4000-0004',NULL,'2024-01-01',1,5,2,1),
    ('정요양5','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-05-01','F','서울특별시 서초구 반포동','cg5@example.com','010-4000-0005',NULL,'2024-01-01',1,5,2,1),
    ('강요양6','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-06-01','F','서울특별시 서초구 잠원동','cg6@example.com','010-4000-0006',NULL,'2024-01-01',1,5,2,1),
    ('오요양7','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-07-01','F','서울특별시 강동구 길동','cg7@example.com','010-4000-0007',NULL,'2024-01-01',1,5,2,1),
    ('윤요양8','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-08-01','F','서울특별시 강동구 둔촌동','cg8@example.com','010-4000-0008',NULL,'2024-01-01',1,5,2,1),
    ('서요양9','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-09-01','F','서울특별시 영등포구 당산동','cg9@example.com','010-4000-0009',NULL,'2024-01-01',1,5,2,1),
    ('문요양10','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-10-01','F','서울특별시 영등포구 도림동','cg10@example.com','010-4000-0010',NULL,'2024-01-01',1,5,2,1),
    ('유요양11','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-11-01','F','서울특별시 마포구 상암동','cg11@example.com','010-4000-0011',NULL,'2024-01-01',1,5,2,1),
    ('배요양12','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1980-12-01','F','서울특별시 마포구 성산동','cg12@example.com','010-4000-0012',NULL,'2024-01-01',1,5,2,1),
    ('신요양13','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-01-01','F','서울특별시 용산구 한남동','cg13@example.com','010-4000-0013',NULL,'2024-01-01',1,5,2,1),
    ('황요양14','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-02-01','F','서울특별시 용산구 청파동','cg14@example.com','010-4000-0014',NULL,'2024-01-01',1,5,2,1),
    ('곽요양15','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-03-01','F','서울특별시 성동구 금호동','cg15@example.com','010-4000-0015',NULL,'2024-01-01',1,5,2,1),
    ('정요양16','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-04-01','F','서울특별시 성동구 행당동','cg16@example.com','010-4000-0016',NULL,'2024-01-01',1,5,2,1),
    ('서요양17','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-05-01','F','서울특별시 동작구 사당동','cg17@example.com','010-4000-0017',NULL,'2024-01-01',1,5,2,1),
    ('최요양18','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-06-01','F','서울특별시 동작구 상도동','cg18@example.com','010-4000-0018',NULL,'2024-01-01',1,5,2,1),
    ('홍요양19','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-07-01','F','서울특별시 관악구 신림동','cg19@example.com','010-4000-0019',NULL,'2024-01-01',1,5,2,1),
    ('김요양20','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-08-01','F','서울특별시 관악구 봉천동','cg20@example.com','010-4000-0020',NULL,'2024-01-01',1,5,2,1),
    ('박요양21','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-09-01','F','서울특별시 강서구 화곡동','cg21@example.com','010-4000-0021',NULL,'2024-01-01',1,5,2,1),
    ('조요양22','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-10-01','F','서울특별시 강서구 등촌동','cg22@example.com','010-4000-0022',NULL,'2024-01-01',1,5,2,1),
    ('유요양23','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-11-01','F','서울특별시 구로구 구로동','cg23@example.com','010-4000-0023',NULL,'2024-01-01',1,5,2,1),
    ('임요양24','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1981-12-01','F','서울특별시 구로구 고척동','cg24@example.com','010-4000-0024',NULL,'2024-01-01',1,5,2,1),
    ('차요양25','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-01-01','F','서울특별시 금천구 가산동','cg25@example.com','010-4000-0025',NULL,'2024-01-01',1,5,2,1),
    ('백요양26','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-02-01','F','서울특별시 금천구 독산동','cg26@example.com','010-4000-0026',NULL,'2024-01-01',1,5,2,1),
    ('송요양27','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-03-01','F','서울특별시 중구 신당동','cg27@example.com','010-4000-0027',NULL,'2024-01-01',1,5,2,1),
    ('심요양28','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-04-01','F','서울특별시 중구 필동','cg28@example.com','010-4000-0028',NULL,'2024-01-01',1,5,2,1),
    ('권요양29','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-05-01','F','서울특별시 종로구 삼청동','cg29@example.com','010-4000-0029',NULL,'2024-01-01',1,5,2,1),
    ('고요양30','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-06-01','F','서울특별시 종로구 명륜동','cg30@example.com','010-4000-0030',NULL,'2024-01-01',1,5,2,1),
    ('은요양31','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-07-01','F','서울특별시 노원구 상계동','cg31@example.com','010-4000-0031',NULL,'2024-01-01',1,5,2,1),
    ('윤요양32','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-08-01','F','서울특별시 도봉구 쌍문동','cg32@example.com','010-4000-0032',NULL,'2024-01-01',1,5,2,1),
    ('계요양33','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-09-01','F','서울특별시 강북구 수유동','cg33@example.com','010-4000-0033',NULL,'2024-01-01',1,5,2,1),
    ('맹요양34','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-10-01','F','서울특별시 서대문구 남가좌동','cg34@example.com','010-4000-0034',NULL,'2024-01-01',1,5,2,1),
    ('류요양35','$2a$10$Zd8/G4.o2ksF14yiZ38we.unBnNNrGAeNxhYABqa9qV5ZD8ctncOS','1982-11-01','F','서울특별시 양천구 목동','cg35@example.com','010-4000-0035',NULL,'2024-01-01',1,5,2,1);
-- -----------------------------------------------------------
-- 3. 직원 근무 이력 (Employee Career)
-- -----------------------------------------------------------
INSERT INTO employee_career (company_name, work_period, task, employee_id)
VALUES
    ('온케어복지센터', '2018-01 ~ 2023-12', '센터장', 1),
    ('행복재가복지센터', '2013-03 ~ 2017-12', '운영팀장 / 인사총괄', 1),
    ('늘푸른요양원', '2008-01 ~ 2012-12', '사회복지사', 1),
    ('스마일재가센터', '2017-01 ~ 2023-12', '영업팀장', 2),
    ('희망복지용품', '2012-01 ~ 2016-12', '영업사원', 2),
    ('효자재요양센터', '2016-01 ~ 2023-12', '자재팀장', 3),
    ('굿케어의료용품', '2011-01 ~ 2015-12', '창고/재고 관리', 3),
    ('굿케어의료용품', '2020-01 ~ 2023-12', '자재 입출고 관리', 4),
    ('굿케어의료용품', '2021-03 ~ 2023-12', '소모품 발주 및 재고조사', 5),
    ('편안복지용품', '2019-01 ~ 2022-12', '창고 정리 및 출고 지원', 6),
    ('온케어복지센터', '2020-01 ~ 2023-12', '방문요양 영업', 9),
    ('행복재가복지센터', '2019-01 ~ 2023-12', '신규 수급자 상담/계약', 10),
    ('스마일재가센터', '2018-01 ~ 2022-12', '병원 연계 영업', 11),
    ('사랑재가요양센터', '2019-01 ~ 2023-12', '방문요양(치매 어르신 담당)', 14),
    ('사랑재가요양센터', '2020-03 ~ 2023-12', '방문요양(와상 어르신 위생관리)',15),
    ('행복재가복지센터', '2018-01 ~ 2022-12', '방문목욕 보조', 16),
    ('행복재가복지센터', '2017-01 ~ 2021-12', '방문요양(일상생활 지원)', 17),
    ('늘푸른요양원', '2016-01 ~ 2020-12', '생활실 케어', 18),
    ('편안요양원', '2015-01 ~ 2019-12', '야간 근무 케어', 19),
    ('온케어복지센터', '2020-01 ~ 2023-12', '방문요양(독거 어르신 지원)', 20),
    ('햇살재가센터', '2019-01 ~ 2023-12', '방문요양(인지활동 중심)', 21),
    ('햇살재가센터', '2018-01 ~ 2022-12', '방문요양(가사/식사 지원)', 22),
    ('사랑요양원', '2016-01 ~ 2021-12', '야간 생활실 케어', 23),
    ('편안노인전문요양원', '2015-01 ~ 2020-12', '물리치료 보조', 24),
    ('늘푸른재가센터', '2017-01 ~ 2023-12', '방문요양(고위험 어르신 담당)', 25),
    ('늘푸른재가센터', '2016-01 ~ 2020-12', '방문요양(일반 어르신)', 26),
    ('행복노인복지센터', '2014-01 ~ 2019-12', '주간보호센터 프로그램 지원', 27),
    ('행복노인복지센터', '2018-01 ~ 2023-12', '주간보호센터 송영차량 동승', 28),
    ('온누리재가요양센터', '2017-01 ~ 2022-12', '방문요양(치매 어르신 인지훈련)',29),
    ('온누리재가요양센터', '2016-01 ~ 2021-12', '방문요양(가사 및 말벗)', 30),
    ('스마일요양원', '2015-01 ~ 2020-12', '생활실 케어 및 위생관리', 31),
    ('스마일요양원', '2014-01 ~ 2019-12', '식사 보조 및 이동 보조', 32),
    ('해피재가복지센터', '2019-01 ~ 2023-12', '방문요양(장기고객 담당)', 33),
    ('해피재가복지센터', '2017-01 ~ 2018-12', '방문요양(단기요양)', 34),
    ('온가족복지센터', '2016-01 ~ 2020-12', '방문요양(부부 수급자 케어)', 35);

/* [파트 3] 종속 주체 및 관계 데이터
   - 수급자 (FK: potential_customer, m_risk_level)
   - 요양보호사 (FK: employee)
   - 보호자, 등급이력, 위험요소, 태그 등 (FK: beneficiary, care_worker)
   - 직원 권한 (FK: employee, m_authorities)
*/

-- -----------------------------------------------------------
-- 1. 수급자 (Beneficiary) - ID 1~20 생성
-- -----------------------------------------------------------
INSERT INTO beneficiary(id, name, gender, birthdate, address, phone, out_of_poket_ratio, status, potential_customer_id, risk_id) VALUES
                                                                                                                                     (1, '김영수', 'M', '1942-01-15', '서울특별시 강남구 역삼동',   '010-1111-0001', 0.20, 1, 1, 1),
                                                                                                                                     (2, '이정자', 'F', '1945-03-22', '서울특별시 서초구 서초동',   '010-1111-0002', 0.15, 1, 2, 2),
                                                                                                                                     (3, '박종원', 'M', '1939-07-10', '서울특별시 송파구 잠실동',   '010-1111-0003', 0.30, 0, 3, 3), -- 만료
                                                                                                                                     (4, '최미숙', 'F', '1950-11-05', '서울특별시 관악구 봉천동',   '010-1111-0004', 0.10, 1, 4, 1),
                                                                                                                                     (5, '정호석', 'M', '1948-06-18', '서울특별시 노원구 상계동',   '010-1111-0005', 0.25, 0, 5, 2), -- 해지
                                                                                                                                     (6, '오순자', 'F', '1941-02-03', '서울특별시 도봉구 창동',     '010-2222-0001', 0.20, 1, 6, 3),
                                                                                                                                     (7, '한병철', 'M', '1937-09-12', '서울특별시 강북구 수유동',   '010-2222-0002', 0.35, 1, 7, 3), -- risk_id 4가 없으므로 3으로 대체
                                                                                                                                     (8, '서영희', 'F', '1952-01-27', '서울특별시 은평구 응암동',   '010-2222-0003', 0.10, 1, 8, 1),
                                                                                                                                     (9, '신동호', 'M', '1946-08-09', '서울특별시 마포구 망원동',   '010-2222-0004', 0.18, 1, 9, 2),
                                                                                                                                     (10,'조은자', 'F', '1954-05-14', '서울특별시 용산구 한강로동', '010-2222-0005', 0.22, 0, 10, 3),
                                                                                                                                     (11,'윤재식', 'M', '1940-12-01', '서울특별시 중랑구 면목동',   '010-3333-0001', 0.27, 1, 11, 2),
                                                                                                                                     (12,'배말순', 'F', '1947-04-25', '서울특별시 동대문구 장안동', '010-3333-0002', 0.19, 1, 12, 1),
                                                                                                                                     (13,'임성호', 'M', '1938-10-30', '서울특별시 성북구 정릉동',   '010-3333-0003', 0.32, 1, 13, 1), -- risk_id 4 대체
                                                                                                                                     (14,'권정임', 'F', '1951-07-19', '서울특별시 성동구 금호동',   '010-3333-0004', 0.14, 1, 14, 2),
                                                                                                                                     (15,'차동수', 'M', '1944-09-08', '서울특별시 광진구 중곡동',   '010-3333-0005', 0.28, 1, 15, 3),
                                                                                                                                     (16,'홍금자', 'F', '1956-02-11', '서울특별시 강서구 화곡동',   '010-4444-0001', 0.12, 1, 16, 1),
                                                                                                                                     (17,'문상철', 'M', '1936-06-17', '서울특별시 양천구 신정동',   '010-4444-0002', 0.40, 1, 17, 2), -- risk_id 5 대체
                                                                                                                                     (18,'남정희', 'F', '1949-01-02', '서울특별시 구로구 개봉동',   '010-4444-0003', 0.20, 1, 18, 2),
                                                                                                                                     (19,'백영준', 'M', '1943-08-26', '서울특별시 금천구 독산동',   '010-4444-0004', 0.24, 0, 19, 3),
                                                                                                                                     (20,'노순례', 'F', '1957-11-29', '서울특별시 영등포구 대림동', '010-4444-0005', 0.10, 0, 20, 1);

-- -----------------------------------------------------------
-- 2. 요양보호사 (Care Worker) - ID 1~10 생성
-- -----------------------------------------------------------
-- [중요] Employee ID 14~23번 직원을 요양보호사 테이블에 등록합니다.
INSERT INTO care_worker (employee_id)
VALUES
    (14),(15),(16),(17),(18),(19),(20),(21),(22),(23),
    (24),(25),(26),(27),(28),(29),(30),(31),(32),(33),
    (34),(35),(36),(37),(38),(39),(40),(41),(42),(43),
    (44),(45);
-- -----------------------------------------------------------
-- 3. 보호자 (Guardian)
-- -----------------------------------------------------------
INSERT INTO guardian (name, phone, relation, is_primary, beneficiary_id) VALUES
                                                                             ('김지훈', '010-2222-0001', '아들', 'Y', 1), ('박지수', '010-2222-0002', '딸', 'Y', 2),
                                                                             ('이민우', '010-2222-0003', '아들', 'Y', 3), ('최유리', '010-2222-0004', '딸', 'Y', 4),
                                                                             ('정세연', '010-2222-0005', '딸', 'Y', 5),   ('오준호', '010-2222-0006', '아들', 'Y', 6),
                                                                             ('한소민', '010-2222-0007', '손녀', 'Y', 7), ('서지훈', '010-2222-0008', '손자', 'Y', 8),
                                                                             ('문채원', '010-2222-0009', '딸', 'Y', 9),   ('유지훈', '010-2222-0010', '아들', 'Y', 10),
                                                                             ('장민지', '010-2222-0011', '딸', 'Y', 11),  ('배수빈', '010-2222-0012', '아들', 'Y', 12),
                                                                             ('신하늘', '010-2222-0013', '딸', 'Y', 13),  ('노지훈', '010-2222-0014', '아들', 'Y', 14),
                                                                             ('권유나', '010-2222-0015', '손녀', 'Y', 15), ('임재민', '010-2222-0016', '아들', 'Y', 16),
                                                                             ('조민서', '010-2222-0017', '딸', 'Y', 17),  ('하지우', '010-2222-0018', '손녀', 'Y', 18),
                                                                             ('설민호', '010-2222-0019', '친구', 'N', 19), ('양세은', '010-2222-0020', '딸', 'Y', 20);

-- -----------------------------------------------------------
-- 4. 수급자 상세 정보 (등급, 위험요소, 태그, 특이사항)
-- -----------------------------------------------------------

-- 수급자별 장기요양등급 이력
INSERT INTO beneficiary_care_level (start_date, end_date, number, renewal, beneficiary_id) VALUES
                                                                                               ('2023-01-01', '2025-01-01', 20230001, 'Y', 1), ('2023-02-01', '2025-02-01', 20230002, 'Y', 2),
                                                                                               ('2023-03-01', '2025-03-01', 20230003, 'N', 3), ('2023-04-01', '2025-04-01', 20230004, 'Y', 4),
                                                                                               ('2023-05-01', '2025-05-01', 20230005, 'N', 5), ('2023-06-01', '2025-06-01', 20230006, 'Y', 6),
                                                                                               ('2023-07-01', '2025-07-01', 20230007, 'Y', 7), ('2023-08-01', '2025-08-01', 20230008, 'N', 8),
                                                                                               ('2023-09-01', '2025-09-01', 20230009, 'Y', 9), ('2023-10-01', '2025-10-01', 20230010, 'N', 10),
                                                                                               ('2023-11-01', '2025-11-01', 20240001, 'Y', 11), ('2023-12-01', '2025-12-01', 20240002, 'N', 12),
                                                                                               ('2024-01-01', '2026-01-01', 20240003, 'Y', 13), ('2024-02-01', '2026-02-01', 20240004, 'Y', 14),
                                                                                               ('2024-03-01', '2026-03-01', 20240005, 'N', 15), ('2024-04-01', '2026-04-01', 20240006, 'Y', 16),
                                                                                               ('2024-05-01', '2026-05-01', 20240007, 'Y', 17), ('2024-06-01', '2026-06-01', 20240008, 'N', 18),
                                                                                               ('2024-07-01', '2026-07-01', 20240009, 'N', 19), ('2024-08-01', '2026-08-01', 20240010, 'N', 20);

-- 수급자별 위험요소 (RiskOfMember)
INSERT INTO riskOfMember (beneficiary_id, risk_id) VALUES
                                                       (1,1),(1,3),(1,6), (2,1),(2,2), (3,3),(3,1), (3,8), (4,9), (5,5),
                                                       (6,5),(6,7),(7,1), (7,3), (8,2), (9,3), (9,6),(10,1),
                                                       (11,2), (12,3), (13,1),(13,2), (14,3), (15,5),
                                                       (16,2), (17,1),(17,2), (18,1),(18,2), (19,6), (20,2),(20,3);
-- 수급자 태그 (Tag of Beneficiary)
INSERT INTO tag_of_beneficiary (beneficiary_id, tag_id) VALUES
                                                            (1, 1),(1, 2), (2, 2), (3, 3),(3, 5), (4, 1), (5, 4),
                                                            (6, 2),(6, 3), (7, 5), (8, 1),(8, 4), (9, 3), (10,2),(10,5),
                                                            (11,1), (12,2), (13,3), (14,4), (15,5),
                                                            (16,1),(16,2), (17,3), (18,4), (19,2),(19,5), (20,1);

-- 수급자 특이사항 (Beneficiary Significant)
INSERT INTO beneficiary_significant (beneficiary_id, significant_id) VALUES
                                                                         (1, 1), (1, 6), (2, 2), (3, 7), (3, 8), (4, 1), (5, 9), (5, 10), (6, 3), (7, 4),
                                                                         (8, 5), (9, 6), (10, 1), (11, 7), (12, 8), (13, 9), (14, 10), (15, 2), (16, 3), (17, 4);

-- 수급자 금액산정 (Beneficiary Count)
INSERT INTO beneficiary_count (beneficiary_care_level_id, m_care_level_id) VALUES
                                                                               (1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 1), (8, 2), (9, 3), (10,4),
                                                                               (11,5), (12,6), (13,1), (14,2), (15,3), (16,4), (17,5), (18,6), (19,1), (20,2);

-- -----------------------------------------------------------
-- 5. 직원 권한 및 요양보호사 상세 (Authorities, Certificate, Education)
-- -----------------------------------------------------------

-- 직원별 권한 (Authorities of Employee)
-- 순서 변경: (authority_code, employee_id) -> (employee_id, authority_code)

-- 1. 센터장 (ID 1)
INSERT INTO authorities_of_employee (employee_id, authority_code) VALUES (1, 5), (1, 2);

-- 2. 영업팀장 (ID 2)
INSERT INTO authorities_of_employee (employee_id, authority_code) VALUES (2, 4), (2, 6), (2, 2);

-- 3. 자재팀장 (ID 3)
INSERT INTO authorities_of_employee (employee_id, authority_code) VALUES (3, 4), (3, 7), (3, 2);

-- 4. 자재팀원 (ID 4~8)
INSERT INTO authorities_of_employee (employee_id, authority_code) VALUES
                                                                      (4, 7), (4, 2),
                                                                      (5, 7), (5, 2),
                                                                      (6, 7), (6, 2),
                                                                      (7, 7), (7, 2),
                                                                      (8, 7), (8, 2);

-- 5. 영업팀원 (ID 9~13)
INSERT INTO authorities_of_employee (employee_id, authority_code) VALUES
                                                                      (9, 6), (9, 2),
                                                                      (10, 6), (10, 2),
                                                                      (11, 6), (11, 2),
                                                                      (12, 6), (12, 2),
                                                                      (13, 6), (13, 2);

-- 6. 요양보호사 (ID 14~23만 예시 등록)
INSERT INTO authorities_of_employee (employee_id, authority_code) VALUES
                                                                      (14, 1), (14, 2),
                                                                      (15, 1), (15, 2),
                                                                      (16, 1), (16, 2),
                                                                      (17, 1), (17, 2),
                                                                      (18, 1), (18, 2),
                                                                      (19, 1), (19, 2),
                                                                      (20, 1), (20, 2),
                                                                      (21, 1), (21, 2),
                                                                      (22, 1), (22, 2),
                                                                      (23, 1), (23, 2);


-- 요양보호사 자격증 (Care Worker Certificate)
INSERT INTO care_worker_certificate
(care_worker_id, certificate_id, license_no, issue_date, expire_date, status)
VALUES
    (1,  1, 'LIC-001', '2022-01-01', '2027-01-01', 2),
    (2,  1, 'LIC-002', '2022-01-01', '2027-01-01', 2),
    (3,  1, 'LIC-003', '2022-01-01', '2027-01-01', 2),
    (4,  1, 'LIC-004', '2022-01-01', '2027-01-01', 2),
    (5,  1, 'LIC-005', '2022-01-01', '2027-01-01', 2),
    (6,  1, 'LIC-006', '2022-01-01', '2027-01-01', 2),
    (7,  1, 'LIC-007', '2022-01-01', '2027-01-01', 2),
    (8,  1, 'LIC-008', '2022-01-01', '2027-01-01', 2),
    (9,  1, 'LIC-009', '2022-01-01', '2027-01-01', 2),
    (10, 1, 'LIC-010', '2022-01-01', '2027-01-01', 2),
    (11, 1, 'LIC-011', '2022-01-01', '2027-01-01', 2),
    (12, 1, 'LIC-012', '2022-01-01', '2027-01-01', 2),
    (13, 1, 'LIC-013', '2022-01-01', '2027-01-01', 2),
    (14, 1, 'LIC-014', '2022-01-01', '2027-01-01', 2),
    (15, 1, 'LIC-015', '2022-01-01', '2027-01-01', 2),
    (16, 1, 'LIC-016', '2022-01-01', '2027-01-01', 2),
    (17, 1, 'LIC-017', '2022-01-01', '2027-01-01', 2),
    (18, 1, 'LIC-018', '2022-01-01', '2027-01-01', 2),
    (19, 1, 'LIC-019', '2022-01-01', '2027-01-01', 2),
    (20, 1, 'LIC-020', '2022-01-01', '2027-01-01', 2),
    (21, 1, 'LIC-021', '2022-01-01', '2027-01-01', 2),
    (22, 1, 'LIC-022', '2022-01-01', '2027-01-01', 2),
    (23, 1, 'LIC-023', '2022-01-01', '2027-01-01', 2),
    (24, 1, 'LIC-024', '2022-01-01', '2027-01-01', 2),
    (25, 1, 'LIC-025', '2022-01-01', '2027-01-01', 2),
    (26, 1, 'LIC-026', '2022-01-01', '2027-01-01', 2),
    (27, 1, 'LIC-027', '2022-01-01', '2027-01-01', 2),
    (28, 1, 'LIC-028', '2022-01-01', '2027-01-01', 2),
    (29, 1, 'LIC-029', '2022-01-01', '2027-01-01', 2),
    (30, 1, 'LIC-030', '2022-01-01', '2027-01-01', 2),
    (31, 1, 'LIC-031', '2022-01-01', '2027-01-01', 2),
    (32, 1, 'LIC-032', '2022-01-01', '2027-01-01', 2);

-- 2) 추가 자격증: 사람마다 다르게 / 여러 개 (2~6)
INSERT INTO care_worker_certificate
(care_worker_id, certificate_id, license_no, issue_date, expire_date, status)
VALUES
-- 그룹 A: 2개 보유
(1,  2, 'LIC-001-A', '2022-02-01', '2024-02-01', 2),
(1,  6, 'LIC-001-B', '2022-06-01', '2023-06-01', 2),

(2,  3, 'LIC-002-A', '2022-03-01', '2024-03-01', 2),
(2,  5, 'LIC-002-B', '2022-05-01', '2024-05-01', 1),

(3,  2, 'LIC-003-A', '2022-02-01', '2024-02-01', 2),
(3,  4, 'LIC-003-B', '2022-04-01', '2023-04-01', 2),

(4,  6, 'LIC-004-A', '2022-06-01', '2023-06-01', 2),
(4,  5, 'LIC-004-B', '2022-05-01', '2024-05-01', 2),

-- 그룹 B: 1개만 보유
(5,  2, 'LIC-005-A', '2022-02-01', '2024-02-01', 2),
(6,  3, 'LIC-006-A', '2022-03-01', '2024-03-01', 2),
(7,  4, 'LIC-007-A', '2022-04-01', '2023-04-01', 2),
(8,  6, 'LIC-008-A', '2022-06-01', '2023-06-01', 2),

-- 그룹 C: 3개 보유
(9,  2, 'LIC-009-A', '2022-02-01', '2024-02-01', 2),
(9,  3, 'LIC-009-B', '2022-03-01', '2024-03-01', 2),
(9,  6, 'LIC-009-C', '2022-06-01', '2023-06-01', 2),

(10, 4, 'LIC-010-A', '2022-04-01', '2023-04-01', 2),
(10, 5, 'LIC-010-B', '2022-05-01', '2024-05-01', 2),
(10, 6, 'LIC-010-C', '2022-06-01', '2023-06-01', 2),

-- 나머지도 분포 다양하게(예시)
(11, 2, 'LIC-011-A', '2022-02-01', '2024-02-01', 2),
(12, 3, 'LIC-012-A', '2022-03-01', '2024-03-01', 2),
(12, 6, 'LIC-012-B', '2022-06-01', '2023-06-01', 2),

(13, 4, 'LIC-013-A', '2022-04-01', '2023-04-01', 2),
(14, 5, 'LIC-014-A', '2022-05-01', '2024-05-01', 1),
(14, 2, 'LIC-014-B', '2022-02-01', '2024-02-01', 2),

(15, 6, 'LIC-015-A', '2022-06-01', '2023-06-01', 2),
(16, 2, 'LIC-016-A', '2022-02-01', '2024-02-01', 2),
(16, 3, 'LIC-016-B', '2022-03-01', '2024-03-01', 2),

(17, 4, 'LIC-017-A', '2022-04-01', '2023-04-01', 2),
(18, 6, 'LIC-018-A', '2022-06-01', '2023-06-01', 2),
(18, 5, 'LIC-018-B', '2022-05-01', '2024-05-01', 2),

(19, 2, 'LIC-019-A', '2022-02-01', '2024-02-01', 2),
(20, 3, 'LIC-020-A', '2022-03-01', '2024-03-01', 2),
(21, 4, 'LIC-021-A', '2022-04-01', '2023-04-01', 2),
(22, 6, 'LIC-022-A', '2022-06-01', '2023-06-01', 2),

(23, 5, 'LIC-023-A', '2022-05-01', '2024-05-01', 1),
(23, 2, 'LIC-023-B', '2022-02-01', '2024-02-01', 2),

(24, 3, 'LIC-024-A', '2022-03-01', '2024-03-01', 2),
(24, 6, 'LIC-024-B', '2022-06-01', '2023-06-01', 2),

(25, 2, 'LIC-025-A', '2022-02-01', '2024-02-01', 2),
(26, 4, 'LIC-026-A', '2022-04-01', '2023-04-01', 2),
(27, 6, 'LIC-027-A', '2022-06-01', '2023-06-01', 2),

(28, 2, 'LIC-028-A', '2022-02-01', '2024-02-01', 2),
(28, 3, 'LIC-028-B', '2022-03-01', '2024-03-01', 2),
(28, 4, 'LIC-028-C', '2022-04-01', '2023-04-01', 2),

(29, 5, 'LIC-029-A', '2022-05-01', '2024-05-01', 2),
(30, 2, 'LIC-030-A', '2022-02-01', '2024-02-01', 2),
(31, 3, 'LIC-031-A', '2022-03-01', '2024-03-01', 2),
(32, 6, 'LIC-032-A', '2022-06-01', '2023-06-01', 2);

-- 요양보호사 교육 이력 (Education)
-- id2는 care_worker_certificate의 ID를 참조함 (1~10 생성됨)
INSERT INTO education
(care_worker_certificate_id, edu_name, institution, edu_date, next_edu_date, is_overdue, status)
VALUES
-- certificate_id = 1 : 요양보호사 자격증
(1, '요양보호사 법정보수교육', '국민건강보험공단',
 '2024-01-15', '2027-01-15', 0, 2),

-- certificate_id = 2 : 치매전문교육 이수증
(2, '치매 이해 및 대응 교육', '중앙치매센터',
 '2024-01-20', '2026-01-20', 0, 2),

-- certificate_id = 3 : 노인돌봄 전문교육 이수증
(3, '노인 인권 및 학대 예방 교육', '노인보호전문기관',
 '2024-02-10', '2026-02-10', 0, 2),

-- certificate_id = 4 : 장기요양 실무교육 수료증
(4, '장기요양 실무 보수교육', '국민건강보험공단',
 '2024-04-01', '2025-04-01', 0, 2),

-- certificate_id = 5 : 응급처치 및 심폐소생술 자격증
(5, '응급처치 및 심폐소생술 갱신교육', '대한적십자사',
 '2024-05-01', '2026-05-01', 0, 2),

-- certificate_id = 6 : 감염관리·위생 안전교육 이수증
(6, '감염병 예방 및 위생관리 교육', '질병관리청',
 '2024-03-05', '2025-03-05', 0, 2);





-- 요양보호사 태그 및 서비스 유형
INSERT INTO tag_of_care_worker (care_worker_id, tag_id) VALUES
                                                            (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,1);

INSERT INTO care_worker_service_type (care_worker_id, m_service_type_id) VALUES
                                                                             (1,1),(2,2),(3,3),(4,1),(5,2),(6,3),(7,1),(8,2),(9,3),(10,1);

/* [파트 4] 계약 및 물류/매칭 프로세스
   - 계약서 (FK: beneficiary, contract_status)
   - 서류 양식 (FK: beneficiary, m_form_category)
   - 매칭 (FK: beneficiary, care_worker) *중요: ID 매핑 수정됨
   - 용품 재고 (FK: m_care_product, m_location_status)
   - 렌탈 계약 및 상세 (FK: beneficiary, employee, care_product)
*/

-- -----------------------------------------------------------
-- 1. 수급자 계약 정보 (Contract)
-- -----------------------------------------------------------
INSERT INTO contract (id, beneficiary_id, start_date, end_date, file_path, contract_status) VALUES
                                                                                                (1, 1, '2024-01-01', NULL,         '/files/contract/1.pdf',  '진행중'),
                                                                                                (2, 2, '2024-01-05', NULL,         '/files/contract/2.pdf',  '진행중'),
                                                                                                (3, 3, '2023-07-01', '2025-06-30', '/files/contract/3.pdf',  '만료'),
                                                                                                (4, 4, '2024-02-01', NULL,         '/files/contract/4.pdf',  '진행중'),
                                                                                                (5, 5, '2023-10-01', '2024-09-30', '/files/contract/5.pdf',  '해지'),
                                                                                                (6, 6, '2024-01-10', NULL,         '/files/contract/6.pdf',  '진행중'),
                                                                                                (7, 7, '2024-01-15', NULL,         '/files/contract/7.pdf',  '진행중'),
                                                                                                (8, 8, '2023-12-01', '2025-11-30', '/files/contract/8.pdf',  '진행중'),
                                                                                                (9, 9, '2024-01-20', NULL,         '/files/contract/9.pdf',  '진행중'),
                                                                                                (10,10,'2023-06-01', '2024-05-31', '/files/contract/10.pdf', '만료'),
                                                                                                (11,11,'2024-02-01', NULL,         '/files/contract/11.pdf', '진행중'),
                                                                                                (12,12,'2024-02-05', NULL,         '/files/contract/12.pdf', '진행중'),
                                                                                                (13,13,'2023-09-01', '2025-08-31', '/files/contract/13.pdf', '진행중'),
                                                                                                (14,14,'2024-01-01', NULL,         '/files/contract/14.pdf', '진행중'),
                                                                                                (15,15,'2023-11-01', '2025-10-31', '/files/contract/15.pdf', '진행중'),
                                                                                                (16,16,'2024-01-10', NULL,         '/files/contract/16.pdf', '진행중'),
                                                                                                (17,17,'2023-05-01', '2025-04-30', '/files/contract/17.pdf', '진행중'),
                                                                                                (18,18,'2024-02-10', NULL,         '/files/contract/18.pdf', '진행중'),
                                                                                                (19,19,'2023-08-01', '2024-07-31', '/files/contract/19.pdf', '해지'),
                                                                                                (20,20,'2024-01-25', '2025-01-31', '/files/contract/20.pdf', '해지');

-- -----------------------------------------------------------
-- 2. 신청 서류 (Application Form)
-- -----------------------------------------------------------
INSERT INTO application_form
(id, mime_type, file_path, created_at, original_file_name, re_file_name, form_category_id,size)
VALUES
    (1,  'application/pdf', '/img/beneficiary/template_doctor_opinion.pdf','2025-01-05 11:00:00',
     '의사소견서(양식).pdf',          'template_doctor_opinion.pdf', 1,'119KB'),
    (2,  'application/pdf', '/img/beneficiary/template_apply_ltc.pdf',     '2025-01-05 11:30:00',
     '장기요양인정 신청,갱신,변경서(양식).pdf','template_apply_ltc.pdf', 2,'128KB'),
    (3,  'application/pdf', '/img/beneficiary/template_survey_ltc.pdf',    '2025-01-06 09:40:00',
     '장기요양인정조사표(양식).pdf',  'template_survey_ltc.pdf',     3,'123KB'),
    (4,  'application/pdf', '/img/beneficiary/template_cert_ltc.pdf',      '2025-01-06 10:00:00',
     '장기요양인정서(양식).pdf',      'template_cert_ltc.pdf',       4,'51KB'),
    (5,  'application/pdf', '/img/beneficiary/template_plan_ltc.pdf',      '2025-01-07 14:10:00',
     '장기요양급여 제공 계획서(양식).pdf','template_plan_ltc.pdf',   5,'70KB'),
    (6,  'application/pdf', '/img/beneficiary/template_record_visit.pdf',  '2025-01-08 15:00:00',
     '제공기록지(방문요양, 양식).pdf', 'template_record_visit.pdf',  6,'89KB'),
    (7, 'application/pdf', '/img/beneficiary/template_record_bath.pdf',   '2025-01-09 10:20:00',
     '제공기록지(방문목욕, 양식).pdf', 'template_record_bath.pdf',  7,'75KB'),
    (8, 'application/pdf', '/img/beneficiary/template_record_nurse.pdf',  '2025-01-09 10:40:00',
     '제공기록지(방문간호, 양식).pdf', 'template_record_nurse.pdf', 8,'64KB'),
    (9, 'application/pdf', '/img/beneficiary/template_healty.pdf',  '2025-01-09 10:50:00',
     '건강보험(중증치매 등)산정특례 신청서(양식).pdf', 'template_healty.pdf', 9,'149KB');
-- -----------------------------------------------------------
-- 3. 요양보호사 매칭 (Matching)
-- -----------------------------------------------------------
-- [중요] beneficiary_id는 1~20, care_worker_id는 1~10을 사용합니다.
INSERT INTO matching
(start_date, end_date, status, reason, create_at, beneficiary_id, care_worker_id)
VALUES
-- 이용 중 (Y)
('2024-01-01', NULL, 'Y', NULL, '2024-01-01 09:00:00', 1, 1),
('2024-02-01', NULL, 'Y', NULL, '2024-02-01 09:00:00', 2, 2),
('2024-02-15', NULL, 'Y', NULL, '2024-02-15 09:00:00', 4, 4),
('2023-06-01', NULL, 'Y', NULL, '2023-06-01 09:00:00', 6, 6),
('2024-03-01', NULL, 'Y', NULL, '2024-03-01 09:00:00', 7, 7),
('2024-03-01', NULL, 'Y', NULL, '2024-03-01 09:00:00', 8, 8),
('2024-01-20', NULL, 'Y', NULL, '2024-01-20 09:00:00', 9, 9),
('2024-01-10', NULL, 'Y', NULL, '2024-01-10 09:00:00', 11, 11),
('2024-02-01', NULL, 'Y', NULL, '2024-02-01 09:00:00', 13, 13),
('2024-03-01', NULL, 'Y', NULL, '2024-03-01 09:00:00', 14, 14),
('2024-03-05', NULL, 'Y', NULL, '2024-03-05 09:00:00', 15, 15),
('2024-01-05', NULL, 'Y', NULL, '2024-01-05 09:00:00', 16, 16),
('2024-02-10', NULL, 'Y', NULL, '2024-02-10 09:00:00', 17, 17),

-- 이용 종료 (N) : 종료일 = 2025-12-08 (오늘 기준 저번 주)
('2024-01-01', '2025-12-08', 'N', '서비스 기간 종료', '2024-01-01 09:00:00', 18, 18),
('2024-02-01', '2025-12-08', 'N', '보호자 요청으로 서비스 중단', '2024-02-01 09:00:00', 12, 12);

-- -----------------------------------------------------------
-- 4. 용품 재고 관리 (Care Product)
-- -----------------------------------------------------------
-- location_status: 1(창고), 2(배송중), 3(대여중), 4(회수중), 5(수리중) 등
INSERT INTO care_product (id, product_cd, amount, rental_amount, product_status, m_location_status_cd, last_id, bought_date) VALUES
                                                                                                                                 ('EM001-001', 'EM001', 800000.00, 35000.00, 2, 3, 1, '2024-01-15'),
                                                                                                                                 ('EM001-002', 'EM001', 800000.00, 35000.00, 2, 3, 2, '2024-02-10'),
                                                                                                                                 ('EM001-003', 'EM001', 800000.00, 35000.00, 1, 1, 3, '2024-03-05'),
                                                                                                                                 ('EM002-001', 'EM002', 500000.00, 25000.00, 2, 3, 1, '2024-01-20'),
                                                                                                                                 ('EM002-002', 'EM002', 500000.00, 25000.00, 3, 4, 2, '2024-04-01'),
                                                                                                                                 ('EM003-001', 'EM003', 400000.00, 28000.00, 2, 3, 1, '2024-02-15'),
                                                                                                                                 ('EM004-001', 'EM004', 150000.00, 18000.00, 2, 3, 1, '2024-03-10');

-- -----------------------------------------------------------
-- 5. 렌탈 계약 및 상세 (Rental Contract)
-- -----------------------------------------------------------
-- emp_id: 자재팀장(3) 또는 자재팀원(4)으로 매핑
INSERT INTO rental_contract (beneficiary_id, emp_id, product_cd, contract_status_cd,term_month,wanted_date, start_date, end_date) VALUES
                                                                                                                                      (1, 3, 'EM001', 2, 11, '2024-02-01', '2024-02-01', '2026-01-31'),
                                                                                                                                      (2, 3, 'EM002', 2, 10, '2024-02-01', '2024-02-01', '2025-12-31'),
                                                                                                                                      (3, 4, 'EM003', 1, 9, '2024-04-01', '2024-04-01', '2026-01-31');
-- 렌탈 상세 (어떤 개별 상품이 나갔는지)
INSERT INTO rental_product (rental_contract_cd, rental_status_id, product_id) VALUES
                                                                                  (1, 1, 'EM001-001'),
                                                                                  (2, 1, 'EM002-001'),
                                                                                  (3, 1, 'EM003-001');

-- 물류 작업 (입출고 예정)
INSERT INTO product_tasks (status, expected_date, is_confirmed, product_id, employee_id, created_at) VALUES
                                                                                                         (1, '2024-02-01', 'Y', 'EM001-001', 4, '2024-01-31 10:00:00'),
                                                                                                         (0, '2024-04-15', 'N', 'EM002-002', 4, '2024-04-10 14:00:00');

-- 요양보호사 물류 작업 (회수 등)
-- care_worker_id: 1~10
INSERT INTO product_tasks_for_worker (status, expected_date, is_confirmed, completed_at, created_at, product_id, care_worker_id, beneficiary_id) VALUES
                                                                                                                                                     (0, '2024-06-03', 'N', NULL, NOW(), 'EM001-001', 1, 3),
                                                                                                                                                     (1, '2024-06-07', 'Y', '2024-06-07 14:00:00', NOW(), 'EM004-001', 1, 4);

-- 요양보호사 할 일 (ToDo)
INSERT INTO todo_for_care_worker (todo, todo_date, type, clear_st, beneficiary_id, care_worker_id) VALUES
                                                                                                       ('12월 정기 방문 예정 확인','2025-12-22','방문예정',0, 1, 1),
                                                                                                       ('주 3회 방문 스케줄 확인','2025-12-23','방문예정',0, 2, 1),
                                                                                                       ('산소포화도 측정기 회수','2025-12-24','장비회수',0, 3, 1),
                                                                                                       ('보조보행기 회수','2025-12-25','장비회수',0, 4, 1),
                                                                                                       ('12월 21일 방문 간호 일지 작성','2025-12-22','요양일지작성',0, 5, 1),
                                                                                                       ('방문요양 활동일지 누락분 작성','2025-12-23','요양일지작성',0, 6, 1),
                                                                                                       ('낙상 위험 기초평가 진행','2025-12-24','기초평가',0, 7, 1),
                                                                                                       ('욕창 위험도 초기 평가','2025-12-25','기초평가',0, 8, 1),
                                                                                                       ('보호자 요청 방문 상담','2025-12-26','방문상담',0, 9, 1),
                                                                                                       ('정기 방문 상담 진행','2025-12-27','방문상담',0, 10, 1);

-- -----------------------------------------------------------
-- 1. 수급자 서비스 일정 (Beneficiary Schedule)
-- -----------------------------------------------------------
-- 기존 파트 4에서 2건만 들어갔다면, 중복 방지를 위해 삭제 후 다시 넣거나 없는 ID만 넣어야 합니다.
-- 안전하게 전체를 다시 정의합니다. (ID 1, 2가 이미 있다면 무시되거나 에러가 날 수 있으므로 제외하거나 수정 필요)
-- 여기서는 ID 3번부터 이어가거나, 전체를 제공합니다. (충돌 시 기존 데이터 삭제 권장)

INSERT INTO beneficiary_schedule
(id, day, start_time, end_time, beneficiary_id, service_type_id)
VALUES
-- beneficiary 1 (3)
(1,  1, '09:00:00', '11:00:00', 1, 1),
(2,  3, '14:00:00', '16:00:00', 1, 1),
(3,  6, '10:00:00', '12:00:00', 1, 2),

-- beneficiary 2 (2)
(4,  2, '09:00:00', '11:00:00', 2, 1),
(5,  5, '13:00:00', '15:00:00', 2, 2),

-- beneficiary 3 (4)
(6,  7, '10:00:00', '12:00:00', 3, 1),
(7,  3, '14:00:00', '16:00:00', 3, 1),
(8,  5, '16:00:00', '18:00:00', 3, 1),
(9,  1, '13:00:00', '15:00:00', 3, 2),

-- beneficiary 4 (2)
(10, 4, '09:30:00', '11:30:00', 4, 3),
(11, 6, '13:30:00', '15:30:00', 4, 3),

-- beneficiary 5 (3)
(12, 1, '08:30:00', '10:30:00', 5, 1),
(13, 2, '14:00:00', '16:00:00', 5, 2),
(14, 5, '10:00:00', '12:00:00', 5, 1),

-- beneficiary 6 (2)
(15, 3, '09:00:00', '11:00:00', 6, 1),
(16, 7, '15:00:00', '17:00:00', 6, 2),

-- beneficiary 7 (3)
(17, 5, '09:00:00', '11:00:00', 7, 3),
(18, 6, '13:00:00', '15:00:00', 7, 3),
(19, 2, '15:00:00', '17:00:00', 7, 3),

-- beneficiary 8 (3)
(20, 1, '10:00:00', '12:00:00', 8, 1),
(21, 4, '14:00:00', '16:00:00', 8, 1),
(22, 7, '16:00:00', '18:00:00', 8, 2),

-- beneficiary 9 (2)
(23, 2, '09:30:00', '11:30:00', 9, 2),
(24, 5, '13:30:00', '15:30:00', 9, 1),

-- beneficiary 10 (3)
(25, 3, '09:00:00', '11:00:00', 10, 1),
(26, 6, '14:00:00', '16:00:00', 10, 2),
(27, 1, '15:00:00', '17:00:00', 10, 1),

-- beneficiary 11 (2)
(28, 1, '09:00:00', '11:00:00', 11, 1),
(29, 4, '14:00:00', '16:00:00', 11, 2),

-- beneficiary 12 (3)
(30, 7, '10:00:00', '12:00:00', 12, 1),
(31, 2, '15:00:00', '17:00:00', 12, 2),
(32, 5, '09:00:00', '11:00:00', 12, 1),

-- beneficiary 13 (4)
(33, 5, '09:00:00', '11:00:00', 13, 1),
(34, 6, '13:00:00', '15:00:00', 13, 1),
(35, 7, '15:00:00', '17:00:00', 13, 2),
(36, 2, '10:00:00', '12:00:00', 13, 1),

-- beneficiary 14 (2)
(37, 1, '09:30:00', '11:30:00', 14, 2),
(38, 4, '14:30:00', '16:30:00', 14, 1),

-- beneficiary 15 (3)
(39, 3, '09:00:00', '11:00:00', 15, 1),
(40, 7, '13:00:00', '15:00:00', 15, 2),
(41, 5, '15:00:00', '17:00:00', 15, 1),

-- beneficiary 16 (2)
(42, 2, '10:00:00', '12:00:00', 16, 1),
(43, 5, '14:00:00', '16:00:00', 16, 2),

-- beneficiary 17 (3)
(44, 6, '09:00:00', '11:00:00', 17, 1),
(45, 3, '15:00:00', '17:00:00', 17, 1),
(46, 1, '13:00:00', '15:00:00', 17, 2),

-- beneficiary 18 (2)
(47, 1, '14:00:00', '16:00:00', 18, 3),
(48, 7, '09:00:00', '11:00:00', 18, 3),

-- beneficiary 19 (3)
(49, 4, '09:30:00', '11:30:00', 19, 1),
(50, 6, '13:30:00', '15:30:00', 19, 2),
(51, 2, '15:00:00', '17:00:00', 19, 1),

-- beneficiary 20 (2)
(52, 2, '09:00:00', '11:00:00', 20, 1),
(53, 5, '14:00:00', '16:00:00', 20, 2);
-- -----------------------------------------------------------
-- 2. 월별 서비스 금액 누계 (Cost of Beneficiary)
-- -----------------------------------------------------------
INSERT INTO cost_of_beneficiary (id, monthly_amount, month, beneficiary_id) VALUES
                                                                                (1,  850000, '2025-12', 1),
                                                                                (2,  900000, '2025-12', 2),
                                                                                (3,  650000, '2025-12', 4),
                                                                                (4,  810000, '2025-12', 6),
                                                                                (5,  790000, '2025-12', 7),
                                                                                (6,  600000, '2025-12', 8),
                                                                                (7,  680000, '2025-12', 9),
                                                                                (8,  700000, '2025-12', 11),
                                                                                (9,  650000, '2025-12', 12),
                                                                                (10, 620000, '2025-12', 13),
                                                                                (11, 640000, '2025-12', 14),
                                                                                (12, 710000, '2025-12', 15),
                                                                                (13, 690000, '2025-12', 16),
                                                                                (14, 750000, '2025-12', 17),
                                                                                (15, 720000, '2025-12', 18),
                                                                                (16, 860000, '2026-01', 1),
                                                                                (17, 910000, '2026-01', 2),
                                                                                (18, 660000, '2026-01', 4),
                                                                                (19, 820000, '2026-01', 6),
                                                                                (20, 800000, '2026-01', 7),
                                                                                (21, 610000, '2026-01', 8),
                                                                                (22, 690000, '2026-01', 9),
                                                                                (23, 710000, '2026-01', 11),
                                                                                (24, 660000, '2026-01', 12),
                                                                                (25, 630000, '2026-01', 13),
                                                                                (26, 650000, '2026-01', 14),
                                                                                (27, 720000, '2026-01', 15),
                                                                                (28, 700000, '2026-01', 16),
                                                                                (29, 760000, '2026-01', 17),
                                                                                (30, 730000, '2026-01', 18);

-- -----------------------------------------------------------
-- 3. 응급 알람 (Emergency)
-- -----------------------------------------------------------
INSERT INTO emergency (id, emergency_st, action_st, emergency_time, action_time, beneficiary_id) VALUES
                                                                                                     (1, 'Y', 'Y', '2025-01-05 08:30:00', '2025-01-05 09:00:00', 1),
                                                                                                     (2, 'Y', 'Y', '2025-01-10 21:10:00', '2025-01-10 21:40:00', 2),
                                                                                                     (3, 'Y', 'N', '2025-01-12 07:20:00', NULL, 3),
                                                                                                     (4, 'N', 'N', '2025-01-15 00:00:00', '2025-01-15 00:00:00', 4),
                                                                                                     (5, 'Y', 'Y', '2025-01-18 16:00:00', '2025-01-18 16:30:00', 5),
                                                                                                     (6, 'N', 'N', '2025-01-20 00:00:00', '2025-01-20 00:00:00', 6),
                                                                                                     (7, 'Y', 'Y', '2025-01-22 11:11:00', '2025-01-22 11:40:00', 7),
                                                                                                     (8, 'Y', 'Y', '2025-01-23 19:30:00', '2025-01-23 19:50:00', 8),
                                                                                                     (9, 'N', 'N', '2025-01-24 00:00:00', '2025-01-24 00:00:00', 9),
                                                                                                     (10,'Y', 'Y', '2025-01-25 05:45:00', '2025-01-25 06:00:00', 10),
                                                                                                     (11,'N', 'N', '2025-02-01 00:00:00', '2025-02-01 00:00:00', 11),
                                                                                                     (12,'Y', 'Y', '2025-02-03 14:00:00', '2025-02-03 14:20:00', 12),
                                                                                                     (13,'Y', 'Y', '2025-02-05 10:30:00', '2025-02-05 10:55:00', 13),
                                                                                                     (14,'N', 'N', '2025-02-06 00:00:00', '2025-02-06 00:00:00', 14),
                                                                                                     (15,'Y', 'Y', '2025-02-08 18:40:00', '2025-02-08 19:05:00', 15),
                                                                                                     (16,'N', 'N', '2025-02-10 00:00:00', '2025-02-10 00:00:00', 16),
                                                                                                     (17,'Y', 'Y', '2025-02-12 09:10:00', '2025-02-12 09:30:00', 17),
                                                                                                     (18,'N', 'N', '2025-02-13 00:00:00', '2025-02-13 00:00:00', 18),
                                                                                                     (19,'Y', 'Y', '2025-02-15 22:00:00', '2025-02-15 22:25:00', 19),
                                                                                                     (20,'N', 'N', '2025-02-16 00:00:00', NULL, 20);

-- -----------------------------------------------------------
-- 4. 요양등급 만료 임박 구간 (Expiration of Care Level)
-- -----------------------------------------------------------
INSERT INTO expiration_of_care_level (id, expired_section, outbound_status, extends_status, beneficiary_id, emp_id) VALUES
                                                                                                                        (1, 1, 'Y', 'Y',  1, 1),
                                                                                                                        (2, 2, 'Y', 'N',  2, 1),
                                                                                                                        (3, 1, 'Y', 'Y',  4, 2),
                                                                                                                        (4, 3, 'Y', 'Y',  6, 1),
                                                                                                                        (5, 1, 'N', 'N',  7, 2),
                                                                                                                        (6, 2, 'Y', 'Y',  8, 3),
                                                                                                                        (7, 3, 'Y', 'N',  9, 1),
                                                                                                                        (8, 2, 'N', 'N', 11, 3),
                                                                                                                        (9, 3, 'Y', 'Y', 12, 1),
                                                                                                                        (10,1, 'Y', 'N', 13, 2),
                                                                                                                        (11,2, 'Y', 'Y', 14, 3),
                                                                                                                        (12,3, 'N', 'N', 15, 1);

-- -----------------------------------------------------------
-- 5. 만료 알림 (Notice Expiration)
-- -----------------------------------------------------------
-- expiration_id는 위 테이블(expiration_of_care_level)의 ID를 참조합니다.
INSERT INTO notice_expiration (id, notice_date, memo, expiration_id, emp_id) VALUES
                                                                                 (1, '2024-10-01 10:00:00', '90일 전 1차 안내(전화)',               1, 1),
                                                                                 (2, '2024-11-01 10:00:00', '60일 전 2차 안내(문자)',               1, 1),
                                                                                 (3, '2024-10-05 09:30:00', '90일 전 안내, 보호자 통화 완료',       2, 1),
                                                                                 (4, '2024-11-05 09:30:00', '60일 전 안내, 연장 의사 보류',         2, 1),
                                                                                 (5, '2024-09-25 15:00:00', '90일 전 안내, 연장 신청 예정',         4, 2),
                                                                                 (6, '2024-09-30 14:10:00', '90일 전 안내, 수급자 직접 통화',       6, 1),
                                                                                 (7, '2024-10-30 14:10:00', '60일 전 안내, 상태 양호',             6, 1),
                                                                                 (8, '2024-10-02 09:00:00', '90일 전 안내, 연락 불가(번호 변경 추정)', 7, 2),
                                                                                 (9, '2024-09-27 13:30:00', '90일 전 안내, 연장 의사 있음',         8, 3),
                                                                                 (10,'2024-10-27 13:30:00', '60일 전 안내, 서류 접수 완료',         8, 3),
                                                                                 (11,'2024-09-29 10:40:00', '90일 전 안내, 방문상담 예정',          9, 1),
                                                                                 (12,'2024-10-29 10:40:00', '60일 전 안내, 일정 확정',             9, 1);
/* [파트 5] 업무 로그, 알림 및 정산
   - 방문 일정 (FK: beneficiary, care_worker, m_service_type)
   - 요양 일지 (FK: visit_schedule, employee, beneficiary) *직원ID 14~23 사용
   - 상담 일지 (FK: employee, beneficiary)
   - 알림/AI (FK: employee, beneficiary)
   - 전자결재 (FK: employee)
   - 청구서/영업상태 (FK: beneficiary, potential_customer)
*/

-- -----------------------------------------------------------
-- 1. 방문 일정 (Visit Schedule)
-- -----------------------------------------------------------
-- care_worker_id: 1~10 (각각 employee_id 14~23에 해당)
INSERT INTO visit_schedule
(start_dt, end_dt, visit_status, rfid_start_time, rfid_end_time, is_log_written, note, service_type_id, care_worker_id, beneficiary_id)
VALUES
    ('2025-12-03 10:00:00', '2025-12-03 11:00:00', 'DONE',        '2025-12-03 10:00:00', '2025-12-03 11:00:00', 1, NULL,        2, 1, 5),
    ('2025-12-03 15:00:00', '2025-12-03 17:00:00', 'DONE',        '2025-12-03 15:00:00', '2025-12-03 17:00:00', 1, NULL,        1, 2, 6),

    ('2025-12-04 09:00:00', '2025-12-04 11:00:00', 'DONE',        '2025-12-04 09:00:00', '2025-12-04 11:00:00', 1, '욕실 점검', 3, 3, 7),
    ('2025-12-04 13:00:00', '2025-12-04 14:00:00', 'DONE',        '2025-12-04 13:00:00', '2025-12-04 14:00:00', 1, NULL,        2, 4, 8),

    ('2025-12-05 09:00:00', '2025-12-05 12:00:00', 'DONE',        '2025-12-05 09:00:00', '2025-12-05 12:00:00', 1, NULL,        1, 5, 9),
    ('2025-12-05 14:00:00', '2025-12-05 15:00:00', 'DONE',        '2025-12-05 14:00:00', '2025-12-05 15:00:00', 1, '혈압 측정', 2, 1, 10),

    ('2025-12-16 09:00:00', '2025-12-16 11:00:00', 'DONE',       '2025-12-16 09:00:00', '2025-12-16 11:00:00', 1, NULL,        1, 1, 1),
    ('2025-12-16 14:00:00', '2025-12-16 16:00:00', 'DONE',       '2025-12-16 14:00:00', '2025-12-16 16:00:00', 1, '혈압 측정', 2, 1, 2),

    ('2025-12-17 10:00:00', '2025-12-17 12:00:00', 'DONE',       '2025-12-17 10:00:00', '2025-12-17 12:00:00', 1, NULL,        1, 1, 3),
    ('2025-12-17 15:00:00', '2025-12-17 17:00:00', 'DONE',       '2025-12-17 15:00:00', '2025-12-17 17:00:00', 1, NULL,        2, 1, 4),

    ('2025-12-18 09:00:00', '2025-12-18 11:00:00', 'DONE',       '2025-12-18 09:00:00', '2025-12-18 11:00:00', 1, NULL,        1, 1, 5),
    ('2025-12-18 13:00:00', '2025-12-18 15:00:00', 'DONE',       '2025-12-18 13:00:00', '2025-12-18 15:00:00', 1, '목욕 지원', 3, 1, 6),

    ('2025-12-19 10:00:00', '2025-12-19 13:00:00', 'DONE',       '2025-12-19 10:00:00', '2025-12-19 13:00:00', 1, NULL,        1, 1, 7),
    ('2025-12-19 14:00:00', '2025-12-19 16:00:00', 'DONE',       '2025-12-19 14:00:00', '2025-12-19 16:00:00', 1, NULL,        2, 1, 8),

    ('2025-12-20 09:00:00', '2025-12-20 11:00:00', 'DONE',       '2025-12-20 09:00:00', '2025-12-20 11:00:00', 1, NULL,        1, 1, 9),
    ('2025-12-20 15:00:00', '2025-12-20 18:00:00', 'DONE',       '2025-12-20 15:00:00', '2025-12-20 18:00:00', 1, NULL,        1, 1, 10),

    ('2025-12-22 09:00:00', '2025-12-22 11:00:00', 'IN_PROGRESS', NULL, NULL, 0, NULL, 1, 1, 1),
    ('2025-12-22 14:00:00', '2025-12-22 16:00:00', 'IN_PROGRESS', NULL, NULL, 0, '정기 방문', 2, 1, 2),
    ('2025-12-22 17:00:00', '2025-12-22 18:00:00', 'SCHEDULED', NULL, NULL, 0, NULL, 1, 1, 3),

    ('2025-12-23 14:00:00', '2025-12-23 16:00:00', 'SCHEDULED', NULL, NULL, 0, '정기 방문', 2, 1, 2),
    ('2025-12-23 17:00:00', '2025-12-23 18:00:00', 'SCHEDULED', NULL, NULL, 0, NULL, 1, 1, 3);



-- 개인 일정 샘플 데이터
-- care_worker_id = 1 (테스트용)
INSERT INTO personal_schedule (care_worker_id, start_dt, end_dt, schedule_status, title, personal_type_id, custom_type, location, notes)
VALUES
    -- 이번 주 개인 일정
    (1, CURRENT_DATE + INTERVAL 12 HOUR, CURRENT_DATE + INTERVAL 13 HOUR, 'SCHEDULED', '점심 약속', 1, NULL, '강남역 근처 식당', '팀 미팅 겸 점심'),
    (1, CURRENT_DATE + INTERVAL 1 DAY + INTERVAL 10 HOUR, CURRENT_DATE + INTERVAL 1 DAY + INTERVAL 11 HOUR, 'SCHEDULED', '병원 방문', 4, NULL, '서울대병원', '정기검진'),
    (1, CURRENT_DATE + INTERVAL 2 DAY + INTERVAL 14 HOUR, CURRENT_DATE + INTERVAL 2 DAY + INTERVAL 16 HOUR, 'SCHEDULED', '교육 연수', 5, NULL, '요양보호사 교육센터', '직무 교육 참석'),
    (1, CURRENT_DATE + INTERVAL 3 DAY + INTERVAL 18 HOUR, CURRENT_DATE + INTERVAL 3 DAY + INTERVAL 19 HOUR, 'SCHEDULED', '저녁 약속', 8, '가족 모임', '홍대 카페', '친구 만남'),
    (1, CURRENT_DATE - INTERVAL 1 DAY + INTERVAL 13 HOUR, CURRENT_DATE - INTERVAL 1 DAY + INTERVAL 14 HOUR, 'DONE', '월간 회의', 6, NULL, '본사 회의실', '월간 회의'),

    -- 다음 주 개인 일정
    (1, CURRENT_DATE + INTERVAL 7 DAY + INTERVAL 9 HOUR, CURRENT_DATE + INTERVAL 7 DAY + INTERVAL 10 HOUR, 'SCHEDULED', '운동', 2, NULL, '헬스장', '아침 운동'),
    (1, CURRENT_DATE + INTERVAL 8 DAY + INTERVAL 15 HOUR, CURRENT_DATE + INTERVAL 8 DAY + INTERVAL 16 HOUR + INTERVAL 30 MINUTE, 'SCHEDULED', '슈퍼비전', 5, NULL, '센터장실', '사례 슈퍼비전'),

    -- 다른 요양 보호사
    (2, CURRENT_DATE + INTERVAL 11 HOUR + INTERVAL 30 MINUTE, CURRENT_DATE + INTERVAL 12 HOUR + INTERVAL 30 MINUTE, 'SCHEDULED', '점심시간', 1, NULL, NULL, NULL),
    (3, CURRENT_DATE + INTERVAL 1 DAY + INTERVAL 16 HOUR, CURRENT_DATE + INTERVAL 1 DAY + INTERVAL 17 HOUR, 'SCHEDULED', '개인 상담', 7, NULL, '상담실', '심리 상담');

-- -----------------------------------------------------------
-- 2. 요양 일지 (Care Logs)
-- -----------------------------------------------------------
-- [중요] employee_id는 요양보호사의 실제 직원 ID(14~23)를 사용해야 합니다.
-- vs_id는 위에서 생성된 1~10번 visit_schedule을 참조합니다.
INSERT INTO care_logs (
    vs_id, service_date, start_time, end_time, service_type,
    updated_at, is_deleted,
    is_breakfast, is_lunch, is_dinner, is_snack,
    diaper_count, toilet_count, is_portable_toilet, is_urine, is_stool,
    stool_normal, stool_diarrhea, stool_constipation,
    is_face_wash, is_oral_care, is_hair_wash, is_body_wash, is_change_clothes,
    is_meal_prep, is_bed_care, is_position_change, is_get_up_help,
    is_indoor_move, is_walk_help,
    is_emotional_talk, is_communication, is_counseling,
    is_cognitive_care, is_behavior_care,
    is_health_good, is_pain, is_edema, is_skin_issue, is_body_etc,
    is_mood_calm, is_mood_anxious, is_mood_depressed, is_mood_angry, is_mood_etc,
    is_excretion_mistake, is_sleep_lack, is_nap_excess,
    special_note, care_worker_id, beneficiary_id
) VALUES
-- 1번 요양일지 (수급자 1, 직원 14)
(1,CURDATE(),'09:00','10:00','방문요양',NOW(),0,
 1,0,0,0, 1,2, 0,1,1, 1,0,0,
 1,1,0,0,1, 1,1,1,1, 1,1,
 1,1,0, 0,0,
 1,0,0,0,0, 1,0,0,0,0, 0,0,0,
 '전반적으로 안정적', 1, 1),

-- 2번 요양일지 (수급자 2, 직원 15)
(2,CURDATE(),'09:00','10:00','방문간호',NOW(),0,
 0,1,0,0, 0,1, 0,1,1, 1,0,0,
 0,1,0,0,1, 0,0,0,0, 0,0,
 0,1,1, 0,0,
 0,1,0,0,0, 0,1,0,0,0, 0,0,0,
 '무릎 통증 호소', 1, 2),

-- 3번 요양일지 (수급자 3, 직원 16)
(3,CURDATE(),'10:00','11:00','방문목욕',NOW(),0,
 0,0,0,0, 0,0, 0,0,0, 0,0,0,
 1,1,1,1,1, 0,0,0,0, 0,0,
 1,1,0, 0,0,
 1,0,0,0,0, 1,0,0,0,0, 0,0,0,
 '목욕 후 컨디션 양호', 1, 3),

-- 4번 (수급자 4, 직원 17)
(4,CURDATE(),'13:00','14:00','방문요양',NOW(),0,
 0,1,0,0, 1,1, 1,1,1, 0,0,0,
 1,0,0,0,1, 1,1,1,1, 1,1,
 1,1,0, 0,0,
 1,0,0,0,0, 1,0,0,0,0, 0,0,0,
 NULL, 1, 4),

-- 5번 (수급자 5, 직원 18)
(5,CURDATE(),'09:00','10:00','방문간호',NOW(),0,
 0,0,0,0, 0,1, 0,1,0, 1,0,0,
 0,1,0,0,0, 0,0,0,0, 0,0,
 0,1,1, 0,0,
 0,1,0,0,0, 0,1,0,0,0, 0,0,0,
 '혈압 측정 후 안정', 1, 5),

-- 6번 (수급자 6, 직원 19)
(6,'2025-12-13','10:00','11:00','방문요양',NOW(),0,
 0,0,1,0, 1,2, 0,1,1, 1,0,0,
 0,0,0,0,1, 1,1,1,1, 1,1,
 1,1,0, 0,0,
 1,0,0,0,0, 1,0,0,0,0, 0,1,0,
 '수면 부족 호소', 1, 6),

-- 7번 (수급자 7, 직원 20)
(7,'2025-12-14','14:00','15:00','방문목욕',NOW(),0,
 0,0,0,0, 0,0, 0,0,0, 0,0,0,
 1,1,1,1,1, 0,0,0,0, 0,0,
 1,0,0, 0,0,
 1,0,0,0,0, 1,0,0,0,0, 0,0,0,
 '욕실 미끄럼 주의', 1, 7),

-- 8번 (수급자 8, 직원 21)
(8,'2025-12-14','09:00','10:00','방문요양',NOW(),0,
 1,0,0,1, 0,1, 0,1,1, 1,0,0,
 1,1,0,0,0, 1,1,0,0, 1,0,
 1,1,0, 1,0,
 1,0,0,0,0, 1,0,0,0,0, 0,0,0,
 '정서 대화 위주', 1, 8),

-- 9번 (수급자 9, 직원 22)
(9,'2025-12-15','15:00','16:00','방문간호',NOW(),0,
 0,0,0,0, 0,0, 0,1,0, 0,0,0,
 0,1,0,0,0, 0,0,0,0, 0,0,
 0,1,1, 0,0,
 0,1,0,0,0, 0,1,0,0,0, 0,0,0,
 '부종 관찰 필요', 1, 9),

-- 10번 (수급자 10, 직원 23)
(10,'2025-12-15','10:00','11:00','방문요양',NOW(),0,
 0,1,0,0, 1,1, 0,1,1, 0,0,0,
 1,0,0,0,1, 1,1,0,1, 1,1,
 1,1,0, 1,0,
 1,0,0,0,0, 1,0,0,0,0, 1,0,0,
 '배뇨 실수 1회', 1, 10);

-- -----------------------------------------------------------
-- 3. 상담 일지 (Counseling Logs)
-- -----------------------------------------------------------
-- id2(employee): 요양보호사(14~) 또는 사회복지사(1~)
INSERT INTO counseling_logs (
    counseling_date, counseling_type, satisfaction,
    visit_purpose, attendees, discussion_content, agreement_content,
    next_visit_date, counselor_sign_url, guardian_sign_url,
    created_at, care_worker_id, beneficiary_id
) VALUES
      (CURRENT_DATE,'초기상담','매우만족', '서비스 시작','수급자,보호자', '건강상태 파악', '주3회 방문 합의', '2024-02-05', 's1.png','g1.png', NOW(), 1, 1),
      (CURRENT_DATE,'정기상담','만족',     '점검',        '보호자',       '이동 어려움',   '이동보조 강화',   '2024-03-10', 's2.png','g2.png', NOW(), 1, 2),
      ('2025-12-15','보호자상담','보통',   '요청',        '보호자',       '식사량 감소',   '지속 소통',       '2024-04-01', 's3.png','g3.png', NOW(), 1, 3),
      ('2025-12-14','정기상담','만족',     '지속확인',    '수급자',       '정서 안정',     '서비스 유지',     '2024-04-20', 's4.png','g4.png', NOW(), 1, 4),
      ('2025-12-13','긴급상담','불만족',   '낙상사고',    '수급자,보호자', '욕실 미끄러짐', '매트 설치',       NULL,         's5.png','g5.png', NOW(), 1, 5);

-- -----------------------------------------------------------
-- 4. 알림 및 AI Care (Notification & AI)
-- -----------------------------------------------------------
-- 알림 템플릿/규칙/로그
INSERT INTO notification_template (title, content, template_type, created_at, is_active, created_by, target_type_id) VALUES
                                                                                                                         ('자격증 만료 예정 안내', '귀하의 자격증이 만료됩니다.', 'CERT_EXPIRE', CURDATE(), 1, 1, 1),
                                                                                                                         ('긴급: 방문 일정 변경', '오늘 오후 3시 방문 일정이 오후 5시로 변경되었습니다.', 'SCHEDULE_CHANGE', CURDATE(), 1, 1, 3),
                                                                                                                         ('긴급: 수급자 상태 확인 요청', '김영희 수급자님의 건강 상태 확인이 필요합니다.', 'HEALTH_CHECK', CURDATE(), 1, 1, 3),
                                                                                                                         ('긴급: 요양일지 미작성', '어제 방문한 요양일지가 아직 작성되지 않았습니다.', 'CARE_LOG_REMINDER', CURDATE(), 1, 1, 3);

INSERT INTO notification_rule (template_id, offset_days, is_active, created_at, created_by, target_type) VALUES
                                                                                                             (1, 7, 1, CURDATE(), 1, 'EMPLOYEE'),
                                                                                                             (2, 0, 1, CURDATE(), 1, 'EMPLOYEE'),
                                                                                                             (3, 0, 1, CURDATE(), 1, 'EMPLOYEE'),
                                                                                                             (4, 0, 1, CURDATE(), 1, 'EMPLOYEE');

INSERT INTO notification_log (rule_id, template_id, receiver_id, receiver_type, sent_at, status, read_at, channel_type_id) VALUES
                                                                                                                               (1, 1, 14, 'EMPLOYEE', CURDATE(), 'SENT', NULL, 1), -- 요양보호사 14번에게 전송
                                                                                                                               (2, 2, 15, 'EMPLOYEE', CURDATE(), 'READ', CURDATE(), 1),
                                                                                                                               -- care_worker_id = 1 (employee_id = 14)에게 추가 알림
                                                                                                                               (1, 1, 14, 'EMPLOYEE', CURDATE() - INTERVAL 1 DAY, 'SENT', NULL, 1),
                                                                                                                               (2, 2, 14, 'EMPLOYEE', CURDATE() - INTERVAL 2 DAY, 'SENT', NULL, 2),
                                                                                                                               (1, 1, 14, 'EMPLOYEE', CURDATE() - INTERVAL 3 DAY, 'FAILED', NULL, 1),
                                                                                                                               (2, 2, 14, 'EMPLOYEE', CURDATE(), 'SENT', NULL, 3),
                                                                                                                               -- 긴급 알림 추가 (employee_id = 14)
                                                                                                                               (3, 3, 14, 'EMPLOYEE', CURDATE(), 'SENT', NULL, 1),
                                                                                                                               (4, 4, 14, 'EMPLOYEE', CURDATE(), 'SENT', NULL, 1);

-- AI Care (FK 없음, id는 beneficiary_id와 논리적 연결)
INSERT INTO ai_care (ai_id, ai_content, ai_month, ai_create_at, beneficiary_id) VALUES
                                                                                    ('REQ-202504-001', '4월 건강 상태 양호, 산책 활동 증가함', '2025-04-01', NOW(), 1),
                                                                                    ('REQ-202504-002', '수면 패턴 불규칙, 야간 케어 필요', '2025-04-01', NOW(), 2);

-- -----------------------------------------------------------
-- 5. 전자결재 및 청구서 (Payment & Invoice)
-- -----------------------------------------------------------
-- 전자결재 (Drafter: Emp 1, Approver: Emp 2)
INSERT INTO electronic_payment (number, approve, employee_id, electronic_payment_category_id)
VALUES
    ('DOC-001', '1', 1, 1), -- 1: 급여
    ('DOC-002', '1', 2, 2), -- 2: 구매
    ('DOC-003', '0', 3, 3), -- 3: 휴가
    ('DOC-004', '1', 4, 4); -- 4: 기타

INSERT INTO electronic_payment_process (approve, drafter_id, approver_id, electronic_payment_id) VALUES
                                                                                                     ('1', 1, 2, 1), ('1', 2, 3, 2), ('0', 3, 4, 3), ('1', 4, 5, 4);

-- 청구서 (Invoice)
INSERT INTO invoice (beneficiary_id, bill, period_start, period_end, is_sent, total_count, own_count, payment_status, billing_month) VALUES
                                                                                                                                         (1, '2025년 1월 청구서', '2025-01-01', '2025-01-31', '전송', 20, 4, '미납', '2025-01'),
                                                                                                                                         (2, '2025년 1월 청구서', '2025-01-01', '2025-01-31', '전송', 18, 3, '완납', '2025-01'),
                                                                                                                                         (3, '2025년 1월 청구서', '2025-01-01', '2025-01-31', '지불됨', 16, 2, '완납', '2025-01'),
                                                                                                                                         (4, '2025년 1월 청구서', '2025-01-01', '2025-01-31', '초안', 15, 3, '미납', '2025-01');

-- -----------------------------------------------------------
-- 6. 영업/상담 이력 (CRM) - 테스트 데이터를 위한 날짜 다양화
-- -----------------------------------------------------------

-- 1) 잠재고객 단계 이력 (Potential Stage)
-- ID 1: 1단계 진행 중
-- ID 2: 2단계 진행 중
-- ID 3: 이탈 (가입 의사 없음)
INSERT INTO potential_stage
(stage, process_status, process_time, month, html_code, potential_customer_id)
VALUES
    (1, 'P', NOW(), NOW(), '<div class="step1">전화상담 완료</div>', 1),
    (2, 'P', NOW(), NOW(), '<div class="step2">서류 접수중</div>', 2),
    (1, 'F', DATE_SUB(NOW(), INTERVAL 10 DAY), NOW(), '<div class="step1 error">실패</div>', 3);
-- 2) 상담 이력 (Counsel History)
-- [테스트 시나리오]
-- 1. 잠재고객 1 (김철수): 오늘 상담함 -> '잠재고객' (1단계, 3일 이내)
-- 2. 잠재고객 2 (이영희): 오늘 상담함 -> '잠재고객' (2단계, 60일 이내)
-- 3. 잠재고객 3 (박민수): 10일 전 상담함 -> 1단계인데 3일 지남 -> '이탈고객'
-- 4. 수급자 1 (김영수): 오늘 상담함 -> '기존고객'
-- 5. 수급자 2 (이정자): 40일 전 상담함 -> 30일 지남 -> '이탈고객' (리스트 조회 시 빨간색 표시 확인용)
INSERT INTO counsel_history
(consult_date, summary, detail, guardian_st, follow_up, follow_up_necessary, churn, churn_reason, m_reservation_channel_id, potential_id, beneficiary_id, counsel_category_id, counselor_id)
VALUES
    (NOW(), '정기안부', '건강 상태 및 식사 여부 확인, 양호함', 0, NULL, 'N', 'N', NULL, 1, NULL, 1, 3, 9),
    (DATE_SUB(NOW(), INTERVAL 7 DAY), '불편사항', '야간에 기침이 잦다고 함', 1, '3일 뒤 상태 재확인 전화 예정', 'Y', 'N', NULL, 1, NULL, 1, 3, 10),
    (DATE_SUB(NOW(), INTERVAL 14 DAY), '방문일정', '요양보호사 방문 시간 조정 요청', 1, NULL, 'N', 'N', NULL, 1, NULL, 1, 3, 11),
    (DATE_SUB(NOW(), INTERVAL 1 MONTH), '정기상담', '서비스 만족도 조사: 매우 만족', 0, NULL, 'N', 'N', NULL, 2, NULL, 1, 3, 12),
    (DATE_SUB(NOW(), INTERVAL 2 MONTH), '급여문의', '본인부담금 납부 계좌 변경 문의', 1, NULL, 'N', 'N', NULL, 1, NULL, 1, 3, 13),
    (DATE_SUB(NOW(), INTERVAL 3 MONTH), '건강상담', '환절기 감기 기운 체크', 0, NULL, 'N', 'N', NULL, 1, NULL, 1, 3, 9),
    (DATE_SUB(NOW(), INTERVAL 4 MONTH), '욕구사정', '정기 욕구 사정 진행', 0, '사정 결과 반영하여 급여제공계획 수정', 'Y', 'N', NULL, 2, NULL, 1, 3, 10),
    (DATE_SUB(NOW(), INTERVAL 5 MONTH), '명절인사', '추석 명절 안부 인사', 0, NULL, 'N', 'N', NULL, 1, NULL, 1, 3, 11),
    (DATE_SUB(NOW(), INTERVAL 6 MONTH), '인지활동', '치매 예방 퍼즐 활동 지원 확인', 0, NULL, 'N', 'N', NULL, 2, NULL, 1, 3, 12),
    (DATE_SUB(NOW(), INTERVAL 8 MONTH), '가족상담', '보호자(아들)와 향후 케어 플랜 논의', 1, '보호자 요청사항 센터장 보고 완료', 'Y', 'N', NULL, 1, NULL, 1, 3, 13),
    (DATE_SUB(NOW(), INTERVAL 10 MONTH), '계약갱신', '등급 갱신 관련 서류 안내', 1, NULL, 'N', 'N', NULL, 1, NULL, 1, 1, 9),
    (DATE_SUB(NOW(), INTERVAL 12 MONTH), '초기상담', '서비스 개시 및 초기 적응 확인', 0, '초기 적응 모니터링 1주일간 진행', 'Y', 'N', NULL, 2, NULL, 1, 1, 10),
    (NOW(), '가입문의', '등급 신청 절차 및 필요 서류 상세 안내', 1, '등급 판정 결과 나오면 연락 주기로 함', 'Y', 'N', NULL, 1, 1, NULL, 1, 11),
    (DATE_SUB(NOW(), INTERVAL 2 DAY), '렌탈문의', '전동침대 대여 모델 및 비용 문의', 0, '재고 확인 후 내일 오전 안내 예정', 'Y', 'N', NULL, 1, 2, NULL, 2, 12),
    (DATE_SUB(NOW(), INTERVAL 65 DAY), '단순문의', '요양원과 재가요양 차이점 문의', 0, NULL, 'N', 'Y', '타 기관(요양원) 입소 결정', 1, 3, NULL, 3, 13),
    (DATE_SUB(NOW(), INTERVAL 90 DAY), '부재중', '전화 연결 안됨, 문자 남김', 0, '오후 5시 이후 재통화 시도', 'Y', 'Y', '장기 연락 두절 및 서비스 의사 없음', 1, 4, NULL, 1, 9),
    (NOW(), '방문상담', '센터 내방하여 시설 확인 및 계약서 검토', 1, '내일 오전 10시 계약 체결 예정', 'Y', 'N', NULL, 2, 5, NULL, 1, 10),
    (NOW(), '정기점검', '서비스 이용 불편 사항 확인, 없음', 0, NULL, 'N', 'N', NULL, 1, NULL, 2, 3, 11),
    (DATE_SUB(NOW(), INTERVAL 3 DAY), '물품요청', '기저귀 추가 구매 요청', 1, '금일 오후 배송 출발 확인', 'Y', 'N', NULL, 1, NULL, 6, 2, 12),
    (DATE_SUB(NOW(), INTERVAL 30 DAY), '해지문의', '서비스 불만족으로 인한 해지 절차 문의', 1, '센터장님 면담 일정 조율 중', 'Y', 'Y', '요양보호사 교체 요구 불수용에 따른 불만', 1, NULL, 5, 4, 13),
    (DATE_SUB(NOW(), INTERVAL 100 DAY), '만료안내', '계약 만료 예정 안내 및 연장 의사 타진', 1, '연장 의사 재확인 필요', 'Y', 'Y', '건강 호전으로 인한 등급 탈락 및 만료', 1, NULL, 3, 1, 9),
    (DATE_SUB(NOW(), INTERVAL 120 DAY), '안부전화', '장기 입원으로 서비스 중단 상태 확인', 1, NULL, 'N', 'Y', '요양병원 장기 입원으로 인한 계약 해지', 1, NULL, 20, 3, 10),
    (DATE_SUB(NOW(), INTERVAL 5 DAY), '직원칭찬', '요양보호사가 매우 친절하다고 칭찬함', 0, NULL, 'N', 'N', NULL, 1, NULL, 7, 3, 11),
    (DATE_SUB(NOW(), INTERVAL 10 DAY), '일정변경', '병원 진료로 인한 방문 시간 변경', 1, '변경된 시간에 맞춰 방문 요양사에게 전달 완료', 'Y', 'N', NULL, 1, NULL, 8, 3, 12),
    (DATE_SUB(NOW(), INTERVAL 15 DAY), '단순문의', '복지용구 휠체어 타이어 교체 문의', 0, 'AS 업체 접수 대행', 'Y', 'N', NULL, 1, NULL, 9, 2, 13),
    (DATE_SUB(NOW(), INTERVAL 20 DAY), '건강악화', '최근 기력 저하로 식사 도움 필요성 증가', 1, '방문간호 서비스 추가 건의', 'Y', 'N', NULL, 2, NULL, 10, 3, 9),
    (DATE_SUB(NOW(), INTERVAL 1 DAY), '가입문의', '지인 소개로 연락함', 0, '가입 필요 서류 팩스 발송 요청', 'Y', 'N', NULL, 1, 6, NULL, 1, 10),
    (DATE_SUB(NOW(), INTERVAL 8 DAY), '등급상담', '등급 상향 조정 가능 여부 문의', 1, '공단에 등급 변경 신청 절차 확인 후 안내', 'Y', 'N', NULL, 1, 7, NULL, 1, 11),
    (DATE_SUB(NOW(), INTERVAL 45 DAY), '불만접수', '목욕 서비스 시간 미준수 컴플레인', 1, '담당 요양보호사 교육 및 주의 조치', 'Y', 'N', NULL, 1, NULL, 11, 4, 12),
    (DATE_SUB(NOW(), INTERVAL 60 DAY), '안부', '특별한 용건 없이 안부 전화', 0, NULL, 'N', 'N', NULL, 1, NULL, 12, 3, 13);

-- ========================================================
-- [중요] 데이터 동기화 (Data Synchronization)
-- 상담 이력(counsel_history)과 단계 이력(potential_stage)을 기반으로
-- 고객 테이블(beneficiary, potential_customer)의 최신 정보를 업데이트합니다.
-- 이 쿼리는 모든 INSERT가 끝난 후 마지막에 실행되어야 합니다.
-- ========================================================

-- 1. 수급자(Beneficiary)의 last_counsel_date 동기화
UPDATE beneficiary b
    JOIN (
        SELECT beneficiary_id, MAX(consult_date) as max_date
        FROM counsel_history
        WHERE beneficiary_id IS NOT NULL
        GROUP BY beneficiary_id
    ) ch ON b.id = ch.beneficiary_id
SET b.last_counsel_date = ch.max_date
WHERE b.id > 0;

-- 2. 잠재고객(Potential Customer)의 last_counsel_date 동기화
UPDATE potential_customer p
    JOIN (
        SELECT potential_id, MAX(consult_date) as max_date
        FROM counsel_history
        WHERE potential_id IS NOT NULL
        GROUP BY potential_id
    ) ch ON p.id = ch.potential_id
SET p.last_counsel_date = ch.max_date
WHERE p.id > 0;

-- 3. 잠재고객(Potential Customer)의 current_stage 동기화
-- potential_stage 테이블에서 가장 높은 단계를 가져와서 설정
UPDATE potential_customer p
    JOIN (
        SELECT potential_customer_id, MAX(stage) as max_stage
        FROM potential_stage
        GROUP BY potential_customer_id
    ) ps ON p.id = ps.potential_customer_id
SET p.current_stage = ps.max_stage
WHERE p.id > 0;

-- 4. current_stage가 NULL인 잠재고객 기본값(1) 처리
UPDATE potential_customer
SET current_stage = 1
WHERE current_stage IS NULL
  AND id > 0;

/* [파트 6] 평가 템플릿 및 결과 데이터 (JSON 포함)
   - 평가 템플릿 (eval_templates): 낙상, 욕창, 인지, 욕구사정 양식 정의
   - 기초 평가 결과 (basic_evaluations): 수급자별 평가 결과 (JSON 저장)
   - FK 참조: beneficiary(id2), employee(id3)
*/

-- -----------------------------------------------------------
-- 1. 평가 템플릿 (Evaluation Templates)
-- -----------------------------------------------------------

-- -----------------------------------------------------------
-- 1. 평가 서식 (Evaluation Templates)
-- -----------------------------------------------------------
INSERT INTO eval_templates (template_id, eval_type, version, name, description, template_json, total_score, is_active) VALUES
-- 1-1. 낙상위험도 평가 (FALL)
(1, 'FALL', 1, '낙상위험도 평가지', '연 1회 낙상위험도 평가 (연령, 정신상태, 낙상병력, 활동, 보행, 균형, 약물/질병 변화 포함)',
 '{
   "title": "낙상위험도 평가",
   "code": "FALL",
   "columns": [
     { "score": 4, "label": "4점" }, { "score": 3, "label": "3점" },
     { "score": 2, "label": "2점" }, { "score": 1, "label": "1점" }, { "score": 0, "label": "0점" }
   ],
   "items": [
     {
       "code": "age", "label": "연령",
       "choices": [ { "score": 3, "label": "80세 이상" }, { "score": 2, "label": "70~79세" }, { "score": 1, "label": "60~69세" }, { "score": 0, "label": "60세 미만" } ]
     },
     {
       "code": "mental_status", "label": "정신상태",
       "choices": [ { "score": 4, "label": "혼란스러움/정신장애" }, { "score": 2, "label": "때때로 혼란스러움/생활환경 장애" }, { "score": 0, "label": "해당없음" } ]
     },
     {
       "code": "stool", "label": "배변",
       "choices": [ { "score": 4, "label": "소변, 대변 실금" }, { "score": 3, "label": "조절능력 있지만 도움필요" }, { "score": 1, "label": "유치도뇨관/인공항문" }, { "score": 0, "label": "해당없음" } ]
     },
     {
       "code": "fall_history", "label": "낙상경험",
       "choices": [ { "score": 4, "label": "이미 세 번 이상 넘어짐" }, { "score": 2, "label": "이미 한 번 또는 두 번 넘어짐" }, { "score": 0, "label": "해당없음" } ]
     },
     {
       "code": "activity", "label": "활동",
       "choices": [ { "score": 4, "label": "전적으로 도움을 받음" }, { "score": 3, "label": "자리에서 일어나 앉기 도움" }, { "score": 1, "label": "자립/세면대,화장실이동" }, { "score": 0, "label": "완전 자립" } ]
     },
     {
       "code": "balance", "label": "걸음걸이 및 균형",
       "choices": [ { "score": 4, "label": "평균적/불안정서 있을 때와 걸을 때 균형을 거의 유지하지 못함" }, { "score": 3, "label": "일어서거나, 걸을 때 기립성 반영/혈압조절 문제" }, { "score": 2, "label": "보행장애/보조도구나 도움으로 걷기" }, { "score": 0, "label": "해당없음" } ]
     },
     {
       "code": "recent", "label": "지난 7일간 약복용이나 계획된 약물",
       "choices": [ { "score": 4, "label": "3개 또는 그 이상의 약 복용" }, { "score": 3, "label": "두 가지 약 복용" }, { "score": 2, "label": "한 가지 약 복용" }, { "score": 0, "label": "해당없음" } ]
     }
   ],
   "grading": {
     "ranges": [
       { "min": 0,  "max": 4,  "label": "낙상 위험 낮음" },
       { "min": 5,  "max": 10, "label": "낙상 위험 높음" },
       { "min": 11, "max": 999, "label": "낙상 위험 매우 높음" }
     ],
     "comment_field": true
   }
 }', 28, 1),

-- 1-2. 욕창위험도 평가 (BEDSORE)
(2, 'BEDSORE', 1, '욕창위험도 평가지 (Braden)', '연 1회 욕창위험도 평가 (감각인지, 습기, 활동성, 움직임, 영양상태, 마찰력/전단력)',
 '{
   "title": "욕창위험도 평가",
   "code": "BEDSORE",
   "columns": [ { "score": 1, "label": "1점" }, { "score": 2, "label": "2점" }, { "score": 3, "label": "3점" }, { "score": 4, "label": "4점" } ],
   "items": [
     { "code": "sensory", "label": "감각인지", "choices": [ { "score": 1, "label": "완전 제한" }, { "score": 2, "label": "매우 제한" }, { "score": 3, "label": "약간 제한" }, { "score": 4, "label": "손상 없음" } ] },
     { "code": "moisture", "label": "습기", "choices": [ { "score": 1, "label": "계속 축축함" }, { "score": 2, "label": "자주 축축함" }, { "score": 3, "label": "가끔 축축함" }, { "score": 4, "label": "거의 없음" } ] },
     { "code": "activity", "label": "활동성", "choices": [ { "score": 1, "label": "침상안정" }, { "score": 2, "label": "의자안정" }, { "score": 3, "label": "가끔 보행" }, { "score": 4, "label": "자주 보행" } ] },
     { "code": "mobility", "label": "움직임", "choices": [ { "score": 1, "label": "완전 부동" }, { "score": 2, "label": "매우 제한" }, { "score": 3, "label": "약간 제한" }, { "score": 4, "label": "제한 없음" } ] },
     { "code": "nutrition", "label": "영양상태", "choices": [ { "score": 1, "label": "매우 불량" }, { "score": 2, "label": "약간 불량" }, { "score": 3, "label": "양호" }, { "score": 4, "label": "매우 양호" } ] },
     { "code": "friction", "label": "마찰력과 전단력", "choices": [ { "score": 1, "label": "문제 있음" }, { "score": 2, "label": "잠재적 문제" }, { "score": 3, "label": "문제 없음" } ] }
   ],
   "grading": {
     "ranges": [
       { "min": 19, "max": 23, "label": "낮음(위험 없음)" },
       { "min": 13, "max": 18, "label": "중간(약간~중도 위험)" },
       { "min": 0, "max": 12, "label": "매우 높음(위험 매우 높음)" }
     ],
     "comment_field": true
   }
 }', 23, 1),

-- 1-3. 인지기능 평가 (COGNITIVE)
(3, 'COGNITIVE', 1, '인지기능 평가 (CIST)', '연 1회 인지기능 평가, 학력 보정 기준을 포함한 CIST 기반 평가',
 '{
   "title": "인지기능 평가",
   "code": "COGNITIVE",
   "max_score": 30,
   "education_levels": [
     { "code": "NO_EDU_READ", "label": "무학(글자 모름)", "cutoff": 14 },
     { "code": "NO_EDU", "label": "무학(글자 앎)", "cutoff": 18 },
     { "code": "ELEMENTARY", "label": "초등학교 졸업", "cutoff": 22 },
     { "code": "MIDDLE_HIGH", "label": "중·고등학교 졸업", "cutoff": 24 },
     { "code": "COLLEGE", "label": "대학교 졸업 이상", "cutoff": 26 }
   ],
   "sections": [
     {
       "code": "A", "title": "지남력 (Orientation)", "max_score": 5,
       "items": [
         { "code": "A1", "question": "올해는 몇 년입니까?", "score": 1 },
         { "code": "A2", "question": "지금은 몇 월입니까?", "score": 1 },
         { "code": "A3", "question": "오늘은 며칠입니까?", "score": 1 },
         { "code": "A4", "question": "오늘은 무슨 요일입니까?", "score": 1 },
         { "code": "A5", "question": "여기는 어디입니까?", "score": 1 }
       ]
     },
     {
       "code": "B", "title": "기억력 - 등록", "max_score": 0,
       "items": [ { "code": "B1", "instruction": "비행기, 연필, 소나무를 따라 말하게 한다", "note": "점수 없음" } ]
     },
     {
       "code": "C", "title": "주의력 (Attention)", "max_score": 3,
       "items": [
         { "code": "C1", "question": "숫자 바로 따라 말하기 (3자리)", "score": 1 },
         { "code": "C2", "question": "숫자 거꾸로 말하기 (3자리)", "score": 1 },
         { "code": "C3", "question": "숫자 거꾸로 말하기 (4자리)", "score": 1 }
       ]
     },
     {
       "code": "D", "title": "시공간 기능 (Visuospatial)", "max_score": 2,
       "items": [
         {
           "code": "D1", "question": "도형 따라 그리기",
           "choices": [ { "score": 2, "label": "두 도형 정확, 교차 정확" }, { "score": 1, "label": "형태 맞으나 교차 오류" }, { "score": 0, "label": "형태 다름" } ]
         }
       ]
     },
     {
       "code": "E", "title": "집행 기능 (Executive Function)", "max_score": 6,
       "items": [
         {
           "code": "E1", "question": "동물 이름 말하기 (1분)",
           "auto_score_rule": [ { "min": 0, "max": 5, "score": 0 }, { "min": 6, "max": 8, "score": 1 }, { "min": 9, "max": 11, "score": 2 }, { "min": 12, "max": 99, "score": 3 } ]
         },
         {
           "code": "E2", "question": "선로 잇기 검사",
           "choices": [ { "score": 3, "label": "실수 없이 시간 내 완료" }, { "score": 2, "label": "스스로 수정하여 완료" }, { "score": 1, "label": "시간 초과/도움 필요" }, { "score": 0, "label": "실패" } ]
         }
       ]
     },
     {
       "code": "F", "title": "기억력 - 회상 (Memory Recall)", "max_score": 10,
       "items": [
         { "code": "F1", "word": "비행기", "choices": [ { "score": 2, "label": "자발 회상" }, { "score": 1, "label": "힌트 회상" }, { "score": 0, "label": "실패" } ] },
         { "code": "F2", "word": "연필", "choices": [ { "score": 2, "label": "자발 회상" }, { "score": 1, "label": "힌트 회상" }, { "score": 0, "label": "실패" } ] },
         { "code": "F3", "word": "소나무", "choices": [ { "score": 2, "label": "자발 회상" }, { "score": 1, "label": "힌트 회상" }, { "score": 0, "label": "실패" } ] },
         { "convert_rule": { "base_total": 6, "converted_total": 10 } }
       ]
     },
     {
       "code": "G", "title": "언어 기능 (Language)", "max_score": 4,
       "items": [
         {
           "code": "G1", "question": "상황 파악 질문",
           "choices": [ { "score": 4, "label": "모두 정답" }, { "score": 3, "label": "부분 정답" }, { "score": 2, "label": "약간 이해" }, { "score": 1, "label": "이해 어려움" }, { "score": 0, "label": "전혀 이해 못함" } ]
         }
       ]
     }
   ],
   "grading": {
     "rule": "education_cutoff",
     "description": "선택된 학력 기준 점수 미만일 경우 인지저하 의심"
   }
 }', 30, 1),

-- 1-4. 욕구사정 평가 (NEEDS)
(4, 'NEEDS', 1, '욕구사정 평가', '신체상태, 질병, 인지·의사소통, 가족환경, 자원활용, 주관적 욕구 등 욕구사정 종합평가',
 '{
   "title": "욕구사정 평가",
   "code": "NEEDS",
   "sections": [
     {
       "code": "1", "title": "일반 상태",
       "items": [
         { "code": "weight", "label": "체중", "type": "number", "unit": "kg" },
         { "code": "height", "label": "키", "type": "number", "unit": "cm" },
         { "code": "nutrition_status", "label": "영양상태", "type": "radio", "choices": ["양호", "적당", "부족", "매우 나쁨"] },
         { "code": "diet_type", "label": "식사형태", "type": "checkbox", "choices": ["일반식", "다진식", "죽식", "미음", "유동식", "연하식", "기타"], "hasTextWhen": ["기타"] },
         { "code": "therapeutic_diet", "label": "치료식이", "type": "checkbox", "choices": ["당뇨식", "저염식", "고단백식", "체중조절식"] },
         { "code": "eating_problem", "label": "식사 시 문제점", "type": "checkbox", "choices": ["양호", "저작곤란", "소화불량", "오심·구토", "연하곤란", "기타"], "hasTextWhen": ["기타"] },
         { "code": "oral_status", "label": "구강상태", "type": "radio", "choices": ["양호", "청결불량", "치아 약함", "틀니", "전존 치아 없음", "기타"], "hasTextWhen": ["기타"] },
         { "code": "urination_status", "label": "배설 양상 - 소변상태", "type": "checkbox", "choices": ["양호", "요실금", "배뇨곤란", "기타"], "hasTextWhen": ["기타"] },
         { "code": "defecation_status", "label": "대변상태", "type": "checkbox", "choices": ["양호", "지속적인 설사", "변비", "기타"], "hasTextWhen": ["기타"] },
         { "code": "diaper_use", "label": "기저귀 여부", "type": "checkbox", "choices": ["기저귀"] }
       ]
     },
     {
       "code": "2", "title": "주요 질병상태",
       "items": [
         { "code": "past_history", "label": "과거 병력", "type": "text" },
         { "code": "current_diagnosis", "label": "현 진단명", "type": "text" },
         { "code": "chronic_disease", "label": "만성질환", "type": "checkbox", "choices": ["당뇨", "고혈압", "만성호흡기질환", "암", "기타"], "hasTextWhen": ["기타"] },
         { "code": "circulatory", "label": "순환기계", "type": "checkbox", "choices": ["뇌경색", "뇌출혈", "협심증", "심근경색증", "기타"], "hasTextWhen": ["기타"] },
         { "code": "neurologic", "label": "신경계", "type": "checkbox", "choices": ["치매", "파킨슨병", "간질", "기타"], "hasTextWhen": ["기타"] },
         { "code": "musculoskeletal", "label": "근골격계", "type": "checkbox", "choices": ["관절염", "요통, 좌골통", "골절 등 후유증", "기타"], "hasTextWhen": ["기타"] },
         { "code": "mental_behavior", "label": "정신·행동장애", "type": "checkbox", "choices": ["중풍", "우울증", "수면장애", "정신질환", "기타"], "hasTextWhen": ["기타"] },
         { "code": "respiratory", "label": "호흡기계", "type": "checkbox", "choices": ["호흡곤란", "결핵", "기타"], "hasTextWhen": ["기타"] },
         { "code": "renal", "label": "만성 신장질환", "type": "checkbox", "choices": ["만성신부전증", "보막투석", "혈액투석", "기타"], "hasTextWhen": ["기타"] },
         { "code": "other_disease", "label": "기타 질환", "type": "checkbox", "choices": ["알레르기", "기타"], "subItems": [ { "code": "allergy_food", "label": "식품", "type": "text", "showWhen": "알레르기" } ], "hasTextWhen": ["기타"] }
       ]
     },
     {
       "code": "3", "title": "신체 상태 (일상생활동작 수행능력)",
       "items": [
         { "code": "adl", "label": "ADL", "type": "table_radio", "rows": ["옷 벗고 입기", "세수하기", "양치질하기", "식사하기", "목욕하기", "체위변경", "일어나 앉기", "옮겨 앉기", "화장실 사용하기", "몸단장하기"], "columns": ["완전자립", "부분도움", "완전도움"] }
       ]
     },
     {
       "code": "4", "title": "인지 상태",
       "items": [
         { "code": "behavior", "label": "행동 증상", "type": "checkbox", "choices": ["망상 (남을 의심하거나, 위험을 느낌)", "환각 (헛것을 보거나 환청을 듣는다.)", "배회 (의미 없이 걷는다.)", "반복적인 행동 (의미 없는 행동을 한다.)", "부적절한 행동 (불결행위 및 숨기는 행동을 한다.)", "폭력적 행동 (주변인에게 폭력적인 행동을 보인다.)", "우울 (슬프거나 처져있고 때로는 운다.)", "불안 (서성이거나, 안절부절 못한다.)"] }
       ]
     },
     {
       "code": "5", "title": "의사소통",
       "items": [
         { "code": "hearing", "label": "청력상태", "type": "radio", "choices": ["정상(보청기 포함)", "가까운 곳 대화 가능", "큰소리만 들음", "소리에 거의 반응 없음", "들리는지 판단 불능"] },
         { "code": "communication", "label": "의사소통", "type": "radio", "choices": ["모두 이해·표현 가능", "대부분 이해하고 의사표현 가능", "가끔 이해하고 의사표현 가능", "거의 이해하지 못하고 의사전달 불가능"] },
         { "code": "speech", "label": "발음능력", "type": "radio", "choices": ["정확함", "옹얼거림", "간혹 왜곡", "전혀 안 됨"] }
       ]
     },
     {
       "code": "6", "title": "가족 및 환경상태",
       "items": [
         { "code": "living_with", "label": "동거인", "type": "checkbox", "choices": ["독거", "배우자", "부모", "자녀", "자부/사위", "손자녀", "친척", "친구/이웃", "기타"], "hasTextWhen": ["기타"] },
         { "code": "children", "label": "자녀수", "type": "compound", "fields": [ { "code": "has_children", "label": "유무", "type": "radio", "choices": ["무", "유"] }, { "code": "son_count", "label": "아들 수", "type": "number", "showWhen": { "has_children": "유" } }, { "code": "daughter_count", "label": "딸 수", "type": "number", "showWhen": { "has_children": "유" } } ] },
         { "code": "main_supporter", "label": "주수발자", "type": "radio", "choices": ["무", "유"] },
         { "code": "supporter_relation", "label": "관계", "type": "radio", "choices": ["배우자", "자녀", "자부", "사위", "형제자매", "친척", "기타"], "hasTextWhen": ["기타"], "showWhen": { "main_supporter": "유" } },
         { "code": "economic", "label": "경제 상태", "type": "radio", "choices": ["안정", "불안정", "연금생활", "기초생활수급", "의료급여"] },
         { "code": "care_burden", "label": "수발 부담", "type": "radio", "choices": ["전혀 부담되지 않음", "아주 가끔 부담됨", "가끔 부담됨", "자주 부담됨", "항상 부담됨"] },
         { "code": "housing", "label": "거주 환경", "type": "radio", "choices": ["아파트", "단독주택", "연립주택", "다세대주택", "기타"], "hasTextWhen": ["기타"] }
       ]
     },
     {
       "code": "7", "title": "자원 이용",
       "items": [
         { "code": "medical_institution", "label": "진료 병원", "type": "compound", "fields": [ { "code": "hospital_name", "label": "병원명(진료과)", "type": "text" }, { "code": "regular_visit", "label": "정기진료", "type": "radio", "choices": ["무", "유"] }, { "code": "hospital_phone", "label": "전화번호", "type": "text", "showWhen": { "regular_visit": "유" } } ] },
         { "code": "religion", "label": "종교활동", "type": "radio", "choices": ["천주교", "기독교", "불교", "기타"], "hasTextWhen": ["기타"] },
         { "code": "community_service", "label": "지역사회 자원", "type": "checkbox", "choices": ["노인맞춤돌봄서비스", "노인돌봄기본서비스", "노인돌봄종합서비스", "단기가사서비스", "독거노인사회관계활성화", "초기독거노인자립지원", "가사간병", "재가복지", "급식및도시락배달", "보건소사업", "개인간병인", "산업재해간병인", "치매안심센터", "복지관(다음희망)", "노인정", "이동서비스", "종교단체", "이미용", "주거개선사업", "기타"], "hasTextWhen": ["기타"] }
       ]
     },
     {
       "code": "8", "title": "주관적 욕구",
       "items": [ { "code": "subjective_need", "label": "수급자 또는 보호자가 희망하는 개별 욕구", "type": "textarea" } ]
     },
     {
       "code": "9", "title": "총평",
       "items": [ { "code": "summary", "label": "종합 소견", "type": "textarea" } ]
     },
     {
       "code": "10", "title": "간호평가",
       "items": [
         { "code": "respiration", "label": "호흡", "type": "checkbox", "choices": ["기관지 절개관 간호", "흡인", "산소요법", "기타"], "hasTextWhen": ["기타"] },
         { "code": "nutrition_nursing", "label": "영양", "type": "checkbox", "choices": ["경관영양", "기타"], "hasTextWhen": ["기타"] },
         { "code": "elimination_nursing", "label": "배설", "type": "checkbox", "choices": ["투석간호", "유치도뇨관", "단순 도뇨", "방광루", "요루", "장루간호"] },
         { "code": "wound", "label": "상처", "type": "checkbox", "choices": ["상처간호", "당뇨발간호", "기타"], "hasTextWhen": ["상처간호", "기타"], "textLabel": { "상처간호": "부위" } },
         { "code": "bedsore_stage", "label": "욕창 단계", "type": "radio", "choices": ["1단계", "2단계", "3단계", "4단계"] },
         { "code": "bedsore_site", "label": "욕창 부위", "type": "checkbox", "choices": ["머리", "등", "어깨", "팔꿈치", "엉덩이", "뒤꿈치", "기타"], "hasTextWhen": ["기타"] },
         { "code": "bedsore_prevention", "label": "욕창 방지 도구", "type": "text" },
         { "code": "pain_cancer_site", "label": "통증 - 암 발생 부위", "type": "checkbox", "choices": ["폐", "위", "대장", "간", "전립선", "유방", "담낭 및 기타담도", "기타"], "hasTextWhen": ["기타"] },
         { "code": "pain_general_site", "label": "일반 통증 부위", "type": "checkbox", "choices": ["머리", "상지", "하지", "허리", "등", "복부", "기타"], "hasTextWhen": ["기타"] }
       ]
     }
   ]
 }', NULL, 1);


-- -----------------------------------------------------------
-- 2. 기초 평가 결과 (Basic Evaluations)
-- -----------------------------------------------------------
-- template_id 1: 낙상 (FALL)
-- template_id 2: 욕창 (BEDSORE)
-- template_id 3: 인지 (COGNITIVE)

INSERT INTO basic_evaluations (eval_type, template_id, eval_date, eval_data, beneficiary_id, care_worker_id) VALUES
-- 2-1. 낙상 평가 결과 (template_id: 1)
('FALL', 1, '2025-12-08',
 '{
   "template_code": "FALL",
   "template_version": 1,
   "result": { "total_score": 1, "grade": "낙상 위험 낮음" },
   "answers": [
     { "code": "age", "label": "연령", "selected": { "score": 1, "label": "60~69세" } },
     { "code": "mental_status", "label": "정신상태", "selected": { "score": 0, "label": "정상" } },
     { "code": "stool", "label": "배변", "selected": { "score": 0, "label": "없음" } },
     { "code": "fall_history", "label": "낙상경험", "selected": { "score": 0, "label": "없음" } },
     { "code": "activity", "label": "활동", "selected": { "score": 0, "label": "완전 자립" } },
     { "code": "balance", "label": "걸음걸이 및 균형", "selected": { "score": 0, "label": "정상" } },
     { "code": "recent", "label": "지난 7일간 약복용이나 계획된 약물", "selected": { "score": 0, "label": "해당없음" } }
   ]
 }', 1, 1),

('FALL', 1, '2025-12-08',
 '{
   "template_code": "FALL",
   "template_version": 1,
   "result": { "total_score": 7, "grade": "낙상 위험 높음" },
   "answers": [
     { "code": "age", "label": "연령", "selected": { "score": 1, "label": "60~69세" } },
     { "code": "mental_status", "label": "정신상태", "selected": { "score": 0, "label": "정상" } },
     { "code": "stool", "label": "배변", "selected": { "score": 1, "label": "유치도뇨관/인공항문" } },
     { "code": "fall_history", "label": "낙상경험", "selected": { "score": 2, "label": "이미 한 번 또는 두 번 넘어짐" } },
     { "code": "activity", "label": "활동", "selected": { "score": 1, "label": "자리에서 일어나 앉기 도움" } },
     { "code": "balance", "label": "걸음걸이 및 균형", "selected": { "score": 2, "label": "보행장애/보조도구나 도움으로 걷기" } },
     { "code": "recent", "label": "지난 7일간 약복용이나 계획된 약물", "selected": { "score": 0, "label": "해당없음" } }
   ]
 }', 2, 1),

('FALL', 1, '2025-12-08',
 '{
   "template_code": "FALL",
   "template_version": 1,
   "result": { "total_score": 23, "grade": "낙상 위험 매우 높음" },
   "answers": [
     { "code": "age", "label": "연령", "selected": { "score": 3, "label": "80세 이상" } },
     { "code": "mental_status", "label": "정신상태", "selected": { "score": 4, "label": "혼란스러움/정신장애" } },
     { "code": "stool", "label": "배변", "selected": { "score": 4, "label": "소변, 대변 실금" } },
     { "code": "fall_history", "label": "낙상경험", "selected": { "score": 4, "label": "이미 세 번 이상 넘어짐" } },
     { "code": "activity", "label": "활동", "selected": { "score": 4, "label": "전적으로 도움을 받음" } },
     { "code": "balance", "label": "걸음걸이 및 균형", "selected": { "score": 4, "label": "평균적/불안정, 걸을 때 균형 거의 유지 못함" } },
     { "code": "recent", "label": "지난 7일간 약복용이나 계획된 약물", "selected": { "score": 0, "label": "해당없음" } }
   ]
 }', 3, 1),

-- 2-2. 욕창 평가 결과 (template_id: 2)
('BEDSORE', 2, '2025-12-08',
 '{
   "template_code": "BEDSORE",
   "template_version": 1,
   "answers": [
     { "code": "sensory",    "score": 4, "label": "손상 없음" },
     { "code": "moisture",   "score": 4, "label": "거의 없음" },
     { "code": "activity",   "score": 4, "label": "자주 보행" },
     { "code": "mobility",   "score": 4, "label": "제한 없음" },
     { "code": "nutrition",  "score": 3, "label": "양호" },
     { "code": "friction",   "score": 2, "label": "잠재적 문제" }
   ],
   "total_score": 21,
   "grade": "낮음(위험 없음)",
   "comment": "이동 및 영양 상태 양호, 피부 관리 유지 권장"
 }', 1, 1),

('BEDSORE', 2, '2025-12-08',
 '{
   "template_code": "BEDSORE",
   "template_version": 1,
   "answers": [
     { "code": "sensory",    "score": 3, "label": "약간 제한" },
     { "code": "moisture",   "score": 3, "label": "가끔 축축함" },
     { "code": "activity",   "score": 2, "label": "의자안정" },
     { "code": "mobility",   "score": 3, "label": "약간 제한" },
     { "code": "nutrition",  "score": 2, "label": "약간 불량" },
     { "code": "friction",   "score": 2, "label": "잠재적 문제" }
   ],
   "total_score": 15,
   "grade": "중간(약간~중도 위험)",
   "comment": "장시간 앉아 있는 생활 패턴, 체위 변경 주기적 필요"
 }', 2, 1),

('BEDSORE', 2, '2025-12-08',
 '{
   "template_code": "BEDSORE",
   "template_version": 1,
   "answers": [
     { "code": "sensory",    "score": 1, "label": "완전 제한" },
     { "code": "moisture",   "score": 1, "label": "계속 축축함" },
     { "code": "activity",   "score": 1, "label": "침상안정" },
     { "code": "mobility",   "score": 1, "label": "완전 부동" },
     { "code": "nutrition",  "score": 2, "label": "약간 불량" },
     { "code": "friction",   "score": 2, "label": "잠재적 문제" }
   ],
   "total_score": 8,
   "grade": "매우 높음(위험 매우 높음)",
   "comment": "지속적 침상 생활, 욕창 고위험군으로 집중 관리 필요"
 }', 3, 1),

-- 2-3. 인지기능 평가 결과 (template_id: 3)
('COGNITIVE', 3, '2025-12-08',
 '{
   "template_code": "COGNITIVE",
   "education_level": { "code": "ELEMENTARY", "label": "초등학교 졸업", "cutoff": 22 },
   "sections": {
     "A": {
       "title": "지남력",
       "items": {
         "A1": { "answer": "정답", "score": 1 },
         "A2": { "answer": "정답", "score": 1 },
         "A3": { "answer": "정답", "score": 1 },
         "A4": { "answer": "정답", "score": 1 },
         "A5": { "answer": "정답", "score": 1 }
       },
       "section_score": 5
     },
     "C": {
       "title": "주의력",
       "items": {
         "C1": { "answer": "성공", "score": 1 },
         "C2": { "answer": "성공", "score": 1 },
         "C3": { "answer": "성공", "score": 1 }
       },
       "section_score": 3
     },
     "D": {
       "title": "시공간 기능",
       "items": { "D1": { "answer": "두 도형 모두 정확, 교차 정확", "score": 2 } },
       "section_score": 2
     },
     "E": {
       "title": "집행 기능",
       "items": {
         "E1": { "answer": "1분 동안 동물 이름 12개", "raw_count": 12, "score": 3 },
         "E2": { "answer": "실수 없이 시간 내 완료", "score": 3 }
       },
       "section_score": 6
     },
     "F": {
       "title": "기억력 - 회상",
       "items": {
         "F1": { "word": "비행기", "answer": "자발 회상", "score": 2 },
         "F2": { "word": "연필",   "answer": "자발 회상", "score": 2 },
         "F3": { "word": "소나무", "answer": "힌트 회상", "score": 1 }
       },
       "raw_total": 5,
       "converted_total": 8
     },
     "G": {
       "title": "언어 기능",
       "items": { "G1": { "answer": "상황 완전 이해, 모두 정답", "score": 4 } },
       "section_score": 4
     }
   },
   "total_score": 28,
   "interpretation": {
     "result": "정상",
     "comment": "학력 보정 기준(초등학교 졸업, 22점) 이상으로 인지기능 양호"
   }
 }', 1, 1),

('COGNITIVE', 3, '2025-12-08',
 '{
   "template_code": "COGNITIVE",
   "education_level": { "code": "NO_EDU", "label": "무학(글자 앎)", "cutoff": 18 },
   "sections": {
     "A": {
       "title": "지남력",
       "items": {
         "A1": { "answer": "정답", "score": 1 },
         "A2": { "answer": "정답", "score": 1 },
         "A3": { "answer": "정답", "score": 1 },
         "A4": { "answer": "오답", "score": 0 },
         "A5": { "answer": "정답", "score": 1 }
       },
       "section_score": 4
     },
     "C": {
       "title": "주의력",
       "items": {
         "C1": { "answer": "성공", "score": 1 },
         "C2": { "answer": "실패", "score": 0 },
         "C3": { "answer": "성공", "score": 1 }
       },
       "section_score": 2
     },
     "D": {
       "title": "시공간 기능",
       "items": { "D1": { "answer": "형태는 맞으나 교차 오류", "score": 1 } },
       "section_score": 1
     },
     "E": {
       "title": "집행 기능",
       "items": {
         "E1": { "answer": "1분 동안 동물 이름 8개", "raw_count": 8, "score": 1 },
         "E2": { "answer": "스스로 수정하여 완료", "score": 2 }
       },
       "section_score": 3
     },
     "F": {
       "title": "기억력 - 회상",
       "items": {
         "F1": { "word": "비행기", "answer": "자발 회상", "score": 2 },
         "F2": { "word": "연필",   "answer": "힌트 회상", "score": 1 },
         "F3": { "word": "소나무", "answer": "실패", "score": 0 }
       },
       "raw_total": 3,
       "converted_total": 5
     },
     "G": {
       "title": "언어 기능",
       "items": { "G1": { "answer": "부분 정답 (대부분 이해)", "score": 3 } },
       "section_score": 3
     }
   },
   "total_score": 20,
   "interpretation": {
     "result": "경도 인지저하 의심",
     "comment": "무학(글자 앎) 기준 컷오프 18점보다 약간 높으나, 일부 영역 저하로 추적 필요"
   }
 }', 2, 1),

('COGNITIVE', 3, '2025-12-08',
 '{
   "template_code": "COGNITIVE",
   "education_level": { "code": "MIDDLE_HIGH", "label": "중·고등학교 졸업", "cutoff": 24 },
   "sections": {
     "A": {
       "title": "지남력",
       "items": {
         "A1": { "answer": "오답", "score": 0 },
         "A2": { "answer": "정답", "score": 1 },
         "A3": { "answer": "오답", "score": 0 },
         "A4": { "answer": "정답", "score": 1 },
         "A5": { "answer": "오답", "score": 0 }
       },
       "section_score": 2
     },
     "C": {
       "title": "주의력",
       "items": {
         "C1": { "answer": "실패", "score": 0 },
         "C2": { "answer": "실패", "score": 0 },
         "C3": { "answer": "성공", "score": 1 }
       },
       "section_score": 1
     },
     "D": {
       "title": "시공간 기능",
       "items": { "D1": { "answer": "형태 다름", "score": 0 } },
       "section_score": 0
     },
     "E": {
       "title": "집행 기능",
       "items": {
         "E1": { "answer": "1분 동안 동물 이름 4개", "raw_count": 4, "score": 0 },
         "E2": { "answer": "시간 초과 또는 도움 받아 완료", "score": 1 }
       },
       "section_score": 1
     },
     "F": {
       "title": "기억력 - 회상",
       "items": {
         "F1": { "word": "비행기", "answer": "힌트 회상", "score": 1 },
         "F2": { "word": "연필",   "answer": "실패", "score": 0 },
         "F3": { "word": "소나무", "answer": "실패", "score": 0 }
       },
       "raw_total": 1,
       "converted_total": 2
     },
     "G": {
       "title": "언어 기능",
       "items": { "G1": { "answer": "질문 이해 어려움, 거의 대답 못함", "score": 1 } },
       "section_score": 1
     }
   },
   "total_score": 7,
   "interpretation": {
     "result": "중등도~중증 인지저하",
     "comment": "중·고등학교 졸업 기준 컷오프(24점)보다 크게 낮아 치매 등 정밀 평가 필요"
   }
 }', 3, 1),

-- 2-4. 욕구사정 평가 결과 (template_id: 4)
-- 1) 비교적 건강한 수급자 (자립, 만성질환 관리 위주)
(
    'NEEDS',4, '2025-12-09',
    '{
      "template_code": "NEEDS",
      "template_version": 1,
      "answers": {
        "1": {
          "weight": 65,
          "height": 170,
          "nutrition_status": "양호",
          "diet_type": ["일반식"],
          "therapeutic_diet": ["저염식"],
          "eating_problem": ["양호"],
          "oral_status": "양호",
          "urination_status": ["양호"],
          "defecation_status": ["양호"],
          "diaper_use": []
        },
        "2": {
          "past_history": "10년 전 맹장수술",
          "current_diagnosis": "고혈압, 고지혈증",
          "chronic_disease": ["고혈압"],
          "circulatory": [],
          "neurologic": [],
          "musculoskeletal": ["관절염"],
          "mental_behavior": [],
          "respiratory": [],
          "renal": [],
          "other_disease": []
        },
        "3": {
          "adl": {
            "옷 벗고 입기": "완전자립", "세수하기": "완전자립", "양치질하기": "완전자립",
            "식사하기": "완전자립", "목욕하기": "완전자립", "체위변경": "완전자립",
            "일어나 앉기": "완전자립", "옮겨 앉기": "완전자립", "화장실 사용하기": "완전자립",
            "몸단장하기": "완전자립"
          }
        },
        "4": {
          "behavior": []
        },
        "5": {
          "hearing": "가까운 곳 대화 가능",
          "communication": "모두 이해·표현 가능",
          "speech": "정확함"
        },
        "6": {
          "living_with": ["배우자"],
          "children": { "has_children": "유", "son_count": 1, "daughter_count": 1 },
          "main_supporter": "유",
          "supporter_relation": "배우자",
          "economic": "연금생활",
          "care_burden": "전혀 부담되지 않음",
          "housing": "아파트"
        },
        "7": {
          "medical_institution": {
             "hospital_name": "서울내과",
             "regular_visit": "유",
             "hospital_phone": "02-123-4567"
          },
          "religion": "기독교",
          "community_service": ["노인정", "종교단체"]
        },
        "8": {
          "subjective_need": "무릎이 아파서 병원 동행 서비스만 가끔 필요함. 그 외에는 스스로 생활 가능."
        },
        "9": {
          "summary": "신체 기능이 양호하며 인지 기능도 정상임. 고혈압 약물 관리와 주기적인 관절염 치료가 필요함."
        },
        "10": {
          "respiration": [],
          "nutrition_nursing": [],
          "elimination_nursing": [],
          "wound": [],
          "bedsore_stage": null,
          "bedsore_site": [],
          "bedsore_prevention": "해당없음",
          "pain_cancer_site": [],
          "pain_general_site": ["하지"]
        }
      }
    }',
    1, -- beneficiary_id
    1    -- employee_id
),

-- 2) 부분 도움이 필요한 수급자 (경증 치매, 당뇨, 보행 부축)
(
    'NEEDS',4,'2025-12-09',
    '{
      "template_code": "NEEDS",
      "template_version": 1,
      "answers": {
        "1": {
          "weight": 52,
          "height": 155,
          "nutrition_status": "적당",
          "diet_type": ["일반식", "다진식"],
          "therapeutic_diet": ["당뇨식"],
          "eating_problem": ["저작곤란"],
          "oral_status": "틀니",
          "urination_status": ["요실금"],
          "defecation_status": ["변비"],
          "diaper_use": ["기저귀"]
        },
        "2": {
          "past_history": "당뇨 합병증으로 시력 저하",
          "current_diagnosis": "알츠하이머 치매, 당뇨",
          "chronic_disease": ["당뇨"],
          "circulatory": [],
          "neurologic": ["치매"],
          "musculoskeletal": ["요통, 좌골통"],
          "mental_behavior": ["수면장애"],
          "respiratory": [],
          "renal": [],
          "other_disease": []
        },
        "3": {
          "adl": {
            "옷 벗고 입기": "부분도움", "세수하기": "부분도움", "양치질하기": "부분도움",
            "식사하기": "부분도움", "목욕하기": "완전도움", "체위변경": "완전자립",
            "일어나 앉기": "완전자립", "옮겨 앉기": "부분도움", "화장실 사용하기": "부분도움",
            "몸단장하기": "부분도움"
          }
        },
        "4": {
          "behavior": ["배회 (의미 없이 걷는다.)", "우울 (슬프거나 처져있고 때로는 운다.)"]
        },
        "5": {
          "hearing": "큰소리만 들음",
          "communication": "대부분 이해하고 의사표현 가능",
          "speech": "간혹 왜곡"
        },
        "6": {
          "living_with": ["독거"],
          "children": { "has_children": "유", "son_count": 0, "daughter_count": 2 },
          "main_supporter": "유",
          "supporter_relation": "자녀",
          "economic": "기초생활수급",
          "care_burden": "자주 부담됨",
          "housing": "다세대주택"
        },
        "7": {
          "medical_institution": {
             "hospital_name": "행복요양병원",
             "regular_visit": "유",
             "hospital_phone": "031-999-8888"
          },
          "religion": "불교",
          "community_service": ["노인맞춤돌봄서비스", "급식및도시락배달", "보건소사업"]
        },
        "8": {
          "subjective_need": "혼자 식사 챙기기가 어렵고 밤에 잠이 안 와서 말벗이 필요함."
        },
        "9": {
          "summary": "경증 치매 증상으로 배회 증상이 있어 낙상 주의가 요구됨. 식사 준비 및 위생 관리에 부분적인 도움이 필요하며 당뇨 식단 관리가 중요함."
        },
        "10": {
          "respiration": [],
          "nutrition_nursing": [],
          "elimination_nursing": [],
          "wound": ["당뇨발간호"],
          "bedsore_stage": "1단계",
          "bedsore_site": ["뒤꿈치"],
          "bedsore_prevention": "발 보호대 착용",
          "pain_cancer_site": [],
          "pain_general_site": ["허리"]
        }
      }
    }',
    2,
    1
),

-- 3) 와상 상태 수급자 (전적 도움, 욕창 3단계, 비위관 영양)
(
    'NEEDS',4,'2025-12-09',
    '{
      "template_code": "NEEDS",
      "template_version": 1,
      "answers": {
        "1": {
          "weight": 45,
          "height": 160,
          "nutrition_status": "매우 나쁨",
          "diet_type": ["유동식"],
          "therapeutic_diet": [],
          "eating_problem": ["연하곤란"],
          "oral_status": "청결불량",
          "urination_status": ["기타"],
          "urination_status_text": "소변줄 착용 중",
          "defecation_status": ["지속적인 설사"],
          "diaper_use": ["기저귀"]
        },
        "2": {
          "past_history": "뇌졸중(2회)",
          "current_diagnosis": "뇌경색 후유증, 사지마비, 흡인성 폐렴",
          "chronic_disease": [],
          "circulatory": ["뇌경색"],
          "neurologic": [],
          "musculoskeletal": ["골절 등 후유증"],
          "mental_behavior": [],
          "respiratory": ["호흡곤란"],
          "renal": [],
          "other_disease": []
        },
        "3": {
          "adl": {
            "옷 벗고 입기": "완전도움", "세수하기": "완전도움", "양치질하기": "완전도움",
            "식사하기": "완전도움", "목욕하기": "완전도움", "체위변경": "완전도움",
            "일어나 앉기": "완전도움", "옮겨 앉기": "완전도움", "화장실 사용하기": "완전도움",
            "몸단장하기": "완전도움"
          }
        },
        "4": {
          "behavior": []
        },
        "5": {
          "hearing": "소리에 거의 반응 없음",
          "communication": "거의 이해하지 못하고 의사전달 불가능",
          "speech": "전혀 안 됨"
        },
        "6": {
          "living_with": ["자녀", "자부/사위"],
          "children": { "has_children": "유", "son_count": 1, "daughter_count": 0 },
          "main_supporter": "유",
          "supporter_relation": "자부",
          "economic": "불안정",
          "care_burden": "항상 부담됨",
          "housing": "아파트"
        },
        "7": {
          "medical_institution": {
             "hospital_name": "대학병원 신경과",
             "regular_visit": "유",
             "hospital_phone": "02-555-9999"
          },
          "religion": "기타",
          "religion_text": "무교",
          "community_service": ["방문간호", "기저귀지원", "이동서비스"]
        },
        "8": {
          "subjective_need": "욕창 치료와 가래 흡인(석션) 도움이 절실함. 보호자의 수발 부담이 매우 큼."
        },
        "9": {
          "summary": "전신 마비로 인한 와상 상태로 모든 일상생활에 전적인 도움이 필요함. 욕창 관리, 비위관 영양, 석션 등 전문적인 간호 처치가 매일 요구됨."
        },
        "10": {
          "respiration": ["흡인", "산소요법"],
          "nutrition_nursing": ["경관영양"],
          "elimination_nursing": ["유치도뇨관"],
          "wound": ["상처간호"],
          "wound_text": { "상처간호": "엉덩이 천골 부위" },
          "bedsore_stage": "3단계",
          "bedsore_site": ["엉덩이"],
          "bedsore_prevention": "에어매트리스 사용, 2시간마다 체위변경",
          "pain_cancer_site": [],
          "pain_general_site": ["전신"]
        }
      }
    }',
    3,
    1
);

/* [추가 데이터] 배정 이력 (Assignment History)
   - 요양보호사(id2)와 수급자(id3) 간의 매칭 변경 이력을 기록합니다.
   - matching 테이블에 있는 현재 데이터와, 과거에 종료된 데이터를 포함합니다.
*/

INSERT INTO assignment_history (start_date, end_date, status, reason, created_at, id2, id3) VALUES
-- 1. 수급자 1번 (현재 요양보호사 1번 배정 중, 과거에 2번이 담당했음)
('2023-01-01', '2023-12-31', '종료', '요양보호사 개인 사정', '2023-01-01 09:00:00', 2, 1),
('2024-01-01', NULL,         '배정', '정기 배정',           '2024-01-01 09:00:00', 1, 1),

-- 2. 수급자 2번 (현재 요양보호사 2번 배정 중)
('2024-04-15', NULL,         '배정', '신규 계약 배정',      '2024-04-15 10:00:00', 2, 2),

-- 3. 수급자 3번 (요양보호사 3번 배정 후 종료됨)
('2023-07-01', '2023-09-30', '종료', '수급자 입원',         '2023-07-01 09:00:00', 3, 3),
('2023-10-01', '2024-03-31', '종료', '계약 만료',           '2023-10-01 09:00:00', 3, 3),

-- 4. 수급자 4번 (요양보호사 4번 -> 보호자 요청으로 중단)
('2024-02-01', '2024-02-20', '중단', '보호자 요청(불만족)', '2024-02-01 14:00:00', 4, 4),

-- 5. 수급자 5번 (요양보호사 5번 -> 단기 종료)
('2024-01-10', '2024-02-05', '종료', '수급자 거주지 이전',  '2024-01-10 11:00:00', 5, 5),

-- 6. 수급자 6번 (장기 근속 배정)
('2023-06-01', NULL,         '배정', '장기 근속 유지',      '2023-06-01 09:00:00', 6, 6),

-- 7. 수급자 7번 (단기 대체 근무 이력)
('2024-01-01', '2024-01-31', '종료', '기존 요양사 휴가 대체', '2024-01-01 09:00:00', 8, 7),
('2024-05-01', '2024-05-31', '종료', '단기 서비스 제공',      '2024-05-01 09:00:00', 7, 7),

-- 8. 수급자 8번 (현재 진행 중)
('2024-03-01', NULL,         '배정', '정기 배정',           '2024-03-01 10:00:00', 8, 8),

-- 9. 수급자 9번 (품질 이슈로 교체 이력)
('2023-12-01', '2024-01-19', '종료', '요양보호사 변경 요청',  '2023-12-01 09:00:00', 10, 9),
('2024-01-20', '2024-03-10', '종료', '서비스 만족도 문제',    '2024-01-20 13:00:00', 9, 9),

-- 10. 수급자 10번 (대기 상태였다가 배정)
('2024-06-01', NULL,         '배정', '신규 배정',           '2024-06-01 09:00:00', 10, 10),

-- 11. 기타 과거 이력 (다양한 케이스)
('2023-05-01', '2023-12-31', '종료', '계약 종료',           '2023-05-01 09:00:00', 5, 11),
('2024-01-01', '2024-02-28', '종료', '수급자 건강 악화',      '2024-01-01 09:00:00', 6, 12),
('2023-09-01', NULL,         '배정', '가족 요양',           '2023-09-01 09:00:00', 7, 13),
('2024-01-01', NULL,         '배정', '주간 보호 병행',      '2024-01-01 09:00:00', 8, 14),
('2023-11-01', '2024-01-31', '종료', '동절기 단기',         '2023-11-01 09:00:00', 9, 15);


-- 월별 서비스유형별 금액 누계 이력(방문일정 테이블 기준)
-- 분 단위로 계산 후 시간 환산 * 단가
-- `rfid_start_time`, `rfid_end_time`가 NULL이면 “아직 출근/퇴근 태그 전”이므로 집계/리스트에서 제외(금액도 아직 확정되지 않은 건으로 취급)
-- => “실제 태그 완료된 건만 보여줌”
INSERT INTO cost_of_beneficiary_record
(visit_schedule_id, beneficiary_id, service_type_id, calculated_amount)
SELECT
    vs.vs_id,
    vs.beneficiary_id,
    vs.service_type_id,

    ROUND(TIMESTAMPDIFF(MINUTE, vs.rfid_start_time, vs.rfid_end_time) / 60, 1) * mst.money
        AS calculated_amount

FROM visit_schedule vs
         JOIN m_service_type mst
              ON mst.id = vs.service_type_id

WHERE vs.rfid_start_time IS NOT NULL
  AND vs.rfid_end_time   IS NOT NULL;


INSERT INTO product_history
(product_id, product_name, start_date, end_date, status, content, employee_id, employee_name, beneficiary_id, beneficiary_name)
VALUES
-- 1. [EM001-001 전동침대] 사이클: 구매 -> 렌탈
('EM001-001', '전동침대', '2024-01-15 09:00:00', NULL, 'PURCHASED', '신규 장비 제조사 구매 입고', 4, '김자재1', NULL, NULL),
('EM001-001', '전동침대', '2024-02-01 10:00:00', '2025-01-31 23:59:59', 'RENTAL', '장기 요양 렌탈 계약 (12개월)', 4, '김자재1', 1, '김영수'),

-- 2. [EM002-001 휠체어] 사이클: 구매 -> 렌탈 -> 수리
('EM002-001', '휠체어', '2024-01-20 09:00:00', NULL, 'PURCHASED', '신규 장비 제조사 구매 입고', 5, '이자재2', NULL, NULL),
('EM002-001', '휠체어', '2024-03-10 11:00:00', '2024-06-10 11:00:00', 'RENTAL', '단기 렌탈 계약 (3개월)', 5, '이자재2', 2, '이정자'),
('EM002-001', '휠체어', '2024-06-12 14:00:00', '2024-06-15 18:00:00', 'REPAIR', '반납 후 브레이크 점검 및 수리', 5, '이자재2', NULL, NULL),

-- 3. [EM003-001 에어매트] 사이클: 구매 -> 폐기
('EM003-001', '에어매트', '2023-05-01 09:00:00', NULL, 'PURCHASED', '이월 상품 구매', 4, '김자재1', NULL, NULL),
('EM003-001', '에어매트', '2024-12-20 15:30:00', NULL, 'DISCARD', '내구 연한 초과 및 파손으로 인한 폐기', 4, '김자재1', NULL, NULL),

-- 4. [EM004-001 보행보조기] 사이클: 구매 (현재 보관 중)
('EM004-001', '보행보조기', '2024-12-01 09:00:00', NULL, 'PURCHASED', '신규 모델 입고', 5, '이자재2', NULL, NULL),

-- 5. [EM001-002 전동침대] 사이클: 구매 -> 렌탈 (진행 중)
('EM001-002', '전동침대', '2024-06-01 09:00:00', NULL, 'PURCHASED', '추가 물량 확보', 4, '김자재1', NULL, NULL),
('EM001-002', '전동침대', '2024-07-01 10:00:00', '2025-06-30 23:59:59', 'RENTAL', '신규 수급자 렌탈 계약', 5, '이자재2', 3, '박종원');