package com.kt.smp.stt.statistics.component;

import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchCondition;
import com.kt.smp.stt.statistics.domain.SttStatisticsVO;

import lombok.extern.log4j.Log4j2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@Component
public class SttStatisticsChartDataBuilder {

    private final static String REQUEST = "(요청)";
    private final static String SUCCESS = "(성공)";

    private ServiceModelService serviceModelService;
    
	public SttStatisticsChartDataBuilder(ServiceModelService serviceModelService) {
    	this.serviceModelService = serviceModelService;
	}
    
    public JSONObject build(List<SttStatisticsVO> source, SttStatisticsSearchCondition searchCondition) {
        Map<String, List<Map<String, Object>>> chartDataByCondition = new HashMap<>();
        Set<String> dateLabelSet = new HashSet<>();
        Set<String> conditionSet =  new HashSet<>();
        // 데이터 생성
        makeChartDataByService2(source
                , conditionSet
                , chartDataByCondition
                , serviceModelService.detailByServiceCode(searchCondition.getServiceCode()).getServiceModelName());

        // 화면에 넘겨줄 JSON 데이터 생성
        return makeChartDataFromRawDataV2(dateLabelSet, conditionSet, chartDataByCondition);
    }


    public JSONObject build(Map<String, Page<SttStatisticsVO>> hashMap){
        Map<String, List<Integer>> chartDataByCondition = new HashMap<>();
        Set<String> dateLabelSet = new HashSet<>();
        Set<String> conditionSet = new HashSet<>();

        Map<String, String> serviceNames = new HashMap<>();
        for (ServiceModelVO serviceModelVO : serviceModelService.listAll()) {
            serviceNames.put(serviceModelVO.getServiceCode(), serviceModelVO.getServiceModelName());
        }

        // 서비스별로 데이터 생성
        for (String serviceModelCode : hashMap.keySet()){
            makeChartDataByService(hashMap.get(serviceModelCode).getResult()
                    , dateLabelSet
                    , conditionSet
                    , chartDataByCondition
                    , serviceNames.get(serviceModelCode));
        }

        // 화면에 넘겨줄 JSON 데이터 생성
        return makeChartDataFromRawData(dateLabelSet, conditionSet, chartDataByCondition);
    }
    
    public JSONObject buildByHashmap(Map<String, List<SttStatisticsVO>> hashMap){
        Map<String, List<Map<String, Object>>> chartDataByCondition = new HashMap<>();
        Set<String> dateLabelSet = new HashSet<>();
        Set<String> conditionSet = new HashSet<>();

        Map<String, String> serviceNames = new HashMap<>();
        for (ServiceModelVO serviceModelVO : serviceModelService.listAll()) {
            serviceNames.put(serviceModelVO.getServiceCode(), serviceModelVO.getServiceModelName());
        }

        // 서비스별로 데이터 생성
        for (String serviceModelCode : hashMap.keySet()){
            makeChartDataByService2(hashMap.get(serviceModelCode)
                    , conditionSet
                    , chartDataByCondition
                    , serviceNames.get(serviceModelCode));
        }

        // 화면에 넘겨줄 JSON 데이터 생성
        return makeChartDataFromRawDataV2(dateLabelSet, conditionSet, chartDataByCondition);
    }


    private List<String> colorList(){
        List<String> colors = new ArrayList<>();
        colors.add("rgba(244, 162, 97, 0.7)");
        colors.add("rgba(11, 93, 158, 0.5)");
        colors.add("rgba(42, 157, 143, 0.7)");
        colors.add("rgba(213, 98, 112, 0.5)");
        colors.add("rgba(231, 111, 81, 0.7)");
        colors.add("rgba(24, 144, 174, 0.5)");
        colors.add("rgba(3, 4, 94, 0.7)");
        colors.add("rgba(252, 251, 161, 0.5)");
        colors.add("rgba(58, 134, 255, 0.7)");
        colors.add("rgba(197, 121, 0, 0.5)");

        return colors;
    }

    private void makeChartDataByService(List<SttStatisticsVO> source
            , Set<String> dateLabelSet
            , Set<String> conditionSet
            , Map<String, List<Integer>> chartDataByCondition
            , String conditionName){

        for(SttStatisticsVO item: source) {
            dateLabelSet.add(item.getRegDt());

            if (!conditionSet.contains(conditionName)) {
                conditionSet.add(conditionName);
                chartDataByCondition.put(conditionName + REQUEST, new ArrayList<>());
                chartDataByCondition.put(conditionName + SUCCESS, new ArrayList<>());
            }

            if (item.getRequestCount()==null){ item.setRequestCount(0); }
            if (item.getCompleteCount()==null){ item.setCompleteCount(0); }
            
            chartDataByCondition.get(conditionName + REQUEST).add(item.getRequestCount());
            chartDataByCondition.get(conditionName + SUCCESS).add(item.getCompleteCount());
        }
    }
    
