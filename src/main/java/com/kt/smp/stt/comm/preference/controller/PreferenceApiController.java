package com.kt.smp.stt.comm.preference.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.comm.preference.dto.AgentConfigDto;
import com.kt.smp.stt.comm.preference.dto.EncryptConfigDto;
import com.kt.smp.stt.comm.preference.dto.PreferenceValuesDto;
import com.kt.smp.stt.comm.preference.dto.RemoveListDto;
import com.kt.smp.stt.comm.preference.dto.EtcConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigDto;
import com.kt.smp.stt.comm.preference.dto.SchedulerConfigUpdateDto;
import com.kt.smp.stt.comm.preference.dto.ThresholdConfigDto;
import com.kt.smp.stt.comm.preference.service.PreferenceService;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.train.trainData.dto.BaseResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${smp.service.uri.prefix}/api")
@RequiredArgsConstructor
public class PreferenceApiController {

	private final PreferenceService preferenceService;
	
	@SmpServiceApi(
            name = "STT 환경 변수 설정값 모두 조회",
            method = RequestMethod.GET,
            path = "/preference",
            type = "조회",
            description = "STT 환경 변수 설정값 모두 조회")
	public ResponseEntity<BaseResponseDto<PreferenceValuesDto>> getAllConfigValue(){
        BaseResponseDto<PreferenceValuesDto> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

		PreferenceValuesDto result = preferenceService.getAllConfigValue();
		responseDto.setResult(result);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@SmpServiceApi(
            name = "STT 스케줄러 설정값 수정",
            method = RequestMethod.POST,
            path = "/preference/scheduler",
            type = "수정",
            description = "STT 스케줄러 설정값 수정")
	public ResponseEntity<BaseResponseDto<Void>> updateSchedulerConfigValues(
			@RequestBody SchedulerConfigUpdateDto dto
			){
        BaseResponseDto<Void> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        
		preferenceService.updateSchedulerConfigValues(dto);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@SmpServiceApi(
            name = "HW 리소스 임계치 설정값 수정",
            method = RequestMethod.POST,
            path = "/preference/threshold",
            type = "수정",
            description = "HW 리소스 임계치 설정값 수정")
	public ResponseEntity<BaseResponseDto<Void>> updateThresholdConfigValues(
			@RequestBody ThresholdConfigDto dto
			){
        BaseResponseDto<Void> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        
		preferenceService.updateThresholdConfigValues(dto);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@SmpServiceApi(
            name = "Agent 관련 설정값 수정",
            method = RequestMethod.POST,
            path = "/preference/agent",
            type = "수정",
            description = "Agent 관련 설정값 수정")
	public ResponseEntity<BaseResponseDto<Void>> updateAgentConfigValues(
			@RequestBody AgentConfigDto dto
			){
        BaseResponseDto<Void> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        
		preferenceService.updateAgentConfigValues(dto);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@SmpServiceApi(
            name = "STT 기타 설정값 수정",
            method = RequestMethod.POST,
            path = "/preference/etc",
            type = "수정",
            description = "STT 기타 설정값 수정")
	public ResponseEntity<BaseResponseDto<Void>> updateEtcConfigValues(
			@RequestBody EtcConfigDto dto
			){
        BaseResponseDto<Void> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        
		preferenceService.updateEtcConfigValues(dto);
		
		return ResponseEntity.ok(responseDto);
	}
	
	@SmpServiceApi(
            name = "STT 암호화 설정 수정",
            method = RequestMethod.POST,
            path = "/preference/enc",
            type = "수정",
            description = "STT 암호화 설정값 수정")
	public ResponseEntity<BaseResponseDto<Void>> updateEncConfigValues(
			@RequestBody EncryptConfigDto dto
			){
        BaseResponseDto<Void> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
        
		preferenceService.updateEncryptConfigValues(dto);
		
		return ResponseEntity.ok(responseDto);
	}
}

