/*
create database smp_master  default character set utf8;
create user smp_master@'%' identified by 'admin';
grant all privileges on smp_master.* to smp_master@'%';
*/

-- DROP SEQ
DROP SEQUENCE IF EXISTS SEQ_IAAP_SERVICE_CONFIG;

-- DROP TABLE
DROP TABLE IF EXISTS IAAP_SERVICE_CONFIG;

-- CREATE TABLE
CREATE TABLE IAAP_SERVICE_CONFIG
(
    ID                    INT          NOT NULL COMMENT '서비스 설정 ID',
    SITE_CODE             VARCHAR(100) NOT NULL COMMENT '사이트 코드',
    PROJECT_CODE          VARCHAR(100) NOT NULL COMMENT '프로젝트 코드',
    ENGINE_PARAMETER      VARCHAR(100) NOT NULL COMMENT '엔진 파라미터',
    DB_DRIVER_CLASS_NAME  VARCHAR(100) NOT NULL COMMENT 'DB 드라이버 클래스명',
    DB_URL                VARCHAR(500) NOT NULL COMMENT 'DB URL',
    DB_USERNAME           VARCHAR(100) NOT NULL COMMENT 'DB 사용자ID',
    DB_PASSWORD           VARCHAR(100) NOT NULL COMMENT 'DB 비밀번호',
    SMP_TYPE              VARCHAR(100) COMMENT 'SMP TYPE',
    IS_DEFAULT            VARCHAR(1)   NOT NULL DEFAULT 'N' COMMENT '기본 설정 여부',
    REG_DT                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    REG_ID                VARCHAR(100) NOT NULL COMMENT '등록자 ID',
    REG_IP                VARCHAR(100) NOT NULL COMMENT '등록자 IP',
    UPD_DT                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
    UPD_ID                VARCHAR(100) NOT NULL COMMENT '수정자 ID',
    UPD_IP                VARCHAR(100) NOT NULL COMMENT '수정자 IP',
    USE_YN                VARCHAR(1)   DEFAULT 'Y' COMMENT '사용여부',
    PRIMARY KEY (ID)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '서비스 연동 정보';

CREATE SEQUENCE SEQ_IAAP_SERVICE_CONFIG START WITH 1 INCREMENT BY 1;

-- DROP TABLE
drop table if exists SHEDLOCK;

-- CREATE TABLE
CREATE TABLE shedlock (
  NAME varchar(64) NOT NULL COMMENT '스케줄잠금이름',
  LOCK_UNTIL timestamp(3) NULL DEFAULT NULL COMMENT '잠금기간',
  LOCKED_AT timestamp(3) NULL DEFAULT NULL COMMENT '잠금일시',
  LOCKED_BY varchar(255) NULL DEFAULT NULL COMMENT '잠금신청자',
  PRIMARY KEY (NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
collate = utf8mb4_unicode_ci COMMENT '스케줄 잠금처리 관리';


/* for stt mater database */
INSERT INTO IAAP_SERVICE_CONFIG
	(`ID`, `SITE_CODE`, `PROJECT_CODE`, `ENGINE_PARAMETER`
	, `DB_DRIVER_CLASS_NAME`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
	, `IS_DEFAULT`, `REG_ID`, `REG_IP`, `UPD_ID`, `UPD_IP`)
VALUES(NEXTVAL(SEQ_IAAP_SERVICE_CONFIG), 'SITE', 'PROJECT', 'ENGINE'
	, 'org.mariadb.jdbc.Driver', 'jdbc:mariadb://localhost:3306/smp_master?characterEncoding=UTF-8&allowMultiQueries=true'
	, 'smp_master', 'x+CJdjpWw3eXDf3QH4Xqog=='
	, 'Y', 'ID', '127.0.0.1', 'DT', '127.0.0.1');