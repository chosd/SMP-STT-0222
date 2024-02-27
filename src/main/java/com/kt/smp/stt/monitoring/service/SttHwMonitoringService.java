/**
 * 
 */
package com.kt.smp.stt.monitoring.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.monitoring.chart.SttHwResourceChartBuilder;
import com.kt.smp.stt.monitoring.dto.SttSystemStatusChartResponseDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SttHwMonitoringService {

	public SttSystemStatusChartResponseDto createHwMonitoringChart(List<SystemStatusSearchResult> resultList) {
		
		JSONArray chartData = SttHwResourceChartBuilder.build(resultList);

		String projectCode = TenantContextHolder.getProjectCode();
		ThresholdConfigDto thresholdConfigDto = new ThresholdConfigDto(
					PreferenceValueHolder.cpuUsedThreshold.get(projectCode),
					PreferenceValueHolder.freeMemorySizeThreshold.get(projectCode),
					PreferenceValueHolder.freeAppStorageSizeThreshold.get(projectCode)
				);
		
		return new SttSystemStatusChartResponseDto(chartData, thresholdConfigDto);
	}

}
