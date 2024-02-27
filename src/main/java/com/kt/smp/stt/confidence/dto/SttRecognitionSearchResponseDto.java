package com.kt.smp.stt.confidence.dto;

import com.kt.smp.stt.recognition.dto.SttRecognitionVO;
import lombok.Data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

@Data
public class SttRecognitionSearchResponseDto {

    private final List<SttRecognitionVO> chartList;

    private final Map<String, SttRecognitionVO> hashmap;

    private final JSONArray chartData;

    public SttRecognitionSearchResponseDto(List<SttRecognitionVO> recognitionChartData, Map<String, SttRecognitionVO> hashmap, JSONArray chartData) {
        this.chartList = recognitionChartData;
        this.hashmap = hashmap;
        this.chartData = chartData;
    }
}
