/**
 * 
 */
package com.kt.smp.stt.operate.session.dto;

import com.kt.smp.stt.operate.session.enums.SessionSortCondition;

import lombok.Data;

@Data
public class SessionRequestDto {
	
	private SessionSortCondition firstCondition;
	private SessionSortCondition secondCondition;
	
	
	private String regId;
	private String regIp;
}
