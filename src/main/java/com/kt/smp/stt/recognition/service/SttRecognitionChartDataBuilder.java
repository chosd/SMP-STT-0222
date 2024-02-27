/**
 * 
 */
package com.kt.smp.stt.recognition.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kt.smp.stt.recognition.dto.SttRecognitionDetailVO;
import com.kt.smp.stt.recognition.dto.SttRecognitionSearchCondition;
import com.kt.smp.stt.recognition.dto.SttRecognitionVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SttRecognitionChartDataBuilder {

    public static JSONArray build(List<SttRecognitionVO> source, SttRecognitionSearchCondition searchCondition) {
    	JSONArray chartData = new JSONArray();

        List<String> colors = colorList();
        List<String> pointStyles = pointStyleList();
        Map<Integer, String> datasetIdMap = new HashMap<>();
        List<String> serviceCodeList = new ArrayList<>();

        
        for (SttRecognitionVO item : source) {

    		if (!serviceCodeList.contains(item.getServiceCode())) {
    			serviceCodeList.add(item.getServiceCode());
    		}
    		
        	for (SttRecognitionDetailVO detailItem : item.getSttRecognitionDetailList()) {
        		Integer datasetId = detailItem.getDatasetId();
        		String datasetName = detailItem.getDatasetName(); 
        		
        		if(!datasetIdMap.containsKey(datasetId)) {
        			datasetIdMap.put(datasetId, datasetName);
        		}
        	}
        }
    	
        for (SttRecognitionVO item : source) {
            Map<Integer, String> addedDataset = new HashMap<>();

        	JSONObject serviceModelChartObj = new JSONObject();
        	
        	JSONArray dataSetArray = new JSONArray();
        	
        	Iterator<Map.Entry<Integer, String>> iterator = datasetIdMap.entrySet().iterator();
        	int colorIndex = 0;
        	int pointIndex = 0;
        	while (iterator.hasNext()) {
                
        		Map.Entry<Integer, String> entry = iterator.next();
        		Integer datasetId = entry.getKey();
        		String datasetName = entry.getValue();
        		
        		if(addedDataset.containsKey(datasetId)) {
        			continue;
        		}
        		
        		addedDataset.put(datasetId, datasetName);
        		
        		JSONObject dataSetObj = new JSONObject();
        		JSONArray xyDataArr = new JSONArray();
        		int tempI = 0;
            	for (SttRecognitionDetailVO detailItem : item.getSttRecognitionDetailList()) {
            		//log.info(">>>>> current datasetId : "+datasetId+" | detailItem.getDatasetId : "+detailItem.getDatasetId());
            		JSONObject xyData = new JSONObject();
            		
            		if (datasetId.equals(detailItem.getDatasetId())) {
            			
	            		xyData.put("x", detailItem.getResultModelId());
	            		xyData.put("y", detailItem.getAvgCer());
	            		//log.info(">>>>> xyData resultModelId : "+xyData.get("x")+" | cer : "+xyData.get("y"));
	            		xyDataArr.add(xyData);
            		}
            		tempI++;
            	}
            	
            	if (xyDataArr.size() > 0) {
            		dataSetObj.put("data", xyDataArr);
            		dataSetObj.put("backgroundColor",colors.get(colorIndex++));
            		dataSetObj.put("pointStyle", pointStyles.get(pointIndex++));
            		dataSetObj.put("pointRadius", 5);
            		dataSetObj.put("pointHoverRadius", 7);
                	dataSetObj.put("label", datasetName);
                	
                	dataSetArray.add(dataSetObj);
                	colorIndex %= colors.size();
                	pointIndex %= pointStyles.size();
            	}

        	}
        	
        	serviceModelChartObj.put("datasets", dataSetArray);
        	serviceModelChartObj.put("serviceCode", item.getServiceCode());
        	serviceModelChartObj.put("serviceModelName", item.getServiceModelName());
        	chartData.add(serviceModelChartObj);
        }
       
        return chartData;
    }

	private static List<String> colorList(){
        List<String> colors = new ArrayList<>();
        colors.add("rgba(0, 153, 204, 0.7)");
        colors.add("rgba(51, 204, 51, 0.7)");
        colors.add("rgba(255, 0, 255, 0.7)");
        colors.add("rgba(255, 80, 80, 0.7)");
        colors.add("rgba(3, 4, 94, 0.7)");
        colors.add("rgba(153, 102, 255, 0.7)");

        return colors;
    }
    
    private static List<String> pointStyleList(){
        List<String> pointStyleList = new ArrayList<>();
        pointStyleList.add("triangle");
        pointStyleList.add("circle");
        pointStyleList.add("rect");
        pointStyleList.add("rectRounded");
        pointStyleList.add("recRot");
        return pointStyleList;
    }
    
}
