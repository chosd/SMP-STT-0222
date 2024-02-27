/**
 * 
 */
package com.kt.smp.stt.monitoring.dto;

import com.kt.smp.stt.statistics.domain.SttServerInfoVO;

import lombok.Data;
import lombok.ToString;

/**
*@FileName : SystemStatusSearchResult.java
@Project : kt-stt-service_r
@Date : 2023. 10. 19.
*@작성자 : rapeech
*@변경이력 :
*@프로그램설명 :
*/
@Data
@ToString(callSuper = true)
public class SystemStatusSearchResult extends SttServerInfoVO{
	
	private String groupByDate; // 통계 검색 일시 (검색 단위대로 groupby된 결)

	private Long serviceId;
	private String serviceName;

	private Long channelId;
	private String channelName;

//	private ProcessType processType; // 검색 조건대로 세팅

	private int totalCount;

	// 검색 기준이 채널인 경우, rowspan 병합을 위해 카운팅
	private int rowIndex = 0;
	private int rowCount = 1;
}
