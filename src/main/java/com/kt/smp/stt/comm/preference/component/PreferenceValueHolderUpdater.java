/**
 * 
 */
package com.kt.smp.stt.comm.preference.component;

import static com.kt.smp.stt.comm.preference.PreferenceValueHolder.*;

import org.springframework.stereotype.Component;

import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;

/**
* @FileName : PreferenceValueHolderUpdater.java
* @Project : stt-smp-service
* @Date : 2024. 2. 14.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@Component
public class PreferenceValueHolderUpdater {

	public void updateSchedulerValueHolder(SchedulerConfigUpdateDto removerSchedulerUpdateDto, String projectCode) {
		schedulerActive.put(projectCode, "Y".equals(removerSchedulerUpdateDto.getSchedulerUseYn()) ? true : false);
		removerTime.put(projectCode, Integer.valueOf(removerSchedulerUpdateDto.getRemoverTime()));
		removerStandard.put(projectCode, Integer.valueOf(removerSchedulerUpdateDto.getRemoverStandard()));
		intervalSystemStatus.put(projectCode, Integer.valueOf(removerSchedulerUpdateDto.getHwResourceCycle()));
		intervalRequestStat.put(projectCode, Integer.valueOf(removerSchedulerUpdateDto.getStatisticsCycle()));
		datasToRemove.put(projectCode, removerSchedulerUpdateDto.getRemoverDataList());
		filesToRemove.put(projectCode, removerSchedulerUpdateDto.getRemoverFileList());
	}
	
	public void updateThresholdValueHolder(ThresholdConfigDto thresholdConfigDto, String projectCode) {
		cpuUsedThreshold.put(projectCode, thresholdConfigDto.getCpu());
		freeMemorySizeThreshold.put(projectCode, thresholdConfigDto.getMemory());
		freeAppStorageSizeThreshold.put(projectCode, thresholdConfigDto.getStorage());
	}
	
	public void updateEtcValueHolder(EtcConfigDto etcConfigDto, String projectCode) {
		recordCount.put(projectCode, Integer.valueOf(etcConfigDto.getRecordCount()));
		confidenceChartThreshold.put(projectCode, Double.valueOf(etcConfigDto.getConfidenceChartThreshold()));
	}
	
	public void updateEncryptValueHolder(EncryptConfigDto encryptConfigDto, String projectCode) {
		textEncrypt.put(projectCode, "Y".equals(encryptConfigDto.getTextEncrypt()) ? true : false);
		wavEncrypt.put(projectCode, "Y".equals(encryptConfigDto.getWavEncrypt()) ? true : false);
	}
	
}
