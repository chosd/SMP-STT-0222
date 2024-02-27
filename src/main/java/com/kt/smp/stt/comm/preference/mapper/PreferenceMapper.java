/**
 * 
 */
package com.kt.smp.stt.comm.preference.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceCodeDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.operate.session.dto.SessionRequestDto;

@Mapper
public interface PreferenceMapper {

	List<PreferenceCodeDto> findCategoryList();

	List<PreferenceCodeDto> findByCodeKey(String codeKey);

	int updateSchedulerConfigValues(SchedulerConfigUpdateDto removerSchedulerUpdateDto);

	int updateThresholdConfigValues(ThresholdConfigDto dto);

	int updateEtcConfigValues(EtcConfigDto dto);

	List<PreferenceCodeDto> findRemovableList();

	List<PreferenceCodeDto> findAllConfig();

	int updateAgentConfigValues(AgentConfigDto request);

	String findSessionTarget();

	String findVerifyDataEncrypt();

	int updateEncryptConfigValues(EncryptConfigDto dto);

	List<PreferenceCodeDto> findRemoveList();

//	int insertSessionSortCondition(SessionRequestDto dto);
//
//	int updateSessionSortCondition(SessionRequestDto dto);
	
}
