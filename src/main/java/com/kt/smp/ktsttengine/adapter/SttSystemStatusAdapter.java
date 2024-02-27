/**
 * 
 */
package com.kt.smp.ktsttengine.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ibm.icu.text.SimpleDateFormat;
import com.kt.smp.common.exception.SttException;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.statistics.domain.SttServerInfoVO;
import com.kt.smp.stt.statistics.dto.SttSystemStatusResponseDto;

import static com.kt.smp.stt.common.component.SttCmsResultStatus.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
*@FileName : SttStatusAdapter.java
@Project : kt-stt-service_r
@Date : 2023. 10. 16.
*@작성자 : 심수연
*@변경이력 :
*@프로그램설명 : HW 리소스 조회 STT 엔진 통신
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class SttSystemStatusAdapter {
	
	private final RestTemplate restTemplate;
	private final EngineUrlResolver engineUrlResolver;
	private static final String CORE_STT_HW_RESOURCE_URL = "/stt/systemStatus";
	
	public SttSystemStatusResponseDto getSystemStatus() {
		String coreUrl = engineUrlResolver.resolve(null); 
		ResponseEntity<SttSystemStatusResponseDto> serverListApiResponseEntity = null;
		SttSystemStatusResponseDto serverListApiResult = null;
		
		try {
			serverListApiResponseEntity = restTemplate.getForEntity(coreUrl + CORE_STT_HW_RESOURCE_URL, SttSystemStatusResponseDto.class);
			} catch(RestClientException e) {
				log.error("[ERROR] systemStatus - RestClientException : {}", e.getMessage());
				throw new SttException(CONNECTION_FAILED);
		}		
			
		if(serverListApiResponseEntity.getStatusCode() == HttpStatus.OK) {
			serverListApiResult = serverListApiResponseEntity.getBody();
		} else {
			log.error("[ERROR] systemStatus - invalid response : {}", serverListApiResponseEntity);
			throw new SttException(INVALID_RESPONSE);
		}		
		
//		SttServerInfoVO dummyServerInfo = getDummyServerInfo();
//		serverListApiResult.getServerInfo().add(dummyServerInfo);
		return serverListApiResult;
	}

	private SttServerInfoVO getDummyServerInfo() {
		SttServerInfoVO dummyServerInfo = new SttServerInfoVO();
		
		dummyServerInfo.setServerName("CPOD2");
		dummyServerInfo.setBps(37L);
		dummyServerInfo.setPps(37L);
		dummyServerInfo.setCpuUsed(0);
		dummyServerInfo.setMaxMemorySize(131837828L);
		dummyServerInfo.setFreeMemorySize(115197396L);
		dummyServerInfo.setMaxAppStorageSize(17005L);
		dummyServerInfo.setFreeAppStorageSize(9786L);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		dummyServerInfo.setLastCheckTime(sdf.format(System.currentTimeMillis()));
		
		return dummyServerInfo;
	}
}
