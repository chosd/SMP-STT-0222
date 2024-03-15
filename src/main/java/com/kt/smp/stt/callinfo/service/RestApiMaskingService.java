package com.kt.smp.stt.callinfo.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.kt.smp.common.util.crypto.AudioCrypto;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.callinfo.repository.SttCallInfoRepository;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestApiMaskingService {

	private final RestTemplate restTemplate;
	
	/**
	 * 인식결과 Text Masking 요청.
	 * @author JangJoongHwan.
	 * @param apiUrl RestApi 요청 URL
	 * @param text 마스킹 대상 문자열
	 * @see request masking 수신 규격 데이터.
	 * ex) {"text":"2024년02월27일 테스트 데이터 입니다. 전화번호 010에1111에2222이고 생년월일은 001011이요."}
	 * @see response masking 송신 규격 데이터.
	 * ex) {"code": "0", "message": "normal_success", "text": "****년02월27일 테스트 데이터 입니다. 전화번호 ***에****에****이고 생년월일은 ******이요."}
	 * */
	@SuppressWarnings("unchecked")
	public String requestMask(String apiUrl, String text) {
		JSONObject baseForm = null;  // request data form
		JSONObject jsonObj = null;   // response data form
		JSONParser jsonParse = null; // response passing form
		String maskingText = null;
		String decodedText = null;
		
		baseForm = new JSONObject();
		baseForm.put("text", text);
		log.debug(">>> Request Api Masking Data Form : {}", baseForm);
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, baseForm, String.class);
		decodedText = responseEntity.getBody();
        log.debug(">>> Response Api Masking Data Form : {}", decodedText);
        
        jsonParse = new JSONParser();
		try {
			jsonObj = (JSONObject) jsonParse.parse(decodedText);
			maskingText = (String) jsonObj.get("text");
			
		} catch (org.json.simple.parser.ParseException e) {
			maskingText = "Masking Api Reqeust failed : " + e.toString();
		} 
		return maskingText;
	}
}
