package com.kt.smp.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
public class ParamValidator {

	private boolean accurate; // 검증결과
	private String errorCode; // 에러 코드
	private String errorMessage; // 에러 메세지
	private boolean hashCompare; // 해시검증결과

	public ParamValidator() {
		this.accurate = true;
		this.errorCode = "";
		this.errorMessage = "";
	}

	@SuppressWarnings("unchecked")
	public void nullChk(Object obj, String[] arrCheckParam) {
		if (obj instanceof String) {
			// ERROR
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = mapper.convertValue(obj, Map.class);

		for (int i = 0; i < arrCheckParam.length; i++) {
			if (StringUtils.isEmpty(map.get(arrCheckParam[i]))) {
				this.accurate = false;
				this.errorCode = ApiResponseCode.PARAM_FAIL.getRetCode();
				this.errorMessage = ApiResponseCode.PARAM_FAIL.getRetSysMsg() + " [" + arrCheckParam[i] + "]";
				break;
			}
		}
	}

	/*
	 * 안드로이드 대응 시그널링 차단 url : interface/getBankList/user api body 값이 "" 으로 올시
	 * GlobalExceptionHandler.HttpMessageNotReadableException 에서 네트워크 팝업 처리 되고있음
	 * body 값이 {} 으로 올시 controller에서 파라미터로 체크 하여 네트워크 팝업 처리
	 */
	@SuppressWarnings("unchecked")
	public void nullChk2(Object obj, String[] arrCheckParam) {
		// TODO -
		if (obj instanceof String) {
			// ERROR
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = mapper.convertValue(obj, Map.class);

//		log.error(map.toString());

		for (int i = 0; i < arrCheckParam.length; i++) {
			if (arrCheckParam[i].equals("userNo")) {
				if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase("{}", map.toString())) {
					this.accurate = false;
					this.errorCode = ApiResponseCode.PARAM_FAIL.getRetCode();
					this.errorMessage = ApiResponseCode.PARAM_FAIL.getRetSysMsg();
					break;
				}
			}
		}
	}

//	public void nullChk(String jsonStr, String[] arrCheckParam) throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, String> map = mapper.readValue(jsonStr, new TypeReference<Map<String, String>>() {
//		});
//		for (int i = 0; i < arrCheckParam.length; i++) {
//			if (StringUtils.isEmpty(map.get(arrCheckParam[i]))) {
//				this.accurate = false;
//				this.errorCode = ApiResponseCode.PARAM_FAIL.getRetCode();
//				this.errorMessage = ApiResponseCode.PARAM_FAIL.getRetSysMsg() + " [" + arrCheckParam[i] + "]";
//				break;
//			}
//		}
//	}

	/**
	 * 파라미터 null 체크 - null 체크하여 null 이면 해당 메시지를 담는다
	 * 
	 * @param msg
	 * @param param
	 */
	public void nullChk(String key, String value) {
		if (StringUtils.isEmpty(value)) {
			this.accurate = false;
			this.errorCode = ApiResponseCode.PARAM_FAIL.getRetCode();
			this.errorMessage = ApiResponseCode.PARAM_FAIL.getRetSysMsg() + " [" + key + "]";
		}
	}

	public void nullChk(Object obj) {
		if (StringUtils.isEmpty(obj)) {
			this.accurate = false;
			this.errorCode = ApiResponseCode.PARAM_FAIL.getRetCode();
			this.errorMessage = ApiResponseCode.PARAM_FAIL.getRetSysMsg();
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isAccurate() {
		return accurate;
	}

	public void setAccurate(boolean accurate) {
		this.accurate = accurate;
	}

	public boolean isHashCompare() {
		return hashCompare;
	}

	public void setHashCompare(boolean hashCompare) {
		this.hashCompare = hashCompare;
	}

}
