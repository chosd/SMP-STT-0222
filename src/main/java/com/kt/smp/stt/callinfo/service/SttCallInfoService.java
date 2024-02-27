package com.kt.smp.stt.callinfo.service;

import com.github.pagehelper.Page;
import com.kt.smp.stt.callinfo.dto.CallInfoSearchConditionDto;
import com.kt.smp.stt.callinfo.dto.SttCallInfoDto;

public interface SttCallInfoService {

	public Page<SttCallInfoDto> search(CallInfoSearchConditionDto callInfoSearchConditionDto);

	public SttCallInfoDto getCallInfoByCallId(String callId);

	public SttCallInfoDto getCallInfoByApplicationId(String applicationId);

	public String getCallStatusByApplicationId(String applicationId);

	public byte[] getCallInfoWavFile(String applicationId);
	
}
