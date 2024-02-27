/**
 * 
 */
package com.kt.smp.common.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.ErrorTypeCode;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.error.service.SttErrorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @FileName : ExternalRequestUtil.java
* @Project : stt-smp-service
* @Date : 2024. 1. 15.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : 외부 API 요청 유틸
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalApiRequester {
	
	private final RestTemplate restTemplate;
	
	private final EngineUrlResolver engineUrlResolver;
	
	private final SttErrorService sttErrorService;
	
	/**
	 * 
	* @MethodName : requestWithLog
	* @작성일 : 2024. 1. 16.
	* @작성자 : rapeech
	* @변경이력 :
	* @Method설명 : POST를 이용한 외부 API 요청 실패 시 장애 이력 저장 (HOST 미 특정)
	* @param apiPath 요청 API Path (ex. "/stt/train")
	* @param requestDto 요청에 담을 파라미터
	* @return
	 */
	public BaseResultDto requestPostWithSavingError(String apiPath, Object requestDto) {
		return requestPostWithSavingError(apiPath, engineUrlResolver.resolve(), requestDto);
	}
	
	/**
	 * 
	* @MethodName : requestWithSavingError
	* @작성일 : 2024. 1. 16.
	* @작성자 : rapeech
	* @변경이력 :
	* @Method설명 : POST를 이용한 외부 API 요청 실패 시 장애 이력 저장 (HOST 특정)
	* @param apiPath - 요청 API Path (ex. "/stt/train")
	* @param hostUrl - 특정 Host (Host or Sub). 실제로는 해당 아이피 주소가 들어와야 함 
	* @param requestDto - 요청에 담을 파라미터
	* @return
	 */
	public BaseResultDto requestPostWithSavingError(String apiPath, String hostUrl, Object requestDto) {
		
		String requestUrl = hostUrl + apiPath;
		
		BaseResultDto responseDto = new BaseResultDto();
		
		try {
			ResponseEntity<BaseResultDto> restResultDto = restTemplate.postForEntity(requestUrl, requestDto, BaseResultDto.class);
			
			BaseResultDto resultDto = restResultDto.getBody();
			
			log.info("[" + apiPath + "] restResultDto >>> " + resultDto.toString());
			
			if(ObjectUtils.isEmpty(resultDto)) {
				ResultCode resultCode = ResultCode.INTERNAL_SERVER_ERROR;
	            
	            setResponseDto(responseDto, resultCode);
				saveErrorLog(apiPath, requestUrl, responseDto);
	            
	            return responseDto;
			}
			
			if (!ResultCode.SUCCESS.getCode().equals(resultDto.getResultCode())) {
		
				ResultCode resultCode = ResultCode.findByCode(resultDto.getResultCode());
				setResponseDto(responseDto, resultCode);
				saveErrorLog(apiPath, requestUrl, responseDto);
				return responseDto;
			} 
			
			setResponseDto(responseDto, resultDto);
			
        } catch (HttpClientErrorException ex) {
            log.error("[ERROR] {}", ex.getMessage());
            BaseResultDto errorResult = parseRequestError(ex.getResponseBodyAsString());
            setResponseDto(responseDto, errorResult);
            saveErrorLog(apiPath, requestUrl, responseDto);
        } catch (ResourceAccessException ex) {
            log.error("[ERROR] {}", ex.getMessage());
            setResponseDto(responseDto, ex);
            saveErrorLog(apiPath, requestUrl, responseDto);
        } catch (Exception ex) {
        	log.error("[ERROR] {}", ex.getMessage());
            setResponseDto(responseDto, ex);
            saveErrorLog(apiPath, requestUrl, responseDto);
        }         
		return responseDto;
	}

	private void saveErrorLog(String apiPath, String requestUrl, BaseResultDto responseDto) {
		log.info("[ExternalRequestUtil.request] "+ apiPath +" Error saving error Log");
		sttErrorService.saveErrorLog(responseDto, ErrorTypeCode.IF, requestUrl);
	}
	
	private BaseResultDto parseRequestError(String errorMessage) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(errorMessage, BaseResultDto.class);

        } catch (JsonProcessingException e) {
            return new BaseResultDto(
                    ResultCode.UNKNOWN_SERVER_ERROR.getCode(),
                    ResultCode.UNKNOWN_SERVER_ERROR.getDescription()
            		);
        }
	}
	
	private void setResponseDto(BaseResultDto responseDto, Exception ex) {
		if (ex instanceof HttpClientErrorException) {
			setResponseDto(responseDto, ResultCode.STT_AGENT_SERVER_ERROR.getCode(), ResultCode.STT_AGENT_SERVER_ERROR.name());
			return;
		}
		
		if (ex instanceof ResourceAccessException) {
			setResponseDto(responseDto, ResultCode.UNKNOWN_SERVER_ERROR.getCode(), ResultCode.UNKNOWN_SERVER_ERROR.name());
			return;
		}
		setResponseDto(responseDto, ResultCode.UNKNOWN_SERVER_ERROR.getCode(), ResultCode.UNKNOWN_SERVER_ERROR.name());
	}

	private void setResponseDto(BaseResultDto responseDto, BaseResultDto resultDto) {
		setResponseDto(responseDto, resultDto.getResultCode(), resultDto.getResultMsg());
	}

	private void setResponseDto(BaseResultDto responseDto, ResultCode resultCode) {
		setResponseDto(responseDto, resultCode.getCode(), resultCode.name());
	}
	
	private void setResponseDto(BaseResultDto responseDto, String code, String resultMsg) {
		responseDto.setResultCode(code);
		responseDto.setResultMsg(resultMsg);
	}
	
}
