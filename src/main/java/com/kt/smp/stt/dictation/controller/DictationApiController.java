package com.kt.smp.stt.dictation.controller;

import static com.kt.smp.stt.common.ResponseMessage.DETAIL_FAIL_MESSAGE;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.dictation.dto.BulkUsageSaveDto;
import com.kt.smp.stt.dictation.dto.ConfidenceConfigDto;
import com.kt.smp.stt.dictation.dto.ConfidenceDto;
import com.kt.smp.stt.dictation.dto.ConfidenceGetResultDto;
import com.kt.smp.stt.dictation.dto.DictationDeleteDto;
import com.kt.smp.stt.dictation.dto.DictationDto;
import com.kt.smp.stt.dictation.dto.DictationSearchCondition;
import com.kt.smp.stt.dictation.dto.DictationToVerifyDataDto;
import com.kt.smp.stt.dictation.dto.DictationUpdateDto;
import com.kt.smp.stt.dictation.dto.ResponseDto;
import com.kt.smp.stt.dictation.service.DictationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api")
public class DictationApiController {

    private final DictationService dictationService;
    
    private static final String CONFIDENCE_PATH = "/stt/confidence/";

    @SmpServiceApi(
            name = "전사데이터 목록 조회",
            method = RequestMethod.GET,
            path = "/dictation",
            type = "조회",
            description = "전사데이터 목록 조회")
    public String search(@ModelAttribute DictationSearchCondition searchCondition) throws JsonProcessingException {

        try {

            int count = dictationService.count(searchCondition);
            return JsonUtil.toJson(HttpResponse.onSuccess(count, 0 < count ? dictationService.search(searchCondition) : null));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "전사데이터 조회",
            method = RequestMethod.GET,
            path = "/dictation/{id}",
            type = "조회",
            description = "전사데이터 조회")
    public String get(@PathVariable("id") Integer id) throws JsonProcessingException {

        try {

            DictationDto dictation = dictationService.get(id);
            return JsonUtil.toJson(HttpResponse.onSuccess(dictation));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure("등록되지 않은 전사데이터입니다"));
        }
    }
    
