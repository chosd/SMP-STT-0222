-- ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ COMMON START
-- DROP SEQ
drop sequence if exists SEQ_IAAP_CM_DIRECTORY_GROUP;

drop sequence if exists SEQ_IAAP_CM_DIRECTORY;
-- DROP TABLE
drop table if exists IAAP_CM_DIRECTORY_GROUP;

drop table if exists IAAP_CM_DIRECTORY;

create table IAAP_CM_DIRECTORY_GROUP
(
    ID INT not null COMMENT '디렉토리 그룹 ID',
    NAME VARCHAR(100) not null COMMENT '디렉토리 그룹명',
    DESCRIPTION VARCHAR(256) COMMENT '디렉토리 그룹 설명',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    PATH_GUBUN VARCHAR(20) COMMENT '데이터셋 구분용(학습TRAIN,검증VERIFY)',
    HOME_PATH VARCHAR(20) COMMENT '데이터셋의 루트경로',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '디렉토리 그룹';
-- CREATE TABLE
create table IAAP_CM_DIRECTORY
(
    ID INT not null COMMENT '디렉토리 ID',
    NAME VARCHAR(100) not null COMMENT '디렉토리명',
    HOME_PATH VARCHAR(500) not null COMMENT '디렉토리 홈 경로',
    path VARCHAR(500) not null COMMENT '디렉토리 경로',
    PRIORITY INT not null COMMENT '노출순서',
    IN_USE VARCHAR(1) not null COMMENT '노출여부',
    GROUP_ID INT not null COMMENT '그룹ID',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '디렉토리';
-- CREATE SEQ
create sequence SEQ_IAAP_CM_DIRECTORY_GROUP
start with
1 increment by 1;

create sequence SEQ_IAAP_CM_DIRECTORY
start with
1 increment by 1;
-- ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ COMMON END
-- ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ STT START
-- DROP SEQ
drop sequence if exists SEQ_IAAP_STT_TRAIN_DATA;

drop sequence if exists SEQ_IAAP_STT_TRAIN_AM_DATA;

drop sequence if exists SEQ_IAAP_STT_SERVICE_MODEL;
-- DROP SEQUENCE IF EXISTS SEQ_IAAP_STT_ENGINE_URL;
drop sequence if exists SEQ_IAAP_STT_VERIFY_DATASET;

drop sequence if exists SEQ_IAAP_STT_VERIFY_DATA;

drop sequence if exists SEQ_IAAP_STT_VERIFY_HISTORY;

drop sequence if exists SEQ_IAAP_STT_LOG;

drop sequence if exists SEQ_IAAP_STT_DICTATION;

drop sequence if exists SEQ_IAAP_STT_USAGE;

drop sequence if exists SEQ_IAAP_STT_DEPLOY_MODEL;

drop sequence if exists SEQ_IAAP_STT_DEPLOY_MNG;

drop sequence if exists SEQ_IAAP_STT_TRAIN_MNG;

drop sequence if exists SEQ_IAAP_STT_CALL_INFO;

drop sequence if exists SEQ_IAAP_STT_RESULT_INFO;
-- DROP TABLE
drop table if exists IAAP_STT_TRAIN_DATA;

drop table if exists IAAP_STT_TRAIN_AM_DATA;

drop table if exists IAAP_STT_SERVICE_MODEL;
-- DROP TABLE IF EXISTS IAAP_STT_ENGINE_URL;
drop table if exists IAAP_STT_VERIFY_DATASET;

drop table if exists IAAP_STT_VERIFY_DATA;

drop table if exists IAAP_STT_VERIFY_HISTORY;

drop table if exists IAAP_STT_LOG;

drop table if exists IAAP_STT_DICTATION;

drop table if exists IAAP_STT_USAGE;

drop table if exists IAAP_STT_TRAIN_MNG;

drop table if exists IAAP_STT_DEPLOY_MNG;

drop table if exists IAAP_STT_DEPLOY_MODEL;

drop table if exists IAAP_STT_TEST_DATA;

drop table if exists IAAP_STT_STATISTICS;

drop table if exists IAAP_STT_STATISTICS_ERROR;

drop table if exists IAAP_STT_CONFIG;

drop table if exists IAAP_STT_RESULT_INFO;

drop table if exists IAAP_STT_CALL_INFO;
-- CREATE TABLE
create table IAAP_STT_TRAIN_DATA
(
    ID INT(11) not null COMMENT '학습데이터ID',
    DATA_TYPE VARCHAR(20) not null COMMENT '데이터구분',
    SERVICE_MODEL VARCHAR(100) COMMENT '서비스모델',
    SERVICE_MODEL_ID INT not null COMMENT '서비스모델 ID',
    CONTENTS VARCHAR(255) not null COMMENT '데이터',
    REPEAT_COUNT INT(11) not null default 1 COMMENT '가중치',
    DESCRIPTION VARCHAR(255) default null COMMENT '설명',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP() COMMENT '등록일시',
    REG_ID VARCHAR(60) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(23) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default null COMMENT '수정일시',
    UPD_ID VARCHAR(60) default null COMMENT '수정자 ID',
    UPD_IP VARCHAR(23) default null COMMENT '수정자 IP',
    USE_YN CHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID),
    unique key SERVICE_MODEL (SERVICE_MODEL_ID,
CONTENTS)
) engine = InnoDB
  default CHARSET = utf8mb4 
  collate = utf8mb4_unicode_ci COMMENT = 'STT 학습데이터 관리 테이블';

create table IAAP_STT_TRAIN_AM_DATA (
  AM_DATA_ID int(11) not null COMMENT 'AM학습데이터ID',
  MODEL_TYPE varchar(20) not null COMMENT '학습데이터 모델타입(E2ELM,CLASS..)',
  SERVICE_MODEL varchar(20) default null COMMENT '서비스모델',
  SERVICE_MODEL_ID int(11) not null COMMENT '서비스모델 ID',
  ANS_DATASET_NM varchar(50) default null COMMENT '정답지 데이터셋 이름',
  TRAIN_VOICE_CNT int(11) default null COMMENT '학습음성갯수',
  DESCRIPTION varchar(100) default null COMMENT '설명',
  DATA_SOURCE int(11) DEFAULT NULL COMMENT '데이터 출처 여부(일반 : 1, 전사 : 2)',
  REG_DT datetime not null default current_timestamp() COMMENT '등록일시',
  REG_ID varchar(60) not null COMMENT '등록자 ID',
  REG_IP varchar(23) not null COMMENT '등록자 IP',
  UPD_DT datetime default null COMMENT '수정일시',
  UPD_ID varchar(60) default null COMMENT '수정자 ID',
  UPD_IP varchar(23) default null COMMENT '수정자 IP',
  USE_YN char(1) not null default 'Y' COMMENT '사용여부',
  ANS_FILE varchar(100) default null COMMENT '정답지 파일',
  VOICE_FILE varchar(100) default null COMMENT '음성파일',
  AM_DATA_PATH varchar(100) default null COMMENT 'AM학습데이터 경로',
  BASE_PATH varchar(50) default null COMMENT '학습데이터셋의 기본 경로',
  DETAIL_PATH varchar(20) default null COMMENT '학습데이터셋의 하위 경로',
  FIRST_PATH_YN char(1) DEFAULT NULL COMMENT '초기 대량 데이터 경로 여부',
  primary key (AM_DATA_ID),
  unique key SERVICE_MODEL (SERVICE_MODEL,
ANS_DATASET_NM)
) engine = InnoDB default CHARSET = utf8mb4 collate = utf8mb4_unicode_ci COMMENT = 'STT AM학습데이터 관리 테이블';

create table IAAP_STT_SERVICE_MODEL
(
    ID INT not null COMMENT '서비스모델 ID',
    SERVICE_MODEL_NAME VARCHAR(100) not null COMMENT '서비스모델명',
    SERVICE_CODE VARCHAR(100) not null COMMENT '서비스코드',
    DESCRIPTION VARCHAR(256) COMMENT '설명',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default null COMMENT '수정일시',
    UPD_ID VARCHAR(60) default null COMMENT '수정자 ID',
    UPD_IP VARCHAR(23) default null COMMENT '수정자 IP',
    USE_YN CHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT = 'STT 서비스모델 관리 테이블';

/* 사용 안함 
CREATE TABLE IAAP_STT_ENGINE_URL
(
    ID                   INT          NOT NULL                  COMMENT '엔진 URL ID',
    TENANT_NAME          VARCHAR(100) NOT NULL                  COMMENT '테넌트명',
    ENGINE_URL           VARCHAR(100) NOT NULL                  COMMENT 'URL',
    DESCRIPTION          VARCHAR(256)                           COMMENT '설명',
    REG_DT               DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID               VARCHAR(100) NOT NULL                  COMMENT '등록자 ID',
    REG_IP               VARCHAR(100) NOT NULL                  COMMENT '등록자 IP',
    UPD_DT               DATETIME     DEFAULT NULL              COMMENT '수정일시',
    UPD_ID               VARCHAR(60)  DEFAULT NULL              COMMENT '수정자 ID',
    UPD_IP               VARCHAR(23)  DEFAULT NULL              COMMENT '수정자 IP',
    USE_YN               CHAR(1)      NOT NULL DEFAULT 'Y'      COMMENT '사용여부',
    PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 
  COLLATE = utf8mb4_unicode_ci COMMENT ='STT 엔진 URL 관리 테이블';
*/

create table IAAP_STT_VERIFY_DATASET
(
    ID INT not null COMMENT '검증데이터셋 ID',
    SERVICE_MODEL_ID INT not null COMMENT '서비스모델 ID',
    DIRECTORY_ID INT not null COMMENT '디렉토리 ID',
    NAME VARCHAR(100) not null COMMENT '검증데이터셋 이름',
    DESCRIPTION VARCHAR(255) COMMENT '설명',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '검증데이터셋';

create table IAAP_STT_VERIFY_DATA
(
    ID INT not null COMMENT '검증데이터 ID',
    DATASET_ID INT not null COMMENT '검증데이터셋 ID',
    WAV_FILE_NAME VARCHAR(500) not null COMMENT '음원파일명',
    ANSWER_FILE_NAME VARCHAR(500) COMMENT '정답지파일명',
    DICTATED_TEXT longtext not null COMMENT '전사한 텍스트',
    DESCRIPTION varchar(500) DEFAULT NULL COMMENT '설명',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    DATASET_NAME VARCHAR(50) COMMENT '검증데이터셋 이름',
    SERVICE_MODEL_ID INT COMMENT '서비스모델ID',
    BASE_PATH VARCHAR(50) COMMENT '학습데이터셋의 기본 경로',
    DETAIL_PATH VARCHAR(20) COMMENT '학습데이터셋의 하위 경로',
    VERIFY_DATA_PATH VARCHAR(100) COMMENT '검증데이터 최종경로',
    FIRST_PATH_YN VARCHAR(1) COMMENT '초기 대량 데이터 경로 여부',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '검증데이터';

create table IAAP_STT_VERIFY_HISTORY
(
    ID INT not null COMMENT '검증이력 ID',
    DATASET_ID INT not null COMMENT '검증데이터셋 ID',
    DEPLOY_ID INT not null COMMENT '배포 ID',
    STATUS VARCHAR(100) not null COMMENT '검증상태',
    DESCRIPTION VARCHAR(500) COMMENT '설명',
    START_AT DATETIME COMMENT '요청시간',
    END_AT DATETIME COMMENT '종료시간',
    CER DOUBLE COMMENT 'CER',
    WER DOUBLE COMMENT 'WER',
    FAIL_CAUSE VARCHAR(500) COMMENT '실패원인',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    SERVICE_MODEL_ID INT not null COMMENT '서비스 모델 아이디',
    VERSION VARCHAR(100) not null COMMENT '버전',
    SAVE_YN VARCHAR(1) default 'N' COMMENT '인식률 반영 여부',
    DATASET_NAME VARCHAR(50) COMMENT '검증데이터셋 이름',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '검증이력';

create table IAAP_STT_LOG
(
    ID bigint not null COMMENT '로그 ID',
    CALL_KEY VARCHAR(500) not null COMMENT '콜키',
    CUSTOMER_IDENTIFIER VARCHAR(500) not null COMMENT '고객 식별정보',
    DIRECTION VARCHAR(100) not null COMMENT 'TXRX',
    START_AT DATETIME not null COMMENT '수신 시간',
    END_AT DATETIME null COMMENT '사용 안함',
    START_TIME bigint not null default 0 COMMENT '콜/턴 시작시간',
    END_TIME bigint not null default 0 COMMENT '콜/턴 종료시간',
    WAV_FILE_PATH VARCHAR(500) not null COMMENT '음원파일 경로',
    SERVICE_MODEL_ID INT not null COMMENT '서비스모델 ID',
    ASSISTANT_ID VARCHAR(100) COMMENT '상담사 ID',
    STATUS VARCHAR(100) not null COMMENT '성공 실패 여부',
    FAIL_CAUSE VARCHAR(500) COMMENT '실패여부',
    TRANSCRIPT longtext not null COMMENT 'STT 결과(문장)',
    TRANSCRIPT_START INT not null COMMENT 'STT 결과(문장) 시작 오프셋',
    TRANSCRIPT_END INT not null COMMENT 'STT 결과(문장) 끝 오프셋',
    CER DOUBLE not null COMMENT '신뢰도(CER)',
    SENTENCE_WAV_PATH VARCHAR(500) COMMENT '발화단위의 음성 경로',
  	WORDS TEXT COMMENT 'word 단위 신뢰도 정보',
    USED_AS_DICTATION VARCHAR(1) not null default 'N' COMMENT '전사데이터 등록여부',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID,
START_AT)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT 'STT 결과로그'
  partition by range columns (START_AT) (
        partition P202307
values LESS THAN ('2023-08-01 00:00:00'),
        partition P202308
values LESS THAN ('2023-09-01 00:00:00'),
        partition P202309
values LESS THAN ('2023-10-01 00:00:00'),
        partition P202310
values LESS THAN ('2023-11-01 00:00:00'),
        partition P202311
values LESS THAN ('2023-12-01 00:00:00'),

        partition P202312
values LESS THAN ('2024-01-01 00:00:00'),
        partition P202401
values LESS THAN ('2024-02-01 00:00:00'),
        partition P202402
values LESS THAN ('2024-03-01 00:00:00'),
        partition P202403
values LESS THAN ('2024-04-01 00:00:00'),
        partition P202404
values LESS THAN ('2024-05-01 00:00:00'),
        partition P202405
values LESS THAN ('2024-06-01 00:00:00'),
        partition P202406
values LESS THAN ('2024-07-01 00:00:00'),
        partition P202407
values LESS THAN ('2024-08-01 00:00:00'),
        partition P202408
values LESS THAN ('2024-09-01 00:00:00'),
        partition P202409
values LESS THAN ('2024-10-01 00:00:00'),
        partition P202410
values LESS THAN ('2024-11-01 00:00:00'),
        partition P202411
values LESS THAN ('2024-12-01 00:00:00'),

        partition P202412
values LESS THAN ('2025-01-01 00:00:00'),
        partition P202501
values LESS THAN ('2025-02-01 00:00:00'),
        partition P202502
values LESS THAN ('2025-03-01 00:00:00'),
        partition P202503
values LESS THAN ('2025-04-01 00:00:00'),
        partition P202504
values LESS THAN ('2025-05-01 00:00:00'),
        partition P202505
values LESS THAN ('2025-06-01 00:00:00'),
        partition P202506
values LESS THAN ('2025-07-01 00:00:00'),
        partition P202507
values LESS THAN ('2025-08-01 00:00:00'),
        partition P202508
values LESS THAN ('2025-09-01 00:00:00'),
        partition P202509
values LESS THAN ('2025-10-01 00:00:00'),
        partition P202510
values LESS THAN ('2025-11-01 00:00:00'),
        partition P202511
values LESS THAN ('2025-12-01 00:00:00'),

        partition P202512
values LESS THAN ('2026-01-01 00:00:00'),
        partition P202601
values LESS THAN ('2026-02-01 00:00:00'),
        partition P202602
values LESS THAN ('2026-03-01 00:00:00'),
        partition P202603
values LESS THAN ('2026-04-01 00:00:00'),
        partition P202604
values LESS THAN ('2026-05-01 00:00:00'),
        partition P202605
values LESS THAN ('2026-06-01 00:00:00'),
        partition P202606
values LESS THAN ('2026-07-01 00:00:00'),
        partition P202607
values LESS THAN ('2026-08-01 00:00:00'),
        partition P202608
values LESS THAN ('2026-09-01 00:00:00'),
        partition P202609
values LESS THAN ('2026-10-01 00:00:00'),
        partition P202610
values LESS THAN ('2026-11-01 00:00:00'),
        partition P202611
values LESS THAN ('2026-12-01 00:00:00'),
        partition PMAXVALUES
values LESS THAN maxvalue
);

create table IAAP_STT_DICTATION
(
    ID bigint not null COMMENT '전사데이터 ID',
    STT_LOG_ID bigint not null COMMENT 'STT 결과 ID',
    TRANSCRIPT longtext not null COMMENT 'STT 결과',
    WAV_FILE_PATH VARCHAR(500) not null COMMENT '음원파일 경로',
    SERVICE_MODEL_ID INT not null COMMENT '서비스타입',
    DIRECTION VARCHAR(100) not null COMMENT 'TXRX 구분',
    DICTATED_TEXT longtext not null default '' COMMENT '전사데이터',
    USAGE_TYPE VARCHAR(100) not null default 'NONE' COMMENT '데이터구분 AM 학습데이터 혹은 검증데이터',
    IS_PREEMPTED VARCHAR(1) not null default 'N' COMMENT '현재 수정중 여부(Y: 수정중, N: 수정중 아님)',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID),
    unique key IAAP_STT_DICTATION_UN (WAV_FILE_PATH)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '전사데이터';

create table IAAP_STT_USAGE
(
    ID INT not null COMMENT '데이터구분 ID',
    DICTATION_ID INT not null COMMENT '전사데이터 ID',
    VERIFY_DATASET_ID INT COMMENT '검증데이터셋 ID',
    AM_TRAIN_DATA_DIRECTORY_ID INT COMMENT 'AM 학습데이터 음원파일 저장 경로 ID',
    AM_DATA_PATH VARCHAR(100) COMMENT 'AM 학습데이터 저장 경로',
    WAV_FILE_NAME VARCHAR(100) COMMENT '검증 음원파일 이름',
    type VARCHAR(100) COMMENT '데이터구분 AM 학습데이터 혹은 검증데이터',
    REG_DT DATETIME not null default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '전사데이터 데이터구분';
-- 학습이력
create table IAAP_STT_TRAIN_MNG
(
    ID INT not null COMMENT '학습이력ID',
    SERVICE_MODEL_ID INT not null COMMENT '서비스모델ID',
    DESCRIPTION VARCHAR(256) COMMENT '설명',
    UPDATED_BY VARCHAR(256) not null COMMENT '작성자',
    REQUESTED_AT DATETIME not null COMMENT '학습일시',
    COMPLETED_AT DATETIME COMMENT '학습완료일시',
    DURATION INT COMMENT '소요시간',
    DATA_NUM INT not null COMMENT '학습데이터수',
    DATA_TIME VARCHAR(100) COMMENT '음성데이터 총 학습시간',
    RESULT_MODEL_ID VARCHAR(256) not null COMMENT '결과모델ID',
    RESULT_CODE VARCHAR(100) COMMENT '학습결과코드',
    RESULT_MSG VARCHAR(256) COMMENT '학습요청결과',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    STATUS VARCHAR(256) COMMENT '학습상태',
    MODEL_TYPE VARCHAR(256) COMMENT '학습요청한 LM TYPE',
    MODEL_PATH VARCHAR(256) COMMENT '학습된 모델 저장경로',
    MODEL_AUTH_KEY VARCHAR(256) COMMENT 'MD5 hash checksum',
    TRAIN_DATA_PATH VARCHAR(256) COMMENT '학습데이터 저장 경로',
    E2ESL_DATAPATH_LIST VARCHAR(256) COMMENT 'E2ESL,E2EMSL타입의 경우 사용하는 정답지 파일경로',
    E2ESL_WAVPATH_LIST VARCHAR(256) COMMENT 'E2ESL,E2EMSL타입의 경우 사용하는 지도 학습음성 파일경로',
    E2EUSL_WAVPATH_LIST VARCHAR(256) COMMENT 'E2EUSL,E2EMSL타입의 경우 사용하는 비지도 학습음성 파일경로',
    AM_DATASET_LIST VARCHAR(256) COMMENT 'AM학습데이터 등록시 저장되는 데이터셋 목록',
    USE_YN VARCHAR(2) not null COMMENT '삭제여부',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '학습이력';
-- 학습이력 인덱스 생성
create index IAAP_STT_TRAIN_MNG_requested_at_IDX on
IAAP_STT_TRAIN_MNG (requested_at);

create index IAAP_STT_TRAIN_MNG_result_model_id_IDX on
IAAP_STT_TRAIN_MNG (result_model_id);

create index IAAP_STT_TRAIN_MNG_service_model_id_IDX on
IAAP_STT_TRAIN_MNG (service_model_id);

create index IAAP_STT_TRAIN_MNG_updated_by_IDX on
IAAP_STT_TRAIN_MNG (updated_by);
-- 배포이력
create table IAAP_STT_DEPLOY_MNG
(
    ID INT not null COMMENT '배포이력ID',
    SERVICE_MODEL_ID INT not null COMMENT '서비스모델ID',
    DESCRIPTION VARCHAR(256) COMMENT '설명',
    CREATED_BY VARCHAR(256) not null COMMENT '작성자',
    REQUESTED_AT DATETIME not null COMMENT '배포일시',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    RESULT_MODEL_ID VARCHAR(256) not null COMMENT '결과모델ID',
    RESULT_MODEL_DESCRIPTION VARCHAR(256) not null COMMENT '결과모델설명',
    RESULT_MODEL_MODELTYPE VARCHAR(10) COMMENT '결과모델타입',
    RESULT_CODE VARCHAR(100) COMMENT '배포결과코드',
    RESULT_MSG VARCHAR(256) COMMENT '배포요청결과',
    COMPLETED_AT DATETIME COMMENT '배포완료일시',
    DURATION INT COMMENT '소요시간',
    STATUS VARCHAR(256) COMMENT '배포상태',
    USE_YN VARCHAR(2) not null COMMENT '삭제여부',
    SAVE_YN VARCHAR(2) default 'N' not null COMMENT '인식률 추이 포함 여부',
    DEPLOY_LIST TEXT COMMENT 'SRU서버들의 배포 상태',
    DEPLOY_URL VARCHAR(100) COMMENT '배포서버주소',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '배포이력';
-- 배포이력 인덱스 생성
create index IAAP_STT_DEPLOY_MNG_created_by_IDX on
IAAP_STT_DEPLOY_MNG (created_by);

create index IAAP_STT_DEPLOY_MNG_requested_at_IDX on
IAAP_STT_DEPLOY_MNG (requested_at);

create index IAAP_STT_DEPLOY_MNG_result_model_id_IDX on
IAAP_STT_DEPLOY_MNG (result_model_id);

create index IAAP_STT_DEPLOY_MNG_service_model_id_IDX on
IAAP_STT_DEPLOY_MNG (service_model_id);

create index IAAP_STT_DEPLOY_MNG_status_IDX on
IAAP_STT_DEPLOY_MNG (status);
-- 배포모델
create table IAAP_STT_DEPLOY_MODEL
(
    ID INT not null COMMENT '배포모델ID',
    SERVICE_MODEL_ID INT not null COMMENT '서비스모델ID',
    DESCRIPTION VARCHAR(256) COMMENT '설명',
    UPLOADED_BY VARCHAR(256) not null COMMENT '작성자',
    UPLOADED_DATE DATETIME not null COMMENT '등록날짜',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    RESULT_MODEL_ID VARCHAR(256) not null COMMENT '결과모델ID',
    MODEL_PATH VARCHAR(256) not null COMMENT '모델경로',
    MODEL_AUTH_KEY VARCHAR(256) not null COMMENT 'MD5 hash checksum',
    DATA_NUM INT not null COMMENT '학습데이터수',
    USE_YN VARCHAR(2) not null COMMENT '삭제여부',
    MODEL_TYPE VARCHAR(20) COMMENT '학습데이터의 모델타입',
    DATA_TIME VARCHAR(100) COMMENT '음성데이터의 총 학습시간',
    primary key (ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '배포모델';
-- 배포모델 인덱스 생성
create index IAAP_STT_DEPLOY_MODEL_result_model_id_IDX on
IAAP_STT_DEPLOY_MODEL (result_model_id);

create index IAAP_STT_DEPLOY_MODEL_service_model_id_IDX on
IAAP_STT_DEPLOY_MODEL (service_model_id);

create index IAAP_STT_DEPLOY_MODEL_uploaded_by_IDX on
IAAP_STT_DEPLOY_MODEL (uploaded_by);

create index IAAP_STT_DEPLOY_MODEL_uploaded_date_IDX on
IAAP_STT_DEPLOY_MODEL (uploaded_date);
-- 단건테스트
create table IAAP_STT_TEST_DATA
(
    UUID VARCHAR(256) not null COMMENT '단건테스트를 구분짓는 UUID',
    RESULT_CODE VARCHAR(100) not null COMMENT '테스트결과코드',
    RESULT_MSG VARCHAR(256) not null COMMENT '테스트요청결과',
    SERVICE_CODE VARCHAR(100) not null COMMENT '서비스코드',
    STT_RESULT TEXT not null COMMENT '단건테스트 결과',
    REG_DT DATETIME null default CURRENT_TIMESTAMP COMMENT '등록일자',
    primary key (UUID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT '단건테스트 임시 테이블';

create index IAAP_STT_TEST_DATA_uuid_IDX on
IAAP_STT_TEST_DATA (uuid);

create table IAAP_STT_STATISTICS
(
    RES_DATE_TIME VARCHAR(100) not null COMMENT '통계정보 응답 시간(분단위)',
    SUPPORT_SVC_LIST TEXT not null COMMENT 'SCU에서 제공하는 SVC 목록',
    COMPLETE_SVC_LIST text default null COMMENT '통계 정보 조회를 성공한 svc 목록',
    SERVICE_CODE VARCHAR(100) not null COMMENT '서비스 코드명',
    REQUEST_COUNT INTEGER not null COMMENT 'STT 요청 건수',
    COMPLETE_COUNT INTEGER not null COMMENT 'STT 완료 건수',
    FAIL_COUNT INTEGER not null COMMENT 'STT 실패 건수',
    BUSY_CALL_COUNT INTEGER COMMENT 'STT 통화중 건수',
    SCU_NO varchar(100) default null COMMENT 'SCU명',
    SERVER_NAME VARCHAR(100) not null COMMENT '서버명',
    TOTAL_SERVER_CNT int(11) not null COMMENT 'C-POD 전체 개수',
    COMPLETE_SERVER_CNT int(11) not null COMMENT 'STT 통계 정보 조회 성공한 C-POD개수',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(2) not null COMMENT '삭제여부',
    primary key (SERVICE_CODE,
RES_DATE_TIME,
SERVER_NAME,
REG_DT)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT 'STT 요청통계';

create table IAAP_STT_STATISTICS_ERROR
(
    RES_DATE_TIME VARCHAR(100) not null COMMENT '통계정보 응답 시간(분단위)',
    SERVICE_CODE VARCHAR(100) not null COMMENT '서비스 코드명',
    SCU_NO VARCHAR(100) not null COMMENT 'SCU명',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(100) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(100) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID VARCHAR(100) not null COMMENT '수정자 ID',
    UPD_IP VARCHAR(100) not null COMMENT '수정자 IP',
    USE_YN VARCHAR(2) not null COMMENT '삭제여부',
    primary key (RES_DATE_TIME,
SERVICE_CODE,
SCU_NO,
REG_DT)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT 'STT 에러통계';

create table IAAP_STT_CONFIG
(
    ID INT not null COMMENT 'CONFIG ID',
    CODE_KEY VARCHAR(100) not null COMMENT '키값',
    CODE_VALUE VARCHAR(100) not null COMMENT '설정값',
    DESCRIPTION VARCHAR(255) COMMENT '설명',
    REG_DT DATETIME default CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID VARCHAR(60) not null COMMENT '등록자 ID',
    REG_IP VARCHAR(23) not null COMMENT '등록자 IP',
    UPD_DT DATETIME default null COMMENT '수정일시',
    UPD_ID VARCHAR(60) default null COMMENT '수정자 ID',
    UPD_IP VARCHAR(23) default null COMMENT '수정자 IP',
    USE_YN CHAR(1) not null default 'Y' COMMENT '사용여부',
    primary key (ID),
    unique key IAAP_STT_CONFIG_UN (CODE_KEY,
DESCRIPTION)
) engine = InnoDB
  default CHARSET = utf8mb4 
  collate = utf8mb4_unicode_ci COMMENT = 'STT CONFIG정보 테이블';

create table IAAP_STT_CALL_INFO (
  STT_CALL_IDX int(11) not null COMMENT '테이블 pk',
  APPLICATION_ID varchar(50) COMMENT '상담어플리케이션 id',
  REC_ID varchar(50) COMMENT '녹취 id',
  CALL_ID varchar(50) COMMENT '콜 ID',
  STT_ID varchar(50) not null COMMENT 'STT ID',
  DEVICE_ID varchar(50) not null COMMENT '요청 Client id',
  SERVICE_CODE varchar(2) not null COMMENT 'STT 모델의 서비스 코드 번호',
  CALL_STATUS varchar(30) not null COMMENT '콜 상태',
  CALL_START_TIME datetime not null default current_timestamp() COMMENT '콜 시작시간',
  CALL_END_TIME datetime not null default current_timestamp() COMMENT '콜 종료시간',
  WAV_FILE_PATH varchar(300) null COMMENT '음원파일 경로',
  primary key (STT_CALL_IDX)
) engine = InnoDB default CHARSET = utf8mb4 collate = utf8mb4_unicode_ci COMMENT = '콜 정보 관리';

create index IAAP_STT_CALL_INFO_IDX0 on
IAAP_STT_CALL_INFO (APPLICATION_ID);

create index IAAP_STT_CALL_INFO_IDX1 on
IAAP_STT_CALL_INFO (REC_ID);

create index IAAP_STT_CALL_INFO_IDX2 on
IAAP_STT_CALL_INFO (CALL_ID);

create index IAAP_STT_CALL_INFO_IDX3 on
IAAP_STT_CALL_INFO (SERVICE_CODE);

create index IAAP_STT_CALL_INFO_IDX4 on
IAAP_STT_CALL_INFO (CALL_STATUS);

create index IAAP_STT_CALL_INFO_IDX5 on
IAAP_STT_CALL_INFO (CALL_START_TIME);

create index IAAP_STT_CALL_INFO_IDX6 on
IAAP_STT_CALL_INFO (SERVICE_CODE,
CALL_STATUS,
CALL_START_TIME);

create table IAAP_STT_RESULT_INFO (
  STT_RESULT_IDX int(11) not null COMMENT '테이블 pk',
  STT_ID varchar(50) not null COMMENT 'STT ID',
  SPEAKER_TYPE varchar(2) not null COMMENT '화자 구분  RX or TX',
  APPLICATION_ID varchar(50) not null COMMENT '상담어플리케이션 id',
  STT_SEQ int COMMENT 'STT SEQ',
  START_TIME_STAMP int COMMENT '시작 타임스탬프',
  END_TIME_STAMP int COMMENT '종료 타임스탬프',
  STT_JSON mediumtext default null COMMENT 'stt JSON 인식결과',
  STT_TEXT mediumtext default null COMMENT 'stt 인식결과',
  CONFIDENCE varchar(15) not null default 0 COMMENT 'STT 신뢰도 값',
  EPD_START_TIME DATETIME default CURRENT_TIMESTAMP COMMENT 'EPD 발화 시작 시간',
  EPD_END_TIME DATETIME default CURRENT_TIMESTAMP COMMENT 'EPD 발화 종료 시간',
  primary key (STT_RESULT_IDX)
) engine = InnoDB default CHARSET = utf8mb4 collate = utf8mb4_unicode_ci COMMENT = 'EPD단위의 STT 인식 결과 관리(신뢰도 추이 계산을 위해 사용)';

create index IAAP_STT_RESULT_INFO_APPLICATION_ID_IDX on
IAAP_STT_RESULT_INFO (`APPLICATION_ID`);

create table IAAP_STT_ERROR_LOG
(
    ID bigint not null COMMENT '장애이력 ID 시퀀스',
    type VARCHAR(100) not null COMMENT '장애유형 : IF(인터페이스), SYSTEM(시스템)',
/* SQLINES DEMO *** (인터페이스), SYSTEM(시스템) */
ERROR_CODE VARCHAR(100) COMMENT 'IF 인 경우 장애 코드',
/* SQLINES DEMO *** 애 코드 */
ERROR_MSG VARCHAR(100) COMMENT 'IF 인 경우 장애 메시지',
/* SQLINES DEMO *** 애 메시지 */
API_URL VARCHAR(500) COMMENT 'IF 인 경우 요청 URL',
/* SQLINES DEMO *** 청 URL */
SERVER_ID VARCHAR(200) COMMENT 'SYSTEM 인 경우 서버ID',
/* SQLINES DEMO ***  서버ID */
ERROR_POINT VARCHAR(1) COMMENT 'SYSTEM 인 경우 모니터링 대상(C:CPU, M:MEMORY, S:STORAGE)',
/* SQLINES DEMO ***  모니터링 대상 */
THRESHOLD DECIMAL(20) COMMENT 'SYSTEM 인 경우 임계설정값',
/* SQLINES DEMO ***  임계설정값 */
STATUS_VALUE DECIMAL(20) COMMENT 'SYSTEM 인 경우 상태값',
/* SQLINES DEMO ***  상태값 */
ERROR_TIME DATETIME default SYSDATE() COMMENT '장애발생시간',
/* SQLINES DEMO *** � */
    primary key (ID)
) engine = InnoDB default CHARSET = utf8mb4 collate = utf8mb4_unicode_ci COMMENT = '장애이력';
-- SQLINES LICENSE FOR EVALUATION USE ONLY
create index IAAP_STT_ERROR_LOG_TYPE_IDX on
IAAP_STT_ERROR_LOG (`TYPE`);

create table IAAP_STT_SYSTEM_STATUS
(
    ID bigint not null COMMENT '통계ID',
    SERVER_ID VARCHAR(200) collate UTF8MB4_UNICODE_CI not null COMMENT '서버의 hostname',
    CPU_USED INT default 0 COMMENT 'CPU사용율',
    MAX_MEMORY_SIZE bigint default 0 COMMENT '물리 메모리 사이즈 (mbyte)',
    FREE_MEMORY_SIZE bigint default 0 COMMENT '사용 가능한 물리 메모리 용량 (mbyte)',
    MAX_APP_STORAGE_SIZE bigint default 0 COMMENT 'App 파티션의 디스크 사이즈 (mbyte)',
    FREE_APP_STORAGE_SIZE bigint default 0 COMMENT '남은 App 파티션의 디스크 사이즈 (mbyte)',
    MAX_LOG_STORAGE_SIZE bigint default 0 COMMENT '로그 파티션의 디스크 사이즈 (mbyte)',
    FREE_LOG_STORAGE_SIZE bigint default 0 COMMENT '남은 로그 파티션의 디스크 사이즈 (mbyte)',
    BPS bigint default 0 COMMENT '초당 송/수신 bit 수  (단위 kbps)',
    PPS bigint default 0 COMMENT '초당 송/수신 packet 수 (단위 pps)',
    LAST_CHECK_TIME DATETIME not null COMMENT '시스템 정보가 체크된 시간',
    REG_DT DATETIME default null COMMENT '등록일시',
    REG_ID VARCHAR(60) collate UTF8MB4_UNICODE_CI default null COMMENT '등록아이디',
    REG_IP VARCHAR(23) collate UTF8MB4_UNICODE_CI default null COMMENT '등록아이피',
    primary key (ID,
LAST_CHECK_TIME),
    unique key IAAP_STT_SYSTEM_STATUS_UN (SERVER_ID,
LAST_CHECK_TIME)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT = '시스템 현황 조회'
    partition by range columns (LAST_CHECK_TIME) (
        partition P202307
values LESS THAN ('2023-08-01 00:00:00'),
        partition P202308
values LESS THAN ('2023-09-01 00:00:00'),
        partition P202309
values LESS THAN ('2023-10-01 00:00:00'),
        partition P202310
values LESS THAN ('2023-11-01 00:00:00'),
        partition P202311
values LESS THAN ('2023-12-01 00:00:00'),

        partition P202312
values LESS THAN ('2024-01-01 00:00:00'),
        partition P202401
values LESS THAN ('2024-02-01 00:00:00'),
        partition P202402
values LESS THAN ('2024-03-01 00:00:00'),
        partition P202403
values LESS THAN ('2024-04-01 00:00:00'),
        partition P202404
values LESS THAN ('2024-05-01 00:00:00'),
        partition P202405
values LESS THAN ('2024-06-01 00:00:00'),
        partition P202406
values LESS THAN ('2024-07-01 00:00:00'),
        partition P202407
values LESS THAN ('2024-08-01 00:00:00'),
        partition P202408
values LESS THAN ('2024-09-01 00:00:00'),
        partition P202409
values LESS THAN ('2024-10-01 00:00:00'),
        partition P202410
values LESS THAN ('2024-11-01 00:00:00'),
        partition P202411
values LESS THAN ('2024-12-01 00:00:00'),

        partition P202412
values LESS THAN ('2025-01-01 00:00:00'),
        partition P202501
values LESS THAN ('2025-02-01 00:00:00'),
        partition P202502
values LESS THAN ('2025-03-01 00:00:00'),
        partition P202503
values LESS THAN ('2025-04-01 00:00:00'),
        partition P202504
values LESS THAN ('2025-05-01 00:00:00'),
        partition P202505
values LESS THAN ('2025-06-01 00:00:00'),
        partition P202506
values LESS THAN ('2025-07-01 00:00:00'),
        partition P202507
values LESS THAN ('2025-08-01 00:00:00'),
        partition P202508
values LESS THAN ('2025-09-01 00:00:00'),
        partition P202509
values LESS THAN ('2025-10-01 00:00:00'),
        partition P202510
values LESS THAN ('2025-11-01 00:00:00'),
        partition P202511
values LESS THAN ('2025-12-01 00:00:00'),

        partition P202512
values LESS THAN ('2026-01-01 00:00:00'),
        partition P202601
values LESS THAN ('2026-02-01 00:00:00'),
        partition P202602
values LESS THAN ('2026-03-01 00:00:00'),
        partition P202603
values LESS THAN ('2026-04-01 00:00:00'),
        partition P202604
values LESS THAN ('2026-05-01 00:00:00'),
        partition P202605
values LESS THAN ('2026-06-01 00:00:00'),
        partition P202606
values LESS THAN ('2026-07-01 00:00:00'),
        partition P202607
values LESS THAN ('2026-08-01 00:00:00'),
        partition P202608
values LESS THAN ('2026-09-01 00:00:00'),
        partition P202609
values LESS THAN ('2026-10-01 00:00:00'),
        partition P202610
values LESS THAN ('2026-11-01 00:00:00'),
        partition P202611
values LESS THAN ('2026-12-01 00:00:00'),
        partition PMAXVALUES
values LESS THAN maxvalue
);

create index IAAP_STT_SYSTEM_STATUS_LAST_CHECK_TIME_IDX
	using BTREE on
IAAP_STT_SYSTEM_STATUS (LAST_CHECK_TIME);
-- IAAP_STT_SYSTEM_STATUS -> 시간 통계 rapeech 2023
create table IAAP_STT_SYSTEM_STATUS_STATH
(
    DATE_HOUR VARCHAR(20) collate UTF8MB4_UNICODE_CI not null COMMENT '통계시간',
    SERVER_ID VARCHAR(200) collate UTF8MB4_UNICODE_CI not null COMMENT '서버의 hostname',
    CPU_MX INT default 0 COMMENT 'CPU사용율 최대',
    CPU_MN INT default 0 COMMENT 'CPU사용율 최소',
    CPU_AV INT default 0 COMMENT 'CPU사용율 평균',
    MEM_TT bigint default 0 COMMENT '물리 메모리 사이즈 (mbyte) total',
    MEM_MX bigint default 0 COMMENT 'Free 물리 메모리 사이즈 (mbyte) 최대',
    MEM_MN bigint default 0 COMMENT 'Free 물리 메모리 사이즈 (mbyte) 최소',
    MEM_AV bigint default 0 COMMENT 'Free 물리 메모리 사이즈 (mbyte) 평균',
    STR_TT bigint default 0 COMMENT 'App 파티션의 디스크 사이즈 (mbyte) total',
    STR_MX bigint default 0 COMMENT 'Free App 파티션의 디스크 사이즈 (mbyte) 최대',
    STR_MN bigint default 0 COMMENT 'Free App 파티션의 디스크 사이즈 (mbyte) 최소',
    STR_AV bigint default 0 COMMENT 'Free App 파티션의 디스크 사이즈 (mbyte) 평균',
    LOG_TT bigint default 0 COMMENT '로그 파티션의 디스크 사이즈 (mbyte) total',
    LOG_MX bigint default 0 COMMENT 'Free 로그 파티션의 디스크 사이즈 (mbyte) 최대',
    LOG_MN bigint default 0 COMMENT 'Free 로그 파티션의 디스크 사이즈 (mbyte) 최소',
    LOG_AV bigint default 0 COMMENT 'Free 로그 파티션의 디스크 사이즈 (mbyte) 평균',
    BPS_MX bigint default 0 COMMENT '초당 송/수신 bit 수  (단위 kbps) 최대',
    BPS_MN bigint default 0 COMMENT '초당 송/수신 bit 수  (단위 kbps) 최소',
    BPS_AV bigint default 0 COMMENT '초당 송/수신 bit 수  (단위 kbps) 평균',
    PPS_MX bigint default 0 COMMENT '초당 송/수신 packet 수 (단위 pps) 최대',
    PPS_MN bigint default 0 COMMENT '초당 송/수신 packet 수 (단위 pps) 최소',
    PPS_AV bigint default 0 COMMENT '초당 송/수신 packet 수 (단위 pps) 평균',
    primary key (DATE_HOUR,
SERVER_ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT = '시스템 현황  시간 통계';

create index IAAP_STT_SYSTEM_STATUS_STATH_DATE_HOUR_IDX
	using BTREE on
IAAP_STT_SYSTEM_STATUS_STATH (DATE_HOUR);

create table IAAP_STT_TRAIN_DATASET
(	
   AM_DATASET_ID int(10) not null COMMENT '학습데이터셋 ID', 
	SERVICE_MODEL VARCHAR(20) COMMENT '서비스모델', 
	SERVICE_MODEL_ID int(11) COMMENT '서비스모델ID', 
	DATASET_NAME VARCHAR(50) COMMENT '데이터셋이름', 
	TRAIN_TIME VARCHAR(20) COMMENT '학습시간', 
	DESCRIPTION VARCHAR(100) COMMENT '설명', 
	REG_DT DATETIME default current_timestamp() not null COMMENT '등록일시', 
	REG_ID VARCHAR(60) not null COMMENT '등록자ID', 
	REG_IP VARCHAR(23) not null COMMENT '등록자IP', 
	UPD_DT DATETIME COMMENT '수정일시', 
	UPD_ID VARCHAR(60) COMMENT '수정자ID', 
	UPD_IP VARCHAR(23) COMMENT '수정자IP', 
	USE_YN VARCHAR(1) default 'Y' not null COMMENT '사용여부', 
	DIRECTORY_ID int(11) not null COMMENT 'NAS디렉토리 메뉴에서의 디렉토리ID값',
	primary key (AM_DATASET_ID)
) engine = InnoDB
  default CHARSET = utf8mb4
  collate = utf8mb4_unicode_ci COMMENT = 'STT AM 학습데이터셋 관리';
-- CREATE SEQ
create sequence SEQ_IAAP_STT_TRAIN_DATA
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_TRAIN_AM_DATA
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_SERVICE_MODEL
start with
1 increment by 1;
-- CREATE SEQUENCE SEQ_IAAP_STT_ENGINE_URL START WITH 1 INCREMENT BY 1;
create sequence SEQ_IAAP_STT_VERIFY_DATASET
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_VERIFY_DATA
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_VERIFY_HISTORY
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_LOG
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_DICTATION
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_USAGE
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_DEPLOY_MODEL
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_DEPLOY_MNG
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_TRAIN_MNG
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_CONFIG
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_CALL_INFO
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_RESULT_INFO
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_ERROR_LOG
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_SYSTEM_STATUS
start with
1 increment by 1;

create sequence SEQ_IAAP_STT_TRAIN_DATASET
start with
1 increment by 1;

-- 환경설정 정보 세팅 ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■

-- 초기데이터 생성
INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'Y'
	, 'SCHEDULER_USEYN'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, '0'
	, 'SCEDULER_CYCLE_HWRESOURCE'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, '0'
	, 'SCHEDULER_CYCLE_STATISTICS'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, '0'
	, 'REMOVER_STANDARD'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, '0'
	, 'REMOVER_TIME'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

/* ---------------------------- */
/* 			삭제할 데이터 			*/
/* ---------------------------- */

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_AM_TRAIN_DATA'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_LM_TRAIN_DATA'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_TRAIN_MNG'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_VERIFY_DATA'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_VERIFY_MNG'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_DEPLOY_MODEL'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_DEPLOY_MNG'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_CALLINFO'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_STT_RESULT'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_HW_RESOURCE'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_STATISTICS'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_DATA_TEST'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

/* ---------------------------- */
/* 			삭제할 파일 			*/
/* ---------------------------- */

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_FILE_AM_TRAIN_DATA'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_FILE_DEPLOY_MODEL'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_FILE_VERIFY_DATA'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_FILE_CALLINFO'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'S'
	, 'N'
	, 'REMOVER_FILE_TEST'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

/* ---------------------------- */
/* 		  HW 리소스 임계치  		*/
/* ---------------------------- */
INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'T'
	, '0'
	, 'THRESHOLD_CPU'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'T'
	, '0'
	, 'THRESHOLD_MEMORY'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'T'
	, '0'
	, 'THRESHOLD_STORAGE'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);




/* ---------------------------- */
/* 			AGENT 설정	 		*/
/* ---------------------------- */
INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'A'
	, 'N'
	, 'MULTIPART_SUB_TEST'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'A'
	, 'N'
	, 'MULTIPART_SUB_DEPLOY'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'A'
	, 'N'
	, 'MULTIPART_HOST_TEST'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'A'
	, 'N'
	, 'MULTIPART_HOST_DEPLOY'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY
	, CODE_VALUE
	, DESCRIPTION
	, REG_DT
	, REG_ID
	, REG_IP
	, UPD_DT
	, UPD_ID
	, UPD_IP
	, USE_YN
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'A'
	, 'host'
	, 'SESSION_TARGET'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y');
	
/* ---------------------------- */
/* 			암호화 설정	 			*/
/* ---------------------------- */
INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'P'
	, 'N'
	, 'TEXT_ENCRYPT'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'P'
	, 'N'
	, 'WAV_ENCRYPT'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

/* ---------------------------- */
/* 			기타 설정	 			*/
/* ---------------------------- */
INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'E'
	, '10'
	, 'SCREEN_RECORDS'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);

INSERT INTO IAAP_STT_CONFIG(
	ID
	, CODE_KEY 
	, CODE_VALUE 
	, DESCRIPTION 
	, REG_DT 
	, REG_ID
	, REG_IP 
	, UPD_DT 
	, UPD_ID 
	, UPD_IP 
	, USE_YN 
) VALUES(
	NEXTVAL(SEQ_IAAP_STT_CONFIG)
	, 'E'
	, '0'
	, 'CONFIDENCE_CHART_THRESHOLD'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, CURRENT_DATE
	, 'test'
	, 'test'
	, 'Y'
);