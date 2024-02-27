package com.kt.smp.stt.verify.history.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dto.HttpResponse;
import com.dev.func.dataconverter.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.verify.history.dto.VerifyHistoryDto;
import com.kt.smp.stt.verify.history.dto.VerifyHistorySaveYnDto;
import com.kt.smp.stt.verify.history.dto.VerifyHistorySearchCondition;
import com.kt.smp.stt.verify.history.dto.VerifyHistoryStatusDto;
import com.kt.smp.stt.verify.history.service.VerifyHistoryService;
import com.kt.smp.stt.verify.request.controller.VerifyRequestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api/verify")
public class VerifyHistoryApiController {

    private final VerifyHistoryService historyService;
    private final ConfigService configService;
    
    @SmpServiceApi(
            name = "검증이력 목록 조회",
            method = RequestMethod.GET,
            path = "/history",
            type = "조회",
            description = "검증이력 목록 조회")
    public String search(@ModelAttribute VerifyHistorySearchCondition searchCondition) throws JsonProcessingException {
    	log.info("searchCondition >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(searchCondition));
    	setCurrentTenantContext();
        try {

            int count = historyService.count(searchCondition);
            return JsonUtil.toJson(HttpResponse.onSuccess(count, 0 < count ? historyService.search(searchCondition) : null));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
    
    @SmpServiceApi(
            name = "검증이력 조회",
            method = RequestMethod.GET,
            path = "/history/{id}",
            type = "조회",
            description = "검증이력 조회")
    public String get(@PathVariable("id") int id) throws JsonProcessingException {
    	setCurrentTenantContext();
        try {
            VerifyHistoryDto history = historyService.get(id);
            return JsonUtil.toJson(HttpResponse.onSuccess(history));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
    
    @SmpServiceApi(
            name = "검증이력 상태 조회",
            method = RequestMethod.GET,
            path = "/history/status",
            type = "조회",
            description = "검증이력 상태 조회")
    public ResponseEntity<VerifyHistoryStatusDto> getStatus(
    		@RequestParam("serviceModelId") String serviceCode
    		,@RequestParam("verifyId") Integer verifyId
    		) {
    	setCurrentTenantContext();
    	VerifyHistoryStatusDto history = historyService.getStatus(serviceCode, verifyId);
        return ResponseEntity.ok(history);

    }
    
    private void setCurrentTenantContext() {
    	List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		TenantContextHolder.set(configDto.getProjectCode());	
    	}
    }
    
    @SmpServiceApi(
            name = "인식률 반영",
            method = RequestMethod.POST,
            path = "/history/updateYn",
            type = "조회",
            description = "인식률 반영")
    public ResponseEntity<BaseResponseDto<Object>> updateYn(@RequestBody VerifyHistorySaveYnDto dto) throws JsonProcessingException {
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	log.info("historyId >>> {}",dto.getHistoryId());
        try {
            int result = historyService.updateSaveYn(dto.getHistoryId());
            if(result > 0) {
            	responseDto.setResultCode(ResultCode.SUCCESS.getCode());
                responseDto.setResultMsg("success");
            } else {
            	responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
                responseDto.setResultMsg("fail");
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
            responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg("fail");
        }
        return ResponseEntity.ok(responseDto);
    }
}
