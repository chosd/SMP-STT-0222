package com.kt.smp.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author search1
 * @title BaseModel
 * @see
 * @ Desc 각 DB 테이블의 공통 컬럼 지정 클래스
 * @since 2022/02/16
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BaseModel implements Serializable {

	protected Long id;
	protected String useYn;
	protected String regDt;
	protected String regId;
	protected String regIp;
	protected String updDt;
	protected String updId;
	protected String updIp;
	
	// 221012 신우성 등록/수정자에 사번 -> 성명(사번) 표시에 사용할 데이터셋 추가
	/* 사용자 명 */
	protected String korNm;
	// 수정 끝
	
}
