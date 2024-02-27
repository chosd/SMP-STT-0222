package com.kt.smp.stt.confidence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;
import com.kt.smp.stt.confidence.domain.SttConfidenceVO;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttConfidenceSearchResponseDto extends PageResponse {

    private final List<SttConfidenceVO> result;

    private final Map<String, SttConfidenceVO> hashmap;
    private final JSONObject chartData;

    public SttConfidenceSearchResponseDto(List<SttConfidenceVO> confidenceChartData, Map<String, SttConfidenceVO> hashmap, JSONObject chartData) {
        this.result = confidenceChartData;
        this.hashmap = hashmap;
        this.chartData = chartData;
    }
}
