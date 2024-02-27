package com.kt.smp.stt.error.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.dto.BaseResponseDto;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.error.dto.SttErrorListDto;
import com.kt.smp.stt.error.dto.SttErrorResponseDto;
import com.kt.smp.stt.error.dto.SttErrorSearchDto;
import com.kt.smp.stt.error.service.SttErrorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/error/api")
@RequiredArgsConstructor
public class SttErrorApiController {

    private final SttErrorService sttErrorService;
    
    @SmpServiceApi(name = "장애이력 조회"
    			, method = RequestMethod.GET
    			, path = "/list"
    			, type = "조회"
    			, description = "장애이력")
    public ResponseEntity<BaseResponseDto<SttErrorResponseDto>> list(
    		@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
    		@ModelAttribute SttErrorSearchDto sttErrorSearchDto) {
    	
    	BaseResponseDto<SttErrorResponseDto> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);
    	
    	PageHelper.startPage(pageNum, sttErrorSearchDto.getPageSize());
    	
    	log.info("sttErrorSearchDto >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(sttErrorSearchDto));
    	Page<SttErrorListDto> pagingResult = sttErrorService.list(sttErrorSearchDto);
    	
    	for (SttErrorListDto dto : pagingResult) {
    		sttErrorService.setEndpoint(dto);
    	}
    		
    	SttErrorResponseDto sttErrorResponseDto = new SttErrorResponseDto(pagingResult);
    	responseDto.setResult(sttErrorResponseDto);
    	
        return ResponseEntity.ok(responseDto);
    }
}
