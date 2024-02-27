/**
 * 
 */
package com.kt.smp.stt.comm.preference.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceValuesDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceCodeDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.RemoveListDto;
import com.kt.smp.stt.comm.preference.dto.RemoveStringListDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.operate.session.dto.SessionRequestDto;


public interface PreferenceService {

	List<PreferenceCodeDto> findCategoryList();
	
	PreferenceValuesDto getAllConfigValue();
	
	AgentConfigDto getAgentConfigValues();

	String getSessionTarget();
	
	RemoveListDto getRemovableList();
	
	RemoveStringListDto getRemoveStringList();
	
	void updateSchedulerConfigValues(SchedulerConfigUpdateDto removerSchedulerUpdateDto);
	
	void updateThresholdConfigValues(ThresholdConfigDto request);

	void updateAgentConfigValues(AgentConfigDto request);
	
	void updateEtcConfigValues(EtcConfigDto request);

	void updateEncryptConfigValues(EncryptConfigDto dto);

//	void insertSessionSortCondition(SessionRequestDto dto);
	
}
