/**
 * 
 */
package com.kt.smp.stt.reprocess.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.config.CommonConstants;
import com.kt.smp.stt.common.CallbackUrlResolver;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.reprocess.dto.ReprocessLogDto;
import com.kt.smp.stt.reprocess.dto.ReprocessStatusDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessRequestDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessResponseDto;
import com.kt.smp.stt.reprocess.dto.SttSemiRealTimeRequestDto;
import com.kt.smp.stt.reprocess.service.SttReprocessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @FileName : SttReprocessServiceImpl.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 18.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Slf4j
@RequiredArgsConstructor
public class SttReprocessServiceImpl implements SttReprocessService {

	private final RestTemplate restTemplate;
	
	private final EngineUrlResolver engineUrlResolver;
	
	private final CallbackUrlResolver callbackUrlResolver;
	
    @Value("${spring.profiles.active}")
    private String profile; 
    
	@Value("${reprocess.multipart}")
	private final boolean isMultipart;
	 
	@Override
	public SttReprocessResponseDto reprocess(List<SttReprocessRequestDto> sttReprocessRequestDto) {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		Integer fileSendType = isMultipart ? 0 : 1;
		
		String coreUrl = engineUrlResolver.resolve();
		
		for (SttReprocessRequestDto dto : sttReprocessRequestDto) {
			
			Integer txRxType = dto.getSpeakerType().equals("TX") ? 0 : dto.getSpeakerType().equals("RX") ? 1 : 2;
			
			
			SttSemiRealTimeRequestDto sttSemiRealTimeRequestDto = SttSemiRealTimeRequestDto.builder()
					.fileSendType(fileSendType)
					.serviceCode(dto.getServiceCode())
					.callKey(dto.getCallId())
					.txRxType(txRxType)
					.callbackUrl(null)
					.build();
		}
		
		restTemplate.postForEntity(null, sttReprocessRequestDto, null);
		return null;
	}

	@Override
	public ReprocessLogDto getReprocessLog(String callKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReprocessStatusDto getReprocessStatus(String applicationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void autoReprocess() {
		// TODO Auto-generated method stub
		
	}

}