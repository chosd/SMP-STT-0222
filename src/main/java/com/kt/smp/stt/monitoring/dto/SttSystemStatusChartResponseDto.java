/**
 * 
 */
package com.kt.smp.stt.monitoring.dto;

import org.json.simple.JSONArray;

import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;

import lombok.Data;

@Data
public class SttSystemStatusChartResponseDto {
    private JSONArray chartData;
    private ThresholdConfigDto hwResourcethreshold;
    
    public SttSystemStatusChartResponseDto(JSONArray chartData, ThresholdConfigDto thresholdConfigDto) {
    	this.chartData = chartData;
    	this.hwResourcethreshold = thresholdConfigDto;
    }
}
