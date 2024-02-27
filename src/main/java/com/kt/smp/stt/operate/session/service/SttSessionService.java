/**
 * 
 */
package com.kt.smp.stt.operate.session.service;

import java.util.List;

import com.kt.smp.stt.operate.session.dto.ServerInfoDto;
import com.kt.smp.stt.operate.session.dto.SessionRequestDto;
import com.kt.smp.stt.operate.session.dto.SttServerInfoDto;
import com.kt.smp.stt.operate.session.dto.SttSessionResponseDto;

public interface SttSessionService {
	List<ServerInfoDto> convertData(List<SttServerInfoDto> serverInfoList, SessionRequestDto sessionRequestDto);
	
	String setDelay();
	
	SttSessionResponseDto getCoreChannelApi();
	
	SttSessionResponseDto getDummyCoreChannelApi();
	
	SttSessionResponseDto getSampleData();
	
	void setEachServerSessionMaxCount(List<SttServerInfoDto> serverInfoList);
}
