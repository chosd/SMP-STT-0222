package com.kt.smp.stt.monitoring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.common.dto.PageParam;

import lombok.Data;

import lombok.extern.slf4j.Slf4j;

/**
*@FileName : SystemStatusSearchRequestDto.java
@Project : kt-tts-service_r
@Date : 2023. 10. 19.
*@작성자 : 심수연
*@변경이력 :
*@프로그램설명 :
*/
@Slf4j
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemStatusSearchRequestDto extends PageParam {
	
	private String searchType;
	private Long serviceId;
	private Long serverId; // searchType == SERVER인 경우에만 참조
	private Long channelId; // searchType == CHANNEL인 경우에만 참조
	private String searchUnit = "MINUTE";
	
	private String startSearchDate; // yyyy-MM-dd HH:mm
	private String endSearchDate; // yyyy-MM-dd HH:mm
	
	private String orderType;
	
    private Boolean isPagingSearch;		// 페이지 별 검색이면 true. 그래프 검색이면 false
	//validation 작성 예정

}
