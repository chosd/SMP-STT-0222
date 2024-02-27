/**
 * 
 */
package com.kt.smp.stt.monitoring.dto;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;

import lombok.Getter;
import lombok.Setter;
/**
*@FileName : SystemStatusSearchResponseDto.java
@Project : kt-stt-service_r
@Date : 2023. 10. 19.
*@작성자 : rapeech
*@변경이력 :
*@프로그램설명 :
*/
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemStatusSearchResponseDto extends PageResponse {

		private final Page<SystemStatusSearchResult> result;
		private List<SystemStatusChartVO> chartList;
		private SttSystemStatusChartResponseDto chartResponse;
		private JSONObject cpuData;
		private JSONObject memData;
		private JSONObject strData;
		private JSONObject logData;
		private JSONObject bpsData;
		private JSONObject ppsData;
		
		private JSONArray customLegend;
		private JSONObject tooltipData;
		private String orderType;
		private String reqStatSearchType;
		private String lastchecktime;
		
		
		public SystemStatusSearchResponseDto(Page<SystemStatusSearchResult> result) {
			this.result = result;

			if(result != null) setPageInfo();
		}
		
		public void setPageInfo() {
			setPageNum(result.getPageNum());
			setPages(result.getPages());
			setTotal(result.getTotal());
			setPageSize(result.getPageSize());
		}
		
}
