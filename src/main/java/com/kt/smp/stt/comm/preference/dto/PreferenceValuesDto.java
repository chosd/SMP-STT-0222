/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreferenceValuesDto {
	
	private SchedulerConfigDto schedulerConfig;
	private ThresholdConfigDto thresholdConfig;
	private AgentConfigDto agentConfig;
	private EtcConfigDto etcConfig;
	private EncryptConfigDto encryptConfig;
//	private OperateConfigDto operateConfigDto;
	
}
