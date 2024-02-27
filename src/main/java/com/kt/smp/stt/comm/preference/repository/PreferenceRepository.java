/**
 * 
 */
package com.kt.smp.stt.comm.preference.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceCodeDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.comm.preference.mapper.PreferenceMapper;
import com.kt.smp.stt.operate.session.dto.SessionRequestDto;

@Repository
public class PreferenceRepository {
	
	private final PreferenceMapper mapper;
	
	public PreferenceRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(PreferenceMapper.class);
    }

	public List<PreferenceCodeDto> findCategoryList() {
		return mapper.findCategoryList();
	}

	public List<PreferenceCodeDto> findByCodeKey(String codeKey) {
		return mapper.findByCodeKey(codeKey);
	}

	public int updateSchedulerConfigValues(SchedulerConfigUpdateDto removerSchedulerUpdateDto) {
		return mapper.updateSchedulerConfigValues(removerSchedulerUpdateDto);
	}

	public int updateThresholdConfigValues(ThresholdConfigDto dto) {
		return mapper.updateThresholdConfigValues(dto);
	}

	public int updateEtcConfigValues(EtcConfigDto dto) {
		return mapper.updateEtcConfigValues(dto);
	}

	public List<PreferenceCodeDto> findRemovableList() {
		return mapper.findRemovableList();
	}

	public List<PreferenceCodeDto> findAllConfig() {
		return mapper.findAllConfig();
	}

	public int updateAgentConfigValues(AgentConfigDto request) {
		return mapper.updateAgentConfigValues(request);
	}

	public String findSessionTarget() {
		return mapper.findSessionTarget();
	}

	public int updateEncryptConfigValues(EncryptConfigDto dto) {
		return mapper.updateEncryptConfigValues(dto);
	}

	public List<PreferenceCodeDto> findRemoveList() {
		return mapper.findRemoveList();
	}
	
//	public int insertSessionSortCondition(SessionRequestDto dto) {
//		return mapper.insertSessionSortCondition(dto);
//	}
//
//	public int updateSessionSortCondition(SessionRequestDto dto) {
//		return mapper.updateSessionSortCondition(dto);
//	}
}
