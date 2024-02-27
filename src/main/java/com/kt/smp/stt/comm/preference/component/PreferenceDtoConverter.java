/**
 * 
 */
package com.kt.smp.stt.comm.preference.component;

import static com.kt.smp.stt.comm.preference.enums.PreferenceDescription.*;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceCodeDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceValuesDto;
import com.kt.smp.stt.comm.preference.dto.RemoveListDto;
import com.kt.smp.stt.comm.preference.dto.RemoveStringListDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.comm.preference.enums.DatasToRemove;
import com.kt.smp.stt.comm.preference.enums.FilesToRemove;
import com.kt.smp.stt.comm.preference.enums.PreferenceCodeKey;

import lombok.RequiredArgsConstructor;

/**
* @FileName : ConfigDtoBuilder.java
* @Project : stt-smp-service
* @Date : 2024. 2. 14.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@Component
@RequiredArgsConstructor
public class PreferenceDtoConverter {
	
	private static final String DESCRIPTION_DELEGATOR = "_";
	private static final String TYPE_FILE = "FILE";
	private static final String TYPE_DATA = "DATA";
	
	public PreferenceValuesDto makeAllConfigValue(List<PreferenceCodeDto> configList) {
		return PreferenceValuesDto.builder()
				.schedulerConfig(makeSchedulerConfigData(configList))
				.thresholdConfig(makeThresholdConfigData(configList))
				.agentConfig(makeAgentConfigData(configList))
				.etcConfig(makeEtcConfigData(configList))
				.encryptConfig(makeEncConfigDto(configList))
//				.operateConfigDto(makeOperateConfigDto(configList))
				.build();
	}
	
	public SchedulerConfigDto makeSchedulerConfigData(List<PreferenceCodeDto> configVariableList) {
		
		SchedulerConfigDto result = new SchedulerConfigDto();
		
		List<FilesToRemove> filesToRemoveList = new ArrayList<>();
		List<DatasToRemove> datasToRemoveList = new ArrayList<>();
		
		for (PreferenceCodeDto dto : configVariableList) {
			if (dto == null || !dto.getCodeKey().equals(PreferenceCodeKey.SCHEDULER.getCodeKey())) {
				continue;
			}
			
			String description = dto.getDescription();
			String codeValue = dto.getCodeValue();
			
			if (description.equals(SCHEDULER_USEYN.getDescription())) {
				result.setSchedulerUseYn(codeValue);
				continue;
			}
			
			if (description.equals(SCHEDULER_CYCLE_STATISTICS.getDescription())) {
				result.setStatisticsCycle(Integer.valueOf(codeValue));
				continue;
			}
			
			if (description.equals(SCHEDULER_CYCLE_HW_RESOURCE.getDescription())) {
				result.setHwResourceCycle(Integer.valueOf(codeValue));
				continue;
			}
			
			if (description.equals(REMOVER_STANDARD.getDescription())) {
				result.setRemoverStandard(Integer.valueOf(codeValue));
				continue;
			}

			if (description.equals(REMOVER_TIME.getDescription())) {
				result.setRemoverTime(codeValue);
				continue;
			}
			
			if (codeValue.equals("N")) {
				continue;
			}
			
			String fileOrData = description.split(DESCRIPTION_DELEGATOR)[1];
			
			if (fileOrData.equals(TYPE_FILE)) {
				filesToRemoveList.add(FilesToRemove.findByDescription(description));
			} else if (fileOrData.equals(TYPE_DATA)) {
				datasToRemoveList.add(DatasToRemove.findByDescription(description));
			}
		}
		
		result.setRemoverDataList(datasToRemoveList);
		result.setRemoverFileList(filesToRemoveList);
		
		return result;
	}
	
	public ThresholdConfigDto makeThresholdConfigData(List<PreferenceCodeDto> configVariableList) {
		ThresholdConfigDto result = new ThresholdConfigDto();
		
		for (PreferenceCodeDto dto : configVariableList) {
			if (dto == null || !dto.getCodeKey().equals(PreferenceCodeKey.THRESHOLD.getCodeKey())) {
				continue;
			}
			
			String description = dto.getDescription();
			int value = Integer.parseInt(dto.getCodeValue());
			if (description.equals(MEMORY_PERCENT.getDescription())) {
				result.setMemory(value);
				continue;
			}
			
			if (description.equals(STORAGE_PERCENT.getDescription())) {
				result.setStorage(value);
				continue;
			}
			
			if (description.equals(CPU_PERCENT.getDescription())) {
				result.setCpu(value);
				continue;
			}
		}
		
		return result;
	}
	
	public EtcConfigDto makeEtcConfigData(List<PreferenceCodeDto> configVariableList) {
		EtcConfigDto result = new EtcConfigDto();

		for (PreferenceCodeDto dto : configVariableList) {
			if (dto == null || !dto.getCodeKey().equals(PreferenceCodeKey.ETC.getCodeKey())) {
				continue;
			}
			
			String description = dto.getDescription();
			String value = dto.getCodeValue();
			
			if (description.equals(RECORD_COUNT.getDescription())) {
				result.setRecordCount(value);
				continue;
			}
			
			if (description.equals(CONFIDENCE_CHART_THRESHOLD.getDescription())) {
				result.setConfidenceChartThreshold(value);
				continue;
			}
		
		}
		return result;
	}
	
	public EncryptConfigDto makeEncConfigDto(List<PreferenceCodeDto> configVariableList) {
		EncryptConfigDto result = new EncryptConfigDto();
		
		for(PreferenceCodeDto dto : configVariableList) {
			if (dto == null || !dto.getCodeKey().equals(PreferenceCodeKey.ENC.getCodeKey())) {
				continue;
			}
			
			String description = dto.getDescription();
			String value = dto.getCodeValue();
			
			
			if (description.equals(TEXT_ENCRYPT.getDescription())) {
				result.setTextEncrypt(value);
				continue;
			}
			
			if (description.equals(WAV_ENCRYPT.getDescription())) {
				result.setWavEncrypt(value);
				continue;
			}
		}
		
		return result;
		
	}

	public RemoveListDto extractToRemovableList(List<PreferenceCodeDto> configVariableList) {
		RemoveListDto result = new RemoveListDto();
		
		List<FilesToRemove> filesToRemoveList = new ArrayList<>();
		List<DatasToRemove> datasToRemoveList = new ArrayList<>();
		
		for (PreferenceCodeDto dto : configVariableList) {
			if (dto == null) {
				continue;
			}
			String description = dto.getDescription();
			String fileOrData = description.split(DESCRIPTION_DELEGATOR)[1];
			if ( fileOrData.equals(TYPE_FILE)) {
				filesToRemoveList.add(FilesToRemove.findByDescription(description));
			} else if (fileOrData.equals(TYPE_DATA)) {
				datasToRemoveList.add(DatasToRemove.findByDescription(description));
			}
		}
		result.setDatasToRemoveList(datasToRemoveList);
		result.setFilesToRemoveList(filesToRemoveList);
		
		return result;
	}
	
	public RemoveStringListDto extractToRemoveYStringList(List<PreferenceCodeDto> configVariableList) {
		RemoveStringListDto result = new RemoveStringListDto();
		
		List<String> filesToRemoveList = new ArrayList<>();
		List<String> datasToRemoveList = new ArrayList<>();
		
		for (PreferenceCodeDto dto : configVariableList) {
			if (dto == null) {
				continue;
			}

			String description = dto.getDescription();
			String fileOrData = description.split(DESCRIPTION_DELEGATOR)[1];

			if (fileOrData.equals(TYPE_FILE)) {
				filesToRemoveList.add(description);
			} else if (fileOrData.equals(TYPE_DATA)) {
				datasToRemoveList.add(description);
			}
		}
		
		result.setDatasToRemoveList(datasToRemoveList);
		result.setFilesToRemoveList(filesToRemoveList);
		
		return result;
	}

	public AgentConfigDto makeAgentConfigData(List<PreferenceCodeDto> configList) {
		AgentConfigDto result = new AgentConfigDto();
		
		for (PreferenceCodeDto dto : configList) {
			if (dto == null || !dto.getCodeKey().equals(PreferenceCodeKey.AGENT.getCodeKey())) {
				continue;
			}
			
			String description = dto.getDescription();
			String codeValue = dto.getCodeValue();
	
			if (description.equals(SESSION_TARGET.getDescription())) {
				result.setSessionTarget(codeValue);
				continue;
			}
			if (description.equals(MULTIPART_HOST_DEPLOY.getDescription())) {
				result.setMultipartHostDeploy(codeValue);
				continue;
			}
			if (description.equals(MULTIPART_HOST_TEST.getDescription())) {
				result.setMultipartHostTest(codeValue);
				continue;
			}
			if (description.equals(MULTIPART_SUB_DEPLOY.getDescription())) {
				result.setMultipartSubDeploy(codeValue);
				continue;
			}
			if (description.equals(MULTIPART_SUB_TEST.getDescription())) {
				result.setMultipartSubTest(codeValue);
				continue;
			}
		}
		
		return result;
	}
	

	/**
	 * 삭제여부를 선택할 수 있는 데이터와 파일 리스트를 WebController에서 model로 넘겨줄 수 있도록 JSONObject로 변환
	 */
	public JSONObject convertRemoveListToJson(RemoveListDto dto) {
		JSONArray datasToRemoveList = new JSONArray();
		JSONArray filesToRemoveList = new JSONArray();
		
		JSONObject result = new JSONObject();
		for (DatasToRemove datasToRemove : dto.getDatasToRemoveList()) {
			JSONObject datasToRemoveObj = new JSONObject();
			datasToRemoveObj.put("description", datasToRemove.getDescription());
			datasToRemoveObj.put("name", datasToRemove.getName());
			
			datasToRemoveList.add(datasToRemoveObj);
		}
		
		for (FilesToRemove filesToRemove : dto.getFilesToRemoveList()) {
			JSONObject filesToRemoveObj= new JSONObject();
			filesToRemoveObj.put("description", filesToRemove.getDescription());
			filesToRemoveObj.put("name", filesToRemove.getName());
			
			filesToRemoveList.add(filesToRemoveObj);
		}
		result.put("datasToRemoveList", datasToRemoveList);
		result.put("filesToRemoveList", filesToRemoveList);
		
		return result;
	}
	
	
	//	private OperateConfigDto makeOperateConfigDto(List<PreferenceCodeDto> configList) {
	//	OperateConfigDto result = new OperateConfigDto();
	//	
	//	for (PreferenceCodeDto dto : configList) {
	//		if (dto == null || !dto.getCodeKey().equals(PreferenceCodeKey.OPERATE.getCodeKey())) {
	//			continue;
	//		}
	//		
	//		String description = dto.getDescription();
	//		String codeValue = dto.getCodeValue();
	//		
	//		if (description.equals(SESSION_FIRST_SORT_CONDITION.getDescription())) {
	//			result.setFirstSortCondition(codeValue);
	//			continue;
	//		}
	//		
	//		if (description.equals(SESSION_SECOND_SORT_CONDITION.getDescription())) {
	//			result.setSecondSortCondition(codeValue);
	//			continue;
	//		}
	//		
	//	}
	//	return null;
	//}
}
