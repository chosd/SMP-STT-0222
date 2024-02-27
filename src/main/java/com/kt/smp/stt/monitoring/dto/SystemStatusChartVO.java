/**
 * 
 */
package com.kt.smp.stt.monitoring.dto;

import org.json.simple.JSONObject;

import lombok.Data;

@Data
public class SystemStatusChartVO {
	private JSONObject cpuData;
	private JSONObject memData;
	private JSONObject strData;
	private JSONObject bpsData;
	private JSONObject ppsData;
}
