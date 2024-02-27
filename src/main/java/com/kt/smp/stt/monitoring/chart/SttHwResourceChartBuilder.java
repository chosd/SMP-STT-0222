/**
 * 
 */
package com.kt.smp.stt.monitoring.chart;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.kt.smp.stt.monitoring.dto.SttSystemStatusChartLegendVO;
import com.kt.smp.stt.monitoring.dto.SystemStatusSearchResult;

public class SttHwResourceChartBuilder {

	private static final String LEGEND_CPU = "cpu";
	private static final String LEGEND_MEMORY = "memory";
	private static final String LEGEND_DISK = "disk";

	public static JSONArray build(List<SystemStatusSearchResult> source) {

		JSONArray result = new JSONArray();
		
		List<String> serverList = new ArrayList<>();
		
		// 서버이름 담긴 리스트 생성
		for (SystemStatusSearchResult serverInfo : source) {
			String serverName = serverInfo.getServerName();
			if (!serverList.contains(serverName)) {
				serverList.add(serverName);
			}
		}
		
		// 데이터를 범례별로 구분
		List<SttSystemStatusChartLegendVO> cpuList = new ArrayList<>();
		List<SttSystemStatusChartLegendVO> memoryList = new ArrayList<>();
		List<SttSystemStatusChartLegendVO> diskList = new ArrayList<>();
		
		for (SystemStatusSearchResult serverInfo : source) {
			String serverName = serverInfo.getServerName();
			String lastCheckTime = serverInfo.getLastCheckTime();
			cpuList.add(SttSystemStatusChartLegendVO.builder()
					.serverName(serverName)
					.legendName(LEGEND_CPU)
					.value(Double.valueOf(serverInfo.getCpuUsed()))
					.lastCheckTime(lastCheckTime)
					.build());
			
			memoryList.add(SttSystemStatusChartLegendVO.builder()
					.serverName(serverName)
					.legendName(LEGEND_MEMORY)
					.value(Math.round((Double.valueOf(serverInfo.getFreeMemorySize()) / Double.valueOf(serverInfo.getMaxMemorySize())) * 100 * 100) / 100.0)
					.lastCheckTime(lastCheckTime)
					.build());
			
			diskList.add(SttSystemStatusChartLegendVO.builder()
					.serverName(serverName)
					.legendName(LEGEND_DISK)
					.value(Math.round((Double.valueOf(serverInfo.getFreeAppStorageSize()) / Double.valueOf(serverInfo.getMaxAppStorageSize())) * 100 * 100) / 100.0)
					.lastCheckTime(lastCheckTime)
					.build());
			
		}
		

		// 서버 별 최종 세팅
		for (String serverName : serverList) {
			int index = 0;
			JSONObject serverInfo = new JSONObject();
			serverInfo.put("serverName", serverName);
			JSONArray datasets = new JSONArray();
			
			datasets.add(getLegendObj(serverName, cpuList, index++));
			datasets.add(getLegendObj(serverName, memoryList, index++));
			datasets.add(getLegendObj(serverName, diskList, index++));
			
			serverInfo.put("datasets", datasets);
			
			result.add(serverInfo);
		}
		
		
		return result;
	}

	private static JSONObject getLegendObj(String serverName, List<SttSystemStatusChartLegendVO> legendList, int index) {
		JSONObject legendObj = new JSONObject();
		JSONArray legendDatas = new JSONArray();
		
		legendObj.put("pointRadius", 5);
		legendObj.put("pointHoverRadius", 7);
		legendObj.put("backgroundColor", colorList().get(index));
		legendObj.put("pointStyle", pointStyleList().get(index));

		for(SttSystemStatusChartLegendVO vo : legendList) {
			if (!serverName.equals(vo.getServerName())) {
				continue;
			}
			if (!legendObj.containsKey("label")) {
				legendObj.put("label", vo.getLegendName());
			}
			JSONObject data = new JSONObject();
			data.put("x", vo.getLastCheckTime());
			data.put("y", vo.getValue());
			legendDatas.add(data);
		}
		
		legendObj.put("data", legendDatas);
		
		return legendObj;
	}
	
	private static List<String> colorList(){
        List<String> colors = new ArrayList<>();
        colors.add("rgba(0, 153, 204, 0.7)");
        colors.add("rgba(51, 204, 51, 0.7)");
//        colors.add("rgba(255, 0, 255, 0.7)");
//        colors.add("rgba(255, 80, 80, 0.7)");
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
