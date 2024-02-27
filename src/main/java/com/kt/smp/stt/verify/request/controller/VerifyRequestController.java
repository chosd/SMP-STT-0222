package com.kt.smp.stt.verify.request.controller;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.config.CommonConstants;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.verify.history.dto.VerifyHistoryDto;
import com.kt.smp.stt.verify.request.dto.VerifyCallbackDto;
import com.kt.smp.stt.verify.request.dto.VerifyRequestDto;
import com.kt.smp.stt.verify.history.service.VerifyHistoryService;
import com.kt.smp.stt.verify.request.dto.VerifyResponseDto;
import com.kt.smp.stt.verify.request.dto.VerifyStatusResponseDto;
import com.kt.smp.stt.verify.request.service.VerifyEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api/verify")
public class VerifyRequestController {

    private final VerifyEngineService engineService;
    private final VerifyHistoryService historyService;
    
    @Value("${spring.profiles.active}")
    private String profile;

    @SmpServiceApi(
            name = "검증요청",
            method = RequestMethod.POST,
            path = "/request",
            type = "등록",
            description = "검증요청")
    public String request(
            HttpServletRequest request,
            @RequestBody VerifyRequestDto verifyRequest) throws JsonProcessingException {

        try {
            verifyRequest.audit(request);
            if (!profile.equals(CommonConstants.LOCAL_PROFILE)) {
            	
            	BaseResultDto resultDto = engineService.requestVerify(verifyRequest);
            	
            	if (resultDto == null) {
                    return JsonUtil.toJson(HttpResponse.onFailure("해당 검증데이터셋에 검증데이터가 없습니다."));
            	}
            	
            	if (resultDto.getResultCode().equals("0000")) {
                    historyService.save(verifyRequest);
                    return JsonUtil.toJson(HttpResponse.onSuccess(true));
            	} else {
            		String description = ResultCode.findByCode(resultDto.getResultCode()).getDescription();
                    return JsonUtil.toJson(HttpResponse.onFailure(description));
            	}
            	
            } else {
            	historyService.save(verifyRequest);
            	VerifyCallbackDto callback = new VerifyCallbackDto();
            	callback.setResultCode("0000");
            	callback.setResultMsg("success");
            	callback.setServiceCode(Integer.toString(verifyRequest.getServiceModelId()));
            	callback.setStatus("COMPLETE");
            	callback.setCer(0);
            	callback.setWer(0);
            	historyService.updateByCallback(callback);
            }
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "검증요청(NAS 미사용)",
            method = RequestMethod.POST,
            path = "/requestMultipart",
            type = "등록",
            description = "검증요청")
    public String requestMultipart(
            HttpServletRequest request,
            @RequestBody VerifyRequestDto verifyRequest) throws JsonProcessingException {
        try {
            verifyRequest.audit(request);
            // 검증 요청 전 해당 데이터셋 기준으로 파일 압축(tar.gz)
            engineService.tarGzCompression(verifyRequest);
            
            BaseResultDto resultDto = engineService.requestMultipartVerify(verifyRequest);
        	if (resultDto != null && resultDto.getResultCode().equals("0000")) {
            	historyService.save(verifyRequest);
            }
            
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<VerifyResponseDto> executeCallback(@RequestBody VerifyCallbackDto callback) {
    	log.info("callback >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(callback));
        try {
            historyService.updateByCallback(callback);
            return ResponseEntity.ok(VerifyResponseDto.success());
        } catch (Throwable ex) {
            return ResponseEntity.ok(VerifyResponseDto.fail(ex.getMessage()));
        }
    }
}
