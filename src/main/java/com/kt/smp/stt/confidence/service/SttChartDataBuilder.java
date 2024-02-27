package com.kt.smp.stt.confidence.service;

import com.kt.smp.stt.confidence.domain.SttConfidenceSearchCondition;
import com.kt.smp.stt.confidence.domain.SttConfidenceVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class SttChartDataBuilder {
	
    public JSONObject build(List<SttConfidenceVO> source, SttConfidenceSearchCondition searchCondition) {
    	JSONObject chartData = new JSONObject();
        JSONArray chartDatasets = new JSONArray();

        List<String> colors = colorList();
        int colorIndex = 0;
        
        List<String> modelNameList = new ArrayList<>();
        
        // 기준이 되는 서비스 모델명 리스트
		for(SttConfidenceVO item : source) {
			if (!modelNameList.contains(item.getServiceModelName())) {
				modelNameList.add(item.getServiceModelName());
			}
		}
        
		for(String modelName : modelNameList) {
			JSONObject dataObject = new JSONObject();
			JSONArray data = new JSONArray();
			// 서비스 모델별로 데이터 셋을 구분하기 위한 이전 모델명 저장 변수
			String prevModel = modelName;
			
			for(SttConfidenceVO item : source) {
	        	JSONObject obj = new JSONObject();
	        	if(modelName.equals(item.getServiceModelName())) {
	        		obj.put("x", item.getStartAt());
		        	obj.put("y", item.getAvgCer());
		        	data.add(obj);
	        	}
	        }
			dataObject.put("borderColor",colors.get(colorIndex++));
			dataObject.put("backgroundColor",colors.get(colorIndex++));
            colorIndex %= colors.size();
			dataObject.put("label", prevModel);
	        dataObject.put("data", data);
	        chartDatasets.add(dataObject);
		}
		
        chartData.put("datasets", chartDatasets);
        
        return chartData;
    }

    private List<String> colorList(){
        List<String> colors = new ArrayList<>();
        colors.add("rgba(244, 162, 97, 0.7)");
        colors.add("rgba(42, 157, 143, 0.7)");
        colors.add("rgba(42, 157, 143, 0.5)");
        colors.add("rgba(231, 111, 81, 0.7)");
        colors.add("rgba(231, 111, 81, 0.5)");
        colors.add("rgba(244, 162, 97, 0.5)");
        colors.add("rgba(3, 4, 94, 0.7)");
        colors.add("rgba(3, 4, 94, 0.5)");
        colors.add("rgba(58, 134, 255, 0.7)");
        colors.add("rgba(58, 134, 255, 0.5)");

        return colors;
    }
}
