/**
 * 
 */
package com.kt.smp.stt.monitoring.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SttSystemStatusChartLegendVO {
	private String serverName;
	private String legendName;
	private Double value;
	private String lastCheckTime;
}
