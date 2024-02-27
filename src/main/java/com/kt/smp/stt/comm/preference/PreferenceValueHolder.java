/**
 * 
 */
package com.kt.smp.stt.comm.preference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceValuesDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.comm.preference.service.PreferenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @FileName : PreferenceValue.java
* @Project : stt-smp-service
* @Date : 2024. 1. 19.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class PreferenceValueHolder {

	private final PreferenceService preferenceService;
	private final ConfigService configService;
	
	// 스케줄러 설정값
	public static Map<String, Boolean> schedulerActive = new HashMap<>();					// 스케줄러 사용여부 
	public static Map<String, List<String>> datasToRemove = new HashMap<>();				// 삭제할 데이터 리스트
	public static Map<String, List<String>> filesToRemove = new HashMap<>();				// 삭제할 데이터 리스트
	public static Map<String, Integer> removerTime = new HashMap<>();						// 삭제 실행 시간 (시)
	public static Map<String, Integer> removerStandard = new HashMap<>();					// 삭제 기준 (일)		
	public static Map<String, Integer> intervalSystemStatus = new HashMap<>();				// HW 리소스 조회 주기 (초)
	public static Map<String, Integer> intervalRequestStat = new HashMap<>();				// 통계 조회 주기 (분)
	
	// HW 리소스 임계치 설정값
	public static Map<String, Integer> cpuUsedThreshold = new HashMap<>();					// CPU 사용률 임계치 (%)
	public static Map<String, Integer> freeMemorySizeThreshold = new HashMap<>();			// 여유 메모리 임계치 (%)
	public static Map<String, Integer> freeAppStorageSizeThreshold = new HashMap<>();		// 여유 저장소 임계치 (%)
	public static Map<String, String> sessionTarget = new HashMap<>();						// 세션 조회 대상 서버
	
	// 암호화 설정 값
	public static Map<String, Boolean> textEncrypt = new HashMap<>();
	public static Map<String, Boolean> wavEncrypt = new HashMap<>();
	
	// 기타 설정 값
	public static Map<String, Integer> recordCount = new HashMap<>();						// 화면 기본 레코드 수
	public static Map<String, Double> confidenceChartThreshold = new HashMap<>();			// 신뢰도 차트 임계값
	
	// 세션 정렬 조건
//	public static Map<String, SessionSortCondition> sessionFirstSortCondition = new HashMap<>();
//	public static Map<String, SessionSortCondition> sessionSecondSortCondition = new HashMap<>();

	@PostConstruct
	public void init() {
		
    	for (ConfigDto tenantConfig : configService.getAllNotMaster()) {
    		log.info("[init] Setting STT Preference Value for tenant. Current projectCode is '{}'", tenantConfig.getProjectCode());
    		String projectCode = tenantConfig.getProjectCode();
    		TenantContextHolder.set(tenantConfig.getProjectCode());
    		setPrefenceValues(projectCode);
    	}
    	
	}

	private void setPrefenceValues(String projectCode) {
		try {
			PreferenceValuesDto preferencValuesDto = preferenceService.getAllConfigValue();
			SchedulerConfigDto schedulerConfigDto = preferencValuesDto.getSchedulerConfig();
			ThresholdConfigDto thresholdConfigDto = preferencValuesDto.getThresholdConfig();
			AgentConfigDto agentConfigDto = preferencValuesDto.getAgentConfig();
			EtcConfigDto etcConfigDto = preferencValuesDto.getEtcConfig();
			EncryptConfigDto encryptConfigDto = preferencValuesDto.getEncryptConfig();
//			OperateConfigDto operateConfigDto = preferencValuesDto.getOperateConfigDto();
			
			// 스케줄러 관련 설정 값 세팅
			schedulerActive.put(projectCode, "Y".equals(schedulerConfigDto.getSchedulerUseYn()) ? true : false);
			removerTime.put(projectCode, Integer.valueOf(schedulerConfigDto.getRemoverTime()));
			removerStandard.put(projectCode, Integer.valueOf(schedulerConfigDto.getRemoverStandard()));
			intervalSystemStatus.put(projectCode, Integer.valueOf(schedulerConfigDto.getHwResourceCycle()));
			intervalRequestStat.put(projectCode, Integer.valueOf(schedulerConfigDto.getStatisticsCycle()));
			
			List<String> removeFileList = schedulerConfigDto.getRemoverFileList().stream()
					.map((removeFile)->{
						return removeFile.getDescription();
					})
					.collect(Collectors.toList());
			
			filesToRemove.put(projectCode, removeFileList);
			
			List<String> removeDataList = schedulerConfigDto.getRemoverDataList().stream()
					.map((removeData)->{
						return removeData.getDescription();
					})
					.collect(Collectors.toList());

			datasToRemove.put(projectCode, removeDataList);
			
			// HW 리소스 임계치 설정 값 세팅
			cpuUsedThreshold.put(projectCode, thresholdConfigDto.getCpu());
			freeMemorySizeThreshold.put(projectCode, thresholdConfigDto.getMemory());
			freeAppStorageSizeThreshold.put(projectCode, thresholdConfigDto.getStorage());
			
			// 세션 대상 서버 설정 값 세팅
			sessionTarget.put(projectCode, agentConfigDto.getSessionTarget());
			
			// 암호화 설정 값 세팅
			textEncrypt.put(projectCode, "Y".equals(encryptConfigDto.getTextEncrypt()) ? true : false);
			wavEncrypt.put(projectCode, "Y".equals(encryptConfigDto.getWavEncrypt()) ? true : false);
			
			// 기타 설정 값 세팅
			recordCount.put(projectCode, Integer.valueOf(etcConfigDto.getRecordCount()));
			confidenceChartThreshold.put(projectCode, Double.valueOf(etcConfigDto.getConfidenceChartThreshold()));
			
			// 세션 정렬 조건 세팅
//			if (operateConfigDto != null) {
//				sessionFirstSortCondition.put(projectCode, operateConfigDto.getFirstSortCondition());
//				sessionSecondSortCondition.put(projectCode, operateConfigDto.getSecondSortCondition());	
//			}
			
		} catch(Exception e) {
			log.debug("[PreferenceValueHolder.setPrefenceValues] Cannot set preference values");
		}
	}
	
}
