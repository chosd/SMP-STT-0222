/**
 * 
 */
package com.kt.smp.stt.monitoring.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.Page;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.monitoring.dto.SttSystemStatusChartResponseDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusChartVO;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchRequestDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResponseDto;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResult;
import com.kt.smp.stt.monitoring.service.SttHwMonitoringService;
import com.kt.smp.stt.statistics.service.SttSystemStatusService;

import lombok.RequiredArgsConstructor;

/**
*@FileName : TtsStatusStatApiController.java
@Project : kt-stt-service_r
@Date : 2023. 10. 18.
*@작성자 : rapeech
*@변경이력 :
*@프로그램설명 : 
*/
@Controller
@RequestMapping("${smp.service.uri.prefix}/monitoring/api")
@RequiredArgsConstructor
public class SttStatusStatApiController {
	
	private final SttSystemStatusService sttSystemStatusService;
	private final SttHwMonitoringService sttHwMonitoringService;
	
	@SmpServiceApi(name = "HW 리소스 정보 검색", method = RequestMethod.GET, path = "/hwResource/listPage", type = "검색", description = "HW 리소스 정보 검색")
	public ResponseEntity<SystemStatusSearchResponseDto> requestStatListPage(
		@ModelAttribute SystemStatusSearchRequestDto searchRequestDto) {

		searchRequestDto.setPage(searchRequestDto.getPage());
		searchRequestDto.setPageSize(searchRequestDto.getPageSize());
		searchRequestDto.setOffset(searchRequestDto.calOffset());

		Page<SystemStatusSearchResult> page = sttSystemStatusService.listPage(searchRequestDto);
        
		SystemStatusSearchResponseDto searchResponseDto = new SystemStatusSearchResponseDto(page);
		//requestStatData(searchResponseDto);
		searchResponseDto.setReqStatSearchType(searchRequestDto.getSearchType());
		
		if (!searchRequestDto.getIsPagingSearch() && !searchRequestDto.getSearchUnit().equals("RAW")) {
			searchRequestDto.setDoNotPaging(true);
			List<SystemStatusSearchResult> chartResult = sttSystemStatusService.listPage(searchRequestDto).getResult();
			SttSystemStatusChartResponseDto chartResponse = sttHwMonitoringService.createHwMonitoringChart(chartResult);
			searchResponseDto.setChartResponse(chartResponse);
		}
        

		return ResponseEntity.ok(searchResponseDto);
		
	}
	
	@SmpServiceApi(name = "HW 리소스 실시간 데이터", method = RequestMethod.GET, path = "/hwResource/nowData", type = "검색", description = "HW 리소스 실시간 데이터")
	public ResponseEntity<SystemStatusSearchResponseDto> requestStatData(){
		
		SystemStatusSearchResponseDto searchResponseDto = new SystemStatusSearchResponseDto(null);
		requestStatData(searchResponseDto);
		
		return ResponseEntity.ok(searchResponseDto);
		
	}
	
	@SmpServiceApi(name = "HW 리소스 이력 그래프", method = RequestMethod.GET, path = "/hwResource/realtimeData", type = "검색", description = "HW 리소스 이력")
	public ResponseEntity<BaseResponseDto<SttSystemStatusChartResponseDto>> hwResourceChart(
			@ModelAttribute SystemStatusSearchRequestDto searchRequestDto){
		
        BaseResponseDto<SttSystemStatusChartResponseDto> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        List<SystemStatusSearchResult> realtimeData = sttSystemStatusService.getRealtimeChart();
        SttSystemStatusChartResponseDto chartResponse = sttHwMonitoringService.createHwMonitoringChart(realtimeData);
        responseDto.setResult(chartResponse);
		return ResponseEntity.ok(responseDto);
		
	}
					
