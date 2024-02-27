/**
 * 
 */
package com.kt.smp.stt.comm.preference.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.stt.comm.preference.component.PreferenceDtoConverter;
import com.kt.smp.stt.comm.preference.component.PreferenceValueHolderUpdater;
import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceValuesDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceCodeDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.RemoveListDto;
import com.kt.smp.stt.comm.preference.dto.RemoveStringListDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.comm.preference.repository.PreferenceRepository;
import com.kt.smp.stt.comm.preference.service.PreferenceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

	private final PreferenceRepository preferenceRepository;
	private final PreferenceDtoConverter preferenceDtoConverter;
	private final PreferenceValueHolderUpdater preferenceValueHolderUpdater;
	
	@Override
	public List<PreferenceCodeDto> findCategoryList() {
		return preferenceRepository.findCategoryList();
	}

	/**
	 * 모든 설정값을 조회
	 */
	@Override
	public PreferenceValuesDto getAllConfigValue() {
		List<PreferenceCodeDto> configList = preferenceRepository.findAllConfig();
		
		return preferenceDtoConverter.makeAllConfigValue(configList);
	}
	
	/**
	 * 스케줄러 설정 값 수정
	 */
	public void updateSchedulerConfigValues(SchedulerConfigUpdateDto removerSchedulerUpdateDto) {
		int result = preferenceRepository.updateSchedulerConfigValues(removerSchedulerUpdateDto);
		String projectCode = TenantContextHolder.getProjectCode();
		preferenceValueHolderUpdater.updateSchedulerValueHolder(removerSchedulerUpdateDto, projectCode);
	}

	/**
	 * 삭제여부를 선택할 수 있는 데이터와 파일 리스트를 조회
	 */
	@Override
	public RemoveListDto getRemovableList() {
		List<PreferenceCodeDto> configVariableList = preferenceRepository.findRemovableList();
		
		return preferenceDtoConverter.extractToRemovableList(configVariableList);
	}
	
	/**
	 * 삭제 설정된 데이터와 파일 리스트를 조회
	 */
	@Override
	public RemoveStringListDto getRemoveStringList() {
		List<PreferenceCodeDto> configVariableList = preferenceRepository.findRemoveList();
		
		return preferenceDtoConverter.extractToRemoveYStringList(configVariableList);
	}
	
	/**
	 * HW 리소스 임계치 설정값 업데이트
	 */
	@Override
	public void updateThresholdConfigValues(ThresholdConfigDto thresholdConfigDto) {
		String projectCode = TenantContextHolder.getProjectCode();
		int count = preferenceRepository.updateThresholdConfigValues(thresholdConfigDto);
		preferenceValueHolderUpdater.updateThresholdValueHolder(thresholdConfigDto, projectCode);
	}

	/**
	 * 기타 설정값 업데이트
	 */
	@Override
	public void updateEtcConfigValues(EtcConfigDto etcConfigDto) {
		preferenceRepository.updateEtcConfigValues(etcConfigDto);
		String projectCode = TenantContextHolder.getProjectCode();
		preferenceValueHolderUpdater.updateEtcValueHolder(etcConfigDto, projectCode);
	}
	
	/**
	 * Agent 관련 설정 값 조회
	 */
	@Override
	public AgentConfigDto getAgentConfigValues() {
		List<PreferenceCodeDto> configList = preferenceRepository.findAllConfig();
		return preferenceDtoConverter.makeAgentConfigData(configList);
	}
	
	/**
	 * Agent 설정 값 수정
	 */
	@Override
	public void updateAgentConfigValues(AgentConfigDto request) {
		int result = preferenceRepository.updateAgentConfigValues(request);
	}

	/**
	 * 세션 대상 서버 조회
	 */
	@Override
	public String getSessionTarget() {
		return preferenceRepository.findSessionTarget();
	}

	/**
	 * 암호화 설정 업데이트
	 */
	@Override
	public void updateEncryptConfigValues(EncryptConfigDto encryptConfigDto) {
		int result = preferenceRepository.updateEncryptConfigValues(encryptConfigDto);
		String projectCode = TenantContextHolder.getProjectCode();
		preferenceValueHolderUpdater.updateEncryptValueHolder(encryptConfigDto, projectCode);
	}

//	@Override
//	public void insertSessionSortCondition(SessionRequestDto dto) {
//		List<PreferenceCodeDto> sessionCondList = preferenceRepository.findByCodeKey(PreferenceCodeKey.OPERATE.getCodeKey());
//		
//		if (sessionCondList == null || sessionCondList.size() == 0 ) {
//			preferenceRepository.insertSessionSortCondition(dto);
//		} else {
//			preferenceRepository.updateSessionSortCondition(dto);
//		}
//		
//		String projectCode = TenantContextHolder.getProjectCode();
//		
//		if (projectCode != null) {
//			sessionFirstSortCondition.put(projectCode, dto.getFirstCondition());
//			sessionSecondSortCondition.put(projectCode, dto.getSecondCondition());
//		}
//		
//	}

}
