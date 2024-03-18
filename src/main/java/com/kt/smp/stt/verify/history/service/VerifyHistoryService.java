package com.kt.smp.stt.verify.history.service;

import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.verify.history.dto.*;
import com.kt.smp.stt.verify.history.repository.VerifyHistoryRepository;
import com.kt.smp.stt.verify.history.type.VerifyStatus;
import com.kt.smp.stt.verify.request.dto.VerifyCallbackDto;
import com.kt.smp.stt.verify.request.dto.VerifyRequestDto;
import com.kt.smp.stt.verify.request.dto.VerifyStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerifyHistoryService {

    private static final String CORE_VERIFY_STATUS_URL = "/stt/verify/status";

	
    private final VerifyHistoryRepository historyRepository;
    private final ServiceModelService serviceModelService;
    private final RestTemplate restTemplate;
    private final EngineUrlResolver engineUrlResolver;
    public int count(VerifyHistorySearchCondition searchCondition) {
        return historyRepository.count(searchCondition);
    }

    public List<VerifyHistoryListDto> search(VerifyHistorySearchCondition searchCondition) {
        return historyRepository.search(searchCondition);
    }

    public VerifyHistoryDto get(int id) {
        return historyRepository.findById(id);
    }

    public void save(VerifyRequestDto verifyRequest) {
        VerifyHistorySaveDto newHistory = VerifyHistorySaveDto.from(verifyRequest);
        log.info("newHistory >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(newHistory));
        historyRepository.save(newHistory);
    }

    public void updateStatus(VerifyHistoryDto history, VerifyStatusResponseDto newStatus) {

        ResultCode code = ResultCode.findByCode(newStatus.getResultCode());
        VerifyStatus status = VerifyStatus.findByCode(newStatus.getStatus());
        VerifyHistoryUpdateDto modifiedHistory = new VerifyHistoryUpdateDto(
                history.getId(),
                status,
                newStatus.getCer(),
                newStatus.getWer(),
                code.getDescription());

        historyRepository.update(modifiedHistory);

    }

    @Transactional
    public void updateByCallback(VerifyCallbackDto callback) {

        ServiceModelVO serviceModel = serviceModelService.detailByServiceCode(callback.getServiceCode());
        if (serviceModel == null) {
            throw new IllegalArgumentException("등록되지 않은 서비스코드 입니다");
        }

        ResultCode resultCode = ResultCode.findByCode(callback.getResultCode());
        if (resultCode.equals(ResultCode.SUCCESS)) {
            successOnVerifying(serviceModel, callback);
            return;
        }

        failOnVerifying(serviceModel, resultCode);
    }

    private void successOnVerifying(ServiceModelVO serviceModel, VerifyCallbackDto callback) {
    	CallbackUpdateDto callbackUpdateDto = new CallbackUpdateDto();
    	callbackUpdateDto.setId(Long.parseLong( serviceModel.getServiceCode()));
    	callbackUpdateDto.setStatus(VerifyStatus.VERIFYING);
        VerifyHistoryDto history = historyRepository.findLatestByServiceModelIdAndStatus(callbackUpdateDto);
        log.info("history >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(history));
        if (history == null) {
            return;
        }

        VerifyHistoryUpdateDto modifiedHistory = new VerifyHistoryUpdateDto(
                history.getId(),
                VerifyStatus.COMPLETE,
                callback.getCer(),
                callback.getWer(),
                "성공");
        log.info(" before verify callback modifiedHistory >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(modifiedHistory));
        historyRepository.update(modifiedHistory);
    }

    private void failOnVerifying(ServiceModelVO serviceModel, ResultCode resultCode) {
    	CallbackUpdateDto callbackUpdateDto = new CallbackUpdateDto();
    	callbackUpdateDto.setId(Long.parseLong(serviceModel.getServiceCode()) );
    	callbackUpdateDto.setStatus(VerifyStatus.VERIFYING);
        VerifyHistoryDto history = historyRepository.findLatestByServiceModelIdAndStatus(callbackUpdateDto);

        if (history == null) {
            return;
        }

        VerifyHistoryUpdateDto modifiedHistory = new VerifyHistoryUpdateDto(
                history.getId(),
                VerifyStatus.FAIL,
                0.0,
                0.0,
                resultCode.getDescription());

        historyRepository.update(modifiedHistory);
    }
    
    public int updateSaveYn(int id) {
    	return historyRepository.updateSaveYn(id);
    }

	public VerifyHistoryStatusDto getStatus(String serviceCode, Integer verifyId) {
		log.info("VerifyHistoryService.getStatus >>> serviceCode = {}, verifyId = {}", serviceCode, verifyId);
        String coreUrl = engineUrlResolver.resolve();
        String verifyStatusUrl = CORE_VERIFY_STATUS_URL + "?serviceCode=" + serviceCode;
        ResponseEntity<VerifyHistoryStatusDto> restResult;
        VerifyHistoryStatusDto response = new VerifyHistoryStatusDto();
        if(coreUrl.contains("https")) {
		    ignoreSSL();
		}
        try {
        	restResult = restTemplate.getForEntity(coreUrl + verifyStatusUrl, VerifyHistoryStatusDto.class);
        	response = restResult.getBody();
            log.info("VerifyHistoryService.getVerifyStatus Rest response >>> {}",response);
            if (response.getResultCode().equals("0000")) {
            	updateVerifyStatus(verifyId, response.getStatus());
            }
            
            response.setResultCodeMsg(ResultCode.findByCode(response.getResultCode()).getDescription());
        }
        catch(HttpClientErrorException e) {
        	int jsonStartIdx = e.getMessage().indexOf("{");
        	String jsonStr = e.getMessage().substring(jsonStartIdx);
        	Map<String,Object> errorResponse = JacksonUtil.jsonToMap(jsonStr);
        	String resultCode = errorResponse.get("resultCode").toString();
        	log.error(ResultCode.findByCode(resultCode).getDescription());
        	response.setResultCode(resultCode);
        	response.setResultCodeMsg("원인 : " + ResultCode.findByCode(resultCode).getDescription());
        }
        
        return response;
	}

	private void updateVerifyStatus(Integer verifyId, VerifyStatus status) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", verifyId);
		params.put("status", status.getCode());
		historyRepository.updateVerifyStatus(params);
	}
	
	private void ignoreSSL() {

	    TrustManager[] trustAllCerts = new TrustManager[] {
	        new X509TrustManager() {

	            @Override
	            public void checkClientTrusted(X509Certificate[] chain, String authType) {

	            }

	            @Override
	            public void checkServerTrusted(X509Certificate[] chain, String authType) {

	            }

	            @Override
	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        }
	    };

	    try {
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	    } catch (Exception e) {
	        log.error("[ERROR] ignoreSSL : {}", e.getMessage());
	    }

	}
}