	private void requestStatData(SystemStatusSearchResponseDto searchResponseDto) {
		List<SystemStatusSearchResult> lastData = sttSystemStatusService.lastData();
		List<SystemStatusChartVO> chartList = new ArrayList<>();
		
		// 1분 이전 데이터면 값들 0(수협 기준 소스)
        //SimpleDateFormat lastCheckTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		for(SystemStatusSearchResult data : lastData) {
			// 1분 이전 데이터면 값들 0(수협 기준 소스)
			
			SystemStatusChartVO chart = new SystemStatusChartVO();
			chart.setCpuData(SttSystemChartBuilder.buildCpuData(data));
			chart.setMemData(SttSystemChartBuilder.buildMemData(data));
			chart.setStrData(SttSystemChartBuilder.buildStrData(data));
			chart.setBpsData(SttSystemChartBuilder.buildBpsData(data));
			chart.setPpsData(SttSystemChartBuilder.buildPpsData(data));
			chartList.add(chart);
		}
		searchResponseDto.setChartList(chartList);
		
		if(lastData != null && lastData.size() > 0)
			searchResponseDto.setLastchecktime(lastData.get(0).getLastCheckTime());
		else 
			searchResponseDto.setLastchecktime("");
		
	}
	
// 수협 기준 시간 계산 소스
	/*
	 * private static Date getCurrentDateIn24HourFormat() { SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
	 * formattedDate = dateFormat.format(new Date());
	 * 
	 * try { return dateFormat.parse(formattedDate); } catch (ParseException e) {
	 * e.printStackTrace(); return null; } }
	 */
	
	private static class SttSystemChartBuilder
	{

		private static List<String> COLOR_LIST = new ArrayList<>();
		static {
			COLOR_LIST.add("rgba(244, 162, 97, 0.7)");
			COLOR_LIST.add("rgba(244, 162, 97, 0.5)");
			COLOR_LIST.add("rgba(42, 157, 143, 0.7)");
			COLOR_LIST.add("rgba(42, 157, 143, 0.5)");
			COLOR_LIST.add("rgba(231, 111, 81, 0.7)");
			COLOR_LIST.add("rgba(231, 111, 81, 0.5)");
			COLOR_LIST.add("rgba(3, 4, 94, 0.7)");
			COLOR_LIST.add("rgba(3, 4, 94, 0.5)");
			COLOR_LIST.add("rgba(58, 134, 255, 0.7)");
			COLOR_LIST.add("rgba(58, 134, 255, 0.5)");		
		}
		
		public static JSONObject buildCpuData(SystemStatusSearchResult item) {
			JSONObject chartDataToJSP = new JSONObject();
			List<String> serviceLabels = new ArrayList<>();
			
			Set<String> dateLabelSet = new HashSet<>();
			Set<String> conditionSet = new HashSet<>();

			// 넘겨줄 JSON 데이터
			JSONArray chartDatasets = new JSONArray();
			int colorIndex = 0;
			JSONArray cpuList = new JSONArray();
			JSONArray labels = new JSONArray();
			

				JSONObject tmp = new JSONObject();
				cpuList.add(item.getCpuUsed());
				labels.add(item.getServerName());
				
				tmp.put("data", cpuList);
				tmp.put("label", "CPU Used");
				
				colorIndex += 2;
				colorIndex %= COLOR_LIST.size();
				tmp.put("backgroundColor", COLOR_LIST.get(colorIndex));
				
				chartDatasets.add(tmp);
			
			
			chartDataToJSP.put("datasets", chartDatasets);
			chartDataToJSP.put("labels", labels);

			return chartDataToJSP;
		}
		
		public static JSONObject buildMemData(SystemStatusSearchResult item) {
			JSONObject chartDataToJSP = new JSONObject();
			List<String> serviceLabels = new ArrayList<>();
			
			Set<String> dateLabelSet = new HashSet<>();
			Set<String> conditionSet = new HashSet<>();
			
			// 넘겨줄 JSON 데이터
			JSONArray chartDatasets = new JSONArray();
			int colorIndex = 1;
			JSONArray memList = new JSONArray();
			JSONArray labels = new JSONArray();
			
//			for(SystemStatusSearchResult item : items) {
				JSONObject tmp = new JSONObject();
				labels.add(item.getServerName());
				float usedMem = 0;
				if(item.getMaxMemorySize() != 0) {
					usedMem = item.getMaxMemorySize() - item.getFreeMemorySize();
					usedMem = (int)(usedMem * 1000 / item.getMaxMemorySize());
					usedMem = (float)(usedMem / 10);
				}
				memList.add(usedMem);
				
				tmp.put("data", memList);
				tmp.put("label", "Memory Used");
				
				colorIndex += 2;
				colorIndex %= COLOR_LIST.size();
				tmp.put("backgroundColor", COLOR_LIST.get(colorIndex));
				
				chartDatasets.add(tmp);
//			}
			
			chartDataToJSP.put("datasets", chartDatasets);
			chartDataToJSP.put("labels", labels);
			
			return chartDataToJSP;
		}
		
		public static JSONObject buildStrData(SystemStatusSearchResult item) {
			JSONObject chartDataToJSP = new JSONObject();
			List<String> serviceLabels = new ArrayList<>();
			
			Set<String> dateLabelSet = new HashSet<>();
			Set<String> conditionSet = new HashSet<>();
			
			// 넘겨줄 JSON 데이터
			JSONArray chartDatasets = new JSONArray();
			int colorIndex = 2;
			JSONArray strList = new JSONArray();
			JSONArray labels = new JSONArray();
			
//			for(SystemStatusSearchResult item : items) {
				JSONObject tmp = new JSONObject();
				labels.add(item.getServerName());
				float usedStr = 0;
				if(item.getMaxAppStorageSize() != 0) {
					usedStr = item.getMaxAppStorageSize() - item.getFreeAppStorageSize();
					usedStr = (int)(usedStr * 1000 / item.getMaxAppStorageSize());
					usedStr = (float)(usedStr / 10);
				}
				strList.add(usedStr);
				
				tmp.put("data", strList);
				tmp.put("label", "Storage Used");
				
				colorIndex += 2;
				colorIndex %= COLOR_LIST.size();
				tmp.put("backgroundColor", COLOR_LIST.get(colorIndex));
				
				chartDatasets.add(tmp);
//			}
			
			chartDataToJSP.put("datasets", chartDatasets);
			chartDataToJSP.put("labels", labels);
			
			return chartDataToJSP;
		}
		
		public static JSONObject buildBpsData(SystemStatusSearchResult item) {
			JSONObject chartDataToJSP = new JSONObject();
			List<String> serviceLabels = new ArrayList<>();
			
			Set<String> dateLabelSet = new HashSet<>();
			Set<String> conditionSet = new HashSet<>();
			
			// 넘겨줄 JSON 데이터
			JSONArray chartDatasets = new JSONArray();
			int colorIndex = 3;
			JSONArray bpsList = new JSONArray();
			JSONArray labels = new JSONArray();
			
//			for(SystemStatusSearchResult item : items) {
				JSONObject tmp = new JSONObject();
				labels.add(item.getServerName());
				bpsList.add(item.getBps());
				
				tmp.put("data", bpsList);
				tmp.put("label", "Bps");
				
				colorIndex += 2;
				colorIndex %= COLOR_LIST.size();
				tmp.put("backgroundColor", COLOR_LIST.get(colorIndex));
				
				chartDatasets.add(tmp);
//			}
			
			chartDataToJSP.put("datasets", chartDatasets);
			chartDataToJSP.put("labels", labels);
			
			return chartDataToJSP;
		}
		
		public static JSONObject buildPpsData(SystemStatusSearchResult item) {
			JSONObject chartDataToJSP = new JSONObject();
			List<String> serviceLabels = new ArrayList<>();
			
			Set<String> dateLabelSet = new HashSet<>();
			Set<String> conditionSet = new HashSet<>();
			
			// 넘겨줄 JSON 데이터
			JSONArray chartDatasets = new JSONArray();
			int colorIndex = 3;
			JSONArray bpsList = new JSONArray();
			JSONArray labels = new JSONArray();
			
//			for(SystemStatusSearchResult item : items) {
				JSONObject tmp = new JSONObject();
				labels.add(item.getServerName());
				bpsList.add(item.getPps());
				
				tmp.put("data", bpsList);
				tmp.put("label", "Pps");
				
				colorIndex += 2;
				colorIndex %= COLOR_LIST.size();
				tmp.put("backgroundColor", COLOR_LIST.get(colorIndex));
				
				chartDatasets.add(tmp);
//			}
			
			chartDataToJSP.put("datasets", chartDatasets);
			chartDataToJSP.put("labels", labels);
			
			return chartDataToJSP;
		}
		
		
		
	}
}
