/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import lombok.Data;

@Data
public class EtcConfigDto {
	private String recordCount;						// 리스트 Default 레코드 수
	private String confidenceChartThreshold;		// 신뢰도 차트 임계값
}
