/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import lombok.Data;

@Data
public class OperateConfigDto {
	private String firstSortCondition;			// 첫 번째 세션 정렬 조건
	private String secondSortCondition;			// 두 번째 세션 정렬 조건
}
