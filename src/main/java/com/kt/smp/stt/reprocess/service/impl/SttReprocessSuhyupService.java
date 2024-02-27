/**
 * 
 */
package com.kt.smp.stt.reprocess.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.stt.reprocess.dto.ReprocessLogDto;
import com.kt.smp.stt.reprocess.dto.ReprocessStatusDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessRequestDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessResponseDto;
import com.kt.smp.stt.reprocess.exception.ReprocessError;
import com.kt.smp.stt.reprocess.exception.ReprocessException;
import com.kt.smp.stt.reprocess.repository.SttReprocessRepository;
import com.kt.smp.stt.reprocess.service.SttReprocessService;

import lombok.RequiredArgsConstructor;

/**
* @FileName : SttReprocessApiServiceImpl.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 17.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Service
@RequiredArgsConstructor
public class SttReprocessSuhyupService implements SttReprocessService{

	@Value("${reprocess.url}")
	private String reproecssUrl;
	
	private final SttReprocessRepository sttReprocessRepository; 
	
	private Logger log = LoggerFactory.getLogger(SttReprocessSuhyupService.class);
	
	@Override
	public SttReprocessResponseDto reprocess(List<SttReprocessRequestDto> sttReprocessRequestDto) {
		SttReprocessResponseDto result = new SttReprocessResponseDto();
		
		List<String> callIdList = new ArrayList<>();
		List<String> applicationList = new ArrayList<>();
		
		for (SttReprocessRequestDto dto : sttReprocessRequestDto) {
			dto.setDescription("NULL");
			dto.setSttResults("NULL");
			if (dto.getWavFileName() == null || dto.getWavFileName().equals("")) {
				dto.setWavFileName("NULL");
			}
			
			callIdList.add(dto.getCallId());
			applicationList.add(dto.getApplicationId());
			
		}
		
		result.setCallKeyList(callIdList);
		result.setApplicationList(applicationList);
		
		insertReprocess(sttReprocessRequestDto, applicationList);
		requestReprocess();
		result.setHttpStatus(HttpStatus.NO_CONTENT.value()); // 204
		return result;

	}
	
	/**
	 * 
	 * @MethodName : isApplicationIdDuplicated
	 * @작성일 : 2023. 10. 18.
	 * @작성자 : homin.lee
	 * @변경이력 :
	 * @Method설명 : RECORDS_REC 테이블에 요청된 재처리가 중복인 지 확인
	 * @param applicationId
	 * @return 중복이면 true
	 */
	private boolean isApplicationIdDuplicated(String applicationId) {
		try {
			return sttReprocessRepository.isApplicationIdDuplicated(applicationId);
		} catch (Exception e) {
			throw new ReprocessException(ReprocessError.DB_ERROR, e);
		}
	}
	
	/**
	 * 
	 * @MethodName : requestReprocess
	 * @작성일 : 2023. 10. 18.
	 * @작성자 : homin.lee
	 * @변경이력 :
	 * @Method설명 : 재처리 요청
	 */
	private void requestReprocess() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(1000 * 3);
		factory.setReadTimeout(1000 * 10);
		RestTemplate restTemplate = new RestTemplate(factory);
		try {
			/* HH:MM:SS SSSS 형식 리턴 */
			ResponseEntity<String> responseEntity = restTemplate.exchange(reproecssUrl, HttpMethod.GET, null, String.class);
	        log.info(">>>> 재처리 요청 후 응답 시간 : " + responseEntity.getBody());
		} catch(Exception e) {
			throw new ReprocessException(ReprocessError.FAIL_TO_LINK_REPROCESS_SERVER, e);
		}

	}
	
	/**
	 * 
	* @MethodName : insertAndUpdateReprocess
	* @작성일 : 2023. 10. 24.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 수동 재처리를 위해 RECORDS_REC에 데이터 적재를 적재
	* @param sttReprocessRequestDto
	* @param applicationList
	 */
	private void insertReprocess(List<SttReprocessRequestDto> sttReprocessRequestDto, List<String> applicationList) {
		sttReprocessRepository.insert(sttReprocessRequestDto);
	}

	/**
	 * 
	* @MethodName : getReprocessLog
	* @작성일 : 2023. 10. 31.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 재처리 이력 조회
	 */
	@Override
	public ReprocessLogDto getReprocessLog(String applicationId) {
		ReprocessLogDto result = sttReprocessRepository.getReprocessLog(applicationId);
		if (result == null) {
			throw new ReprocessException(ReprocessError.HISTORY_NOT_FOUND);
		}
		return result;
	}

	/**
	 * 
	* @MethodName : getReprocessStatus
	* @작성일 : 2023. 10. 31.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 재처리 상태 조회 (DB)
	 */
	@Override
	public ReprocessStatusDto getReprocessStatus(String applicationId) {
		ReprocessStatusDto result = sttReprocessRepository.getReprocessStatus(applicationId);
		result.setReprocessStatusCode(result.getReprocessStatus().getStatusCode());
		return result;
	}
	
	/**
	 * 
	* @MethodName : autoReprocess
	* @작성일 : 2023. 11. 01.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 자동 재처리
	 */
	@Override
	public void autoReprocess() {
		sttReprocessRepository.autoReprocess();
		
		requestReprocess();
	}

}