    private void makeChartDataByService2(List<SttStatisticsVO> source
            , Set<String> conditionSet
            , Map<String, List<Map<String, Object>>> chartDataByCondition
            , String conditionName){

        for(SttStatisticsVO item: source) {
            if (!conditionSet.contains(conditionName)) {
                conditionSet.add(conditionName);
                chartDataByCondition.put(conditionName + REQUEST, new ArrayList<>());
                chartDataByCondition.put(conditionName + SUCCESS, new ArrayList<>());
            }

            if (item.getRequestCount()==null){ item.setRequestCount(0); }
            if (item.getCompleteCount()==null){ item.setCompleteCount(0); }
            
            
            Map<String, Object> requestCountMap = new HashMap<>();
            requestCountMap.put("x", item.getRegDt());
            requestCountMap.put("y", item.getRequestCount());
            Map<String, Object> successCountMap = new HashMap<>();
            successCountMap.put("x", item.getRegDt());
            successCountMap.put("y", item.getCompleteCount());
            chartDataByCondition.get(conditionName + REQUEST).add(requestCountMap);
            chartDataByCondition.get(conditionName + SUCCESS).add(successCountMap);
        }
    }

    private JSONObject makeChartDataFromRawData(Set<String> dateLabelSet, Set<String> conditionSet, Map<String, List<Integer>> chartDataByCondition){
        JSONObject chartData = new JSONObject();
        JSONArray chartDatasets = new JSONArray();

        // 날짜별 라벨 생성 및 정렬
        List<String> dateLabels = new ArrayList<>(dateLabelSet);
        Collections.sort(dateLabels);

        List<String> colors = colorList();
        int colorIndex = 0;

        chartData.put("backgroundColor", colors);
        chartData.put("labels", dateLabels);

        String[] ReqSuc = {REQUEST, SUCCESS};
        for(Object item : conditionSet){
            for(int i=0; i<2; i++){
                JSONObject tmp = new JSONObject();

                tmp.put("label",item + ReqSuc[i]);

                Collections.reverse(chartDataByCondition.get(item + ReqSuc[i]));

                tmp.put("data",chartDataByCondition.get(item + ReqSuc[i]));

                tmp.put("backgroundColor",colors.get(colorIndex));
                tmp.put("borderColor", colors.get(colorIndex++));
                colorIndex %= colors.size();

                chartDatasets.add(tmp);
            }

        }
        chartData.put("datasets", chartDatasets);

        return chartData;
    }
    
    private JSONObject makeChartDataFromRawDataV2(Set<String> dateLabelSet, Set<String> conditionSet, Map<String, List<Map<String, Object>>> chartDataByCondition){
        JSONObject chartData = new JSONObject();
        JSONArray chartDatasets = new JSONArray();
        
        // 날짜별 라벨 생성 및 정렬
        List<String> dateLabels = new ArrayList<>(dateLabelSet);
        Collections.sort(dateLabels);

        List<String> colors = colorList();
        int colorIndex = 0;

        chartData.put("backgroundColor", colors);
        chartData.put("labels", dateLabels);

        String[] ReqSuc = {REQUEST, SUCCESS};
        for(Object item : conditionSet){
            for(int i=0; i<2; i++){
                JSONObject tmp = new JSONObject();

                tmp.put("label",item + ReqSuc[i]);

                Collections.reverse(chartDataByCondition.get(item + ReqSuc[i]));

                tmp.put("data",chartDataByCondition.get(item + ReqSuc[i]));

                tmp.put("backgroundColor",colors.get(colorIndex));
                tmp.put("borderColor", colors.get(colorIndex++));
                colorIndex %= colors.size();
                tmp.put("pointRadius", 2);

                chartDatasets.add(tmp);
            }

        }
        chartData.put("datasets", chartDatasets);

        return chartData;
    }
    
    public Map<String, List<SttStatisticsVO>> getChartHashmap(List<SttStatisticsVO> list) {
    	
        Map<String, List<SttStatisticsVO>> serviceCodeChartMap = new HashMap<>();

		for (ServiceModelVO serviceModel : serviceModelService.listAll()) {
			String serviceCode = serviceModel.getServiceCode();
			List<SttStatisticsVO> tempList = new ArrayList<>();
			for (SttStatisticsVO vo : list) {
				String statisticsServiceCode = vo.getServiceCode();
				if (statisticsServiceCode == null) {
					continue;
				}
				if (statisticsServiceCode.equals(serviceCode)) {
					tempList.add(vo);
				}
			}
			serviceCodeChartMap.put(serviceCode, tempList);
		}

        return serviceCodeChartMap;
    }
}
