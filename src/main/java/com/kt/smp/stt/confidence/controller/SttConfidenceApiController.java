package com.kt.smp.stt.confidence.controller;

import static com.kt.smp.stt.common.ResponseMessage.DETAIL_FAIL_MESSAGE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.confidence.domain.SttConfidenceSearchCondition;
import com.kt.smp.stt.confidence.domain.SttConfidenceVO;
import com.kt.smp.stt.confidence.dto.ConfidenceDataDto;
import com.kt.smp.stt.confidence.dto.ConfidenceRequestDto;
import com.kt.smp.stt.confidence.dto.SttConfidenceSearchResponseDto;
import com.kt.smp.stt.confidence.service.SttChartDataBuilder;
import com.kt.smp.stt.confidence.service.SttConfidenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
*@FileName : SttConfidenceApiController.java
@Project : kt-stt-service_r
@Date : 2023. 9. 20.
*@작성자 : wonyoung.ahn
*@변경이력 :
*@프로그램설명 :
*/
@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/confidence/api")
@RequiredArgsConstructor
public class SttConfidenceApiController {

    private final SttConfidenceService sttConfidenceService;
    private final ServiceModelService serviceModelService;
    private final ConfigService configService;
    
    /**
    *@MethodName : saveConfidenceTest
    *@작성일 : 2023. 9. 20.
    *@작성자 : wonyoung.ahn
    *@변경이력 :
    *@Method설명 :
    *@param newConfidence
    *@param project
    *@return
    *@throws JsonProcessingException
    */
    @PostMapping(value = "/stt/confidence/{project}")
    public ResponseEntity<BaseResponseDto<Object>> saveConfidenceTest(@RequestBody ConfidenceRequestDto newConfidence, @PathVariable String project) throws JsonProcessingException {
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	
    	log.info("newConfidence >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(newConfidence));
    	List<ConfigDto> configDtos = configService.getAllUserDefined();
    	for (ConfigDto configDto : configDtos) {
    		if(configDto.getProjectCode().equals(project)) {
    			TenantContextHolder.set(configDto.getProjectCode());	
    		}
    	}
        try {
        	/*
        	 * 서비스모델별 설정된 신뢰도 값에 따라 저장하는 로직
        	 * 설정값을 config테이블에서 조회 후 서비스에서 조건문 로직 수행
        	 * */
        	if (newConfidence.getConfidenceData() == null) {
        		responseDto.setResultCode(ResultCode.STT_SVCRTE_ERROR.getCode());
        		responseDto.setResultMsg("fail");
        	} else {
            	for(ConfidenceDataDto confidenceDataDto : newConfidence.getConfidenceData()) {
                	sttConfidenceService.record(confidenceDataDto);	
            	}
            	
            	responseDto.setResultCode(ResultCode.SUCCESS.getCode());
                responseDto.setResultMsg("success");
        	}
        } catch (Exception e) {
//        	log.error("error : {}",e.getMessage());
        	e.printStackTrace();
        	responseDto.setResultCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
            responseDto.setResultMsg(DETAIL_FAIL_MESSAGE);
        }
        return ResponseEntity.ok(responseDto); 
    }
    
    /**
    *@MethodName : listPage
    *@작성일 : 2023. 9. 20.
    *@작성자 : wonyoung.ahn
    *@변경이력 :
    *@Method설명 :
    *@param searchCondition
    *@return
    */
    @SmpServiceApi(name = "STT 신뢰도 차트 검색", method = RequestMethod.GET, path = "/listPage", type = "검색", description = "STT 신뢰도 목록 검색")
    public ResponseEntity<SttConfidenceSearchResponseDto> listPage(
            @ModelAttribute SttConfidenceSearchCondition searchCondition) {

        List<SttConfidenceVO> confidenceChartData = sttConfidenceService.confidenceChartData(searchCondition);
        
        log.info("confidenceChartData >>> {}", confidenceChartData);

        // 차트
        SttChartDataBuilder builder = new SttChartDataBuilder();

        SttConfidenceSearchResponseDto searchResponseDto = new SttConfidenceSearchResponseDto(confidenceChartData, null, builder.build(confidenceChartData, searchCondition));
        log.info("searchResponseDto >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(searchResponseDto));
        return ResponseEntity.ok(searchResponseDto);
    }
    
}
