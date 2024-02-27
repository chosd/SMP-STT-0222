package com.kt.smp.common.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @title  RestApiUtil
 * @author kjk
 * @since  2022. 04. 15.
 */
@Slf4j
@SuppressWarnings("serial")
public class RestApiUtil {
	public static ApiResponseMessage isValidateParameter(Object obj, String[] arrCheckParam) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName() != null ? new Object() {}.getClass().getEnclosingMethod().getName() : "";
		log.debug("[{}] Enter ===========================", methodName);
       
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = mapper.convertValue(obj, Map.class);

		String retcode = "";
		String retmsg = "";
		String errorMessage = " 필수값없음";
		Boolean validateYn = false;
		
		for (int i = 0; i < arrCheckParam.length; i++) {
			if (StringUtils.isEmpty(map.get(arrCheckParam[i]))) {
				validateYn = true;
				retcode = ApiResponseCode.PARAM_FAIL.getRetCode();
				retmsg = ApiResponseCode.PARAM_FAIL.getRetCode();
				errorMessage += " [" + arrCheckParam[i] + "]";
			}
		}
		
		if( !validateYn ) {
			return null;
		} else {
			return new ApiResponseMessage(retcode, retmsg, errorMessage);
		}
	}
	
	/**
	 * @param jsonElement
	 * @apiNote JSONObject를 Map<String, String> 형식으로 변환처리.
	 * @return Map<String,String>
	 * **/
	public static Map<String, Object> getMapFromJsonObject(JsonElement jsonElement){
	    Map<String, Object> map = null;
	    
	    try {
	       map = new ObjectMapper().readValue(jsonElement.toString(), Map.class);
	    } catch (JsonParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (JsonMappingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return map;
	}
}