    @SmpServiceApi(
            name = "전사데이터 ID 조회",
            method = RequestMethod.GET,
            path = "/dictation/id",
            type = "조회",
            description = "전사데이터 조회")
    public String getIdBySttLogId(@RequestParam("sttLogId") Integer sttLogId) throws JsonProcessingException {
        try {
            int dictaionId = dictationService.findIdBySttLogId(sttLogId);
            return JsonUtil.toJson(HttpResponse.onSuccess(dictaionId));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure("등록되지 않은 전사데이터입니다"));
        }
    }
    
    @SmpServiceApi(
            name = "전사데이터 수정",
            method = RequestMethod.POST,
            path = "/dictation/update",
            type = "수정",
            description = "전사데이터 수정")
    public String update(
            HttpServletRequest request,
            @RequestBody DictationUpdateDto modifiedDictation) throws JsonProcessingException {

        try {
            modifiedDictation.audit(request);
            dictationService.update(modifiedDictation);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "데이터구분 일괄등록",
            method = RequestMethod.POST,
            path = "/dictation/usage/save",
            type = "등록",
            description = "데이터구분 일괄등록")
    public String addUsage(HttpServletRequest request, @RequestBody BulkUsageSaveDto usageSave) throws JsonProcessingException {

        try {
            dictationService.saveUsage(request, usageSave);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));

        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }
    
    @SmpServiceApi(
            name = "검증데이터 등록",
            method = RequestMethod.POST,
            path = "/dictation/verify/save",
            type = "등록",
            description = "검증데이터 등록")
    public String addVerifyData(HttpServletRequest request, @RequestBody DictationToVerifyDataDto verifyDataDto) throws JsonProcessingException {
        try {
            dictationService.saveVerifyData(request, verifyDataDto);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "전사데이터 삭제",
            method = RequestMethod.POST,
            path = "/dictation/delete",
            type = "삭제",
            description = "전사데이터 삭제")
    public String deleteList(@RequestBody DictationDeleteDto target) throws JsonProcessingException {

        try {
            dictationService.delete(target);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "전사데이터 선점",
            method = RequestMethod.POST,
            path = "/dictation/{id}/preempt",
            type = "수정",
            description = "전사데이터 선점")
    public String preempt(@PathVariable("id") Integer id) throws JsonProcessingException {

        try {
            dictationService.preempt(id);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "전사데이터 선점 해제",
            method = RequestMethod.POST,
            path = "/dictation/{id}/preempt/free",
            type = "수정",
            description = "전사데이터 선점")
    public String freePreempt(@PathVariable("id") Integer id) throws JsonProcessingException {

        try {
            dictationService.freePreempt(id);
            return JsonUtil.toJson(HttpResponse.onSuccess(true));
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonUtil.toJson(HttpResponse.onFailure(e.getMessage()));
        }
    }

    @SmpServiceApi(
            name = "음원파일 재생",
            method = RequestMethod.GET,
            path = "/dictation/{id}/wav",
            type = "조회",
            description = "음원파일 재생",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getWavFileResource(@PathVariable("id") Integer id) {

        byte[] wavFileBytes = dictationService.getWavFileBytes(id);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(wavFileBytes));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(wavFileBytes.length);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    @SmpServiceApi(
            name = "신뢰도 값 조회",
            method = RequestMethod.POST,
            path = "/dictation/confidence",
            type = "조회",
            description = "신뢰도 값 조회")
    public ResponseEntity<BaseResponseDto<ConfidenceConfigDto>> getConfidence(@RequestBody ConfidenceConfigDto dto) {
    	BaseResponseDto<ConfidenceConfigDto> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

    	log.info("ConfidenceConfigDto >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(dto));
    	
    	try {
    		responseDto.setResult(dictationService.getConfidenceValue(dto.getServiceCode()));
    	} catch (Exception e) {
    		log.error(e.getMessage());
    		ResultCode errorResult = ResultCode.findByCode(e.getMessage());
    		responseDto.setResultCode(errorResult.getCode());
            responseDto.setResultMsg(errorResult.getDescription());
    	}
    	return ResponseEntity.ok(responseDto);
    }
    
    @SmpServiceApi(
            name = "신뢰도 제어",
            method = RequestMethod.POST,
            path = "/dictation/confidence/{type}",
            type = "조회",
            description = "신뢰도 제어")
    public ResponseEntity<BaseResponseDto<Object>> confidenceStart(HttpServletRequest request, @RequestBody ConfidenceDto dto, @PathVariable("type") String type) {
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	try {
    		String serviceModelCode = dto.getServiceCode();
    		String result = dictationService.callApi(CONFIDENCE_PATH, dto, type, serviceModelCode, true);
        	ResponseDto resultInfo = JacksonUtil.jsonToObject(result, ResponseDto.class);
        	log.info("resultInfo >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(resultInfo));
        	
        	int configResult = 0;
        	if((type.equals("set") || type.equals("start") || type.equals("stop")) && resultInfo.getResultCode().equals("0000")) {
                configResult = dictationService.confidenceSave(dictationService.makeInsertData(request, dto, type));
        	}
        	
        	if(configResult > 0) {
    			responseDto.setResultCode(ResultCode.SUCCESS.getCode());
    			responseDto.setResult(type);
        	} else {
        		responseDto.setResultCode(ResultCode.SUCCESS.getCode());
                responseDto.setResultMsg(DETAIL_FAIL_MESSAGE);
        	}
        	
        	responseDto.setResultCode(resultInfo.getResultCode());
        	responseDto.setResultMsg(resultInfo.getResultMsg());
        	
    	} catch (Exception e) {
    		log.error(e.getMessage());
    		responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
    		responseDto.setResultMsg("통신오류가 발생했습니다.");
    	}
    	return ResponseEntity.ok(responseDto);
    }

}
