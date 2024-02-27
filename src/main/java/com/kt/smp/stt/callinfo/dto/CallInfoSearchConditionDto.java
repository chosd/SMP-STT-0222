package com.kt.smp.stt.callinfo.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.kt.smp.common.dto.PageParam;

import lombok.Data;

@Data
public class CallInfoSearchConditionDto extends PageParam {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String startDate; 		  // 조회 시작일
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;		  // 조회 종료일
	 
	private String serviceCode;
	private String callStatus;
	
	private String searchKeyword;	  // 검색어
	
	private String searchKeywordType; // 검색어 타입
}
