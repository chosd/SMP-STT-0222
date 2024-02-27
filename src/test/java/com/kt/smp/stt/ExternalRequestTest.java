/**
 * 
 */
package com.kt.smp.stt;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.common.util.ExternalApiRequester;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.dto.BaseResultDto;

import lombok.extern.slf4j.Slf4j;

/**
* @FileName : ExternalRequestTest.java
* @Project : stt-smp-service
* @Date : 2024. 1. 15.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@SpringBootTest(properties = "spring.profiles.active:dev")
@ExtendWith(MockitoExtension.class)
@Slf4j
public class ExternalRequestTest {
	
	@SpyBean
	private ExternalApiRequester externalRequestUtil;
	
	@MockBean
	private RestTemplate restTemplate;
	
	@Test
	@DisplayName("엔진요청성공")
	void api_success() {
		// given
		String resultCode = ResultCode.SUCCESS.getCode();
		String resultMsg = ResultCode.SUCCESS.name();
		
		BaseResultDto expectedResult = BaseResultDto.builder()
								.resultCode(resultCode)
								.resultMsg(resultMsg)
								.build();
		
		 ResponseEntity<Object> mockRestResult = ResponseEntity.ok(expectedResult);
		
		when(restTemplate.postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
		.thenReturn(mockRestResult);
		
		// when
		BaseResultDto resultDto = externalRequestUtil.requestPostWithSavingError("/stt/train", new BaseResultDto());
		
		// then
		Assertions.assertThat(resultDto.getResultCode()).isEqualTo(resultCode);
		Assertions.assertThat(resultDto.getResultMsg()).isEqualTo(resultMsg);
	}
	
	@Test
	@DisplayName("엔진요청실패_0000아님")
	void api_fail_not_0000() {
		// given
		String resultCode = ResultCode.SERVICE_CODE_NOT_FOUND.getCode();
		String resultMsg = ResultCode.SERVICE_CODE_NOT_FOUND.name();
		
		BaseResultDto expectedResult = BaseResultDto.builder()
								.resultCode(resultCode)
								.resultMsg(resultMsg)
								.build();
		
		 ResponseEntity<Object> mockRestResult = ResponseEntity.ok(expectedResult);
		
		when(restTemplate.postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
		.thenReturn(mockRestResult);
		
		// when
		BaseResultDto resultDto = externalRequestUtil.requestPostWithSavingError("/stt/train", new BaseResultDto());
		
		// then
		
		Assertions.assertThat(resultDto.getResultCode()).isEqualTo(resultCode);
		Assertions.assertThat(resultDto.getResultMsg()).isEqualTo(resultMsg);
	}
	
	@Test
	@DisplayName("엔진요청실패_결과가없음")
	void api_fail_null() {
		// given
		String resultCode = ResultCode.INTERNAL_SERVER_ERROR.getCode();
		String resultMsg = ResultCode.INTERNAL_SERVER_ERROR.name();
		
		 ResponseEntity<Object> mockRestResult = ResponseEntity.ok(null);
		
		when(restTemplate.postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
		.thenReturn(mockRestResult);
		
		// when
		BaseResultDto resultDto = externalRequestUtil.requestPostWithSavingError("/stt/train", new BaseResultDto());
		
		// then
		
		Assertions.assertThat(resultDto.getResultCode()).isEqualTo(resultCode);
		Assertions.assertThat(resultDto.getResultMsg()).isEqualTo(resultMsg);
	}
	
	@Test
	@DisplayName("엔진요청실패_예외발생_ResourceAccessException")
	void api_fail_ResourceAccessException() {
		// given
		String resultCode = ResultCode.STT_AGENT_SERVER_ERROR.getCode();
		String resultMsg = ResultCode.STT_AGENT_SERVER_ERROR.name();
		
		when(restTemplate.postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
		.thenThrow(ResourceAccessException.class);
		
		// when
		BaseResultDto resultDto = externalRequestUtil.requestPostWithSavingError("/stt/train", new BaseResultDto());
		
		// then
		
		Assertions.assertThat(resultDto.getResultCode()).isEqualTo(resultCode);
		Assertions.assertThat(resultDto.getResultMsg()).isEqualTo(resultMsg);
	}

}
