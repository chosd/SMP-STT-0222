package com.kt.smp.stt.operate.session.controller;

import static com.kt.smp.stt.common.component.SttCmsResultStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.common.exception.SttException;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import com.kt.smp.stt.operate.session.dto.SessionRequestDto;
import com.kt.smp.stt.operate.session.dto.SessionResponseDto;
import com.kt.smp.stt.operate.session.dto.SttSessionResponseDto;
import com.kt.smp.stt.operate.session.service.SttSessionService;
import com.kt.smp.stt.train.trainData.dto.BaseResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
*@FileName : SttSessionApiController.java
@Project : kt-stt-service_r
@Date : 2023. 9. 19.
*@작성자 : wonyoung.ahn
*@변경이력 :
*@프로그램설명 :
*/
@Slf4j
@RestController
@RequestMapping("${smp.service.uri.prefix}/session/api")
@RequiredArgsConstructor
public class SttSessionApiController {

	@Qualifier("sttSuhyupSessionService")
    private final SttSessionService sttSessionService;

    @SmpServiceApi(name = "실시간 세션 정보", method = RequestMethod.GET, path = "/list", type = "목록", description = "실시간 세션 정보")
    public ResponseEntity<BaseResponseDto<Object>> list(@ModelAttribute SessionRequestDto sessionRequestDto, HttpServletRequest request) {
        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);

    	log.info("Session Sort Condition >>> {}", sessionRequestDto.toString());
        
    	sessionRequestDto.setRegId(header.getUserId());
        sessionRequestDto.setRegIp(request.getRemoteAddr());
    	
    	BaseResponseDto<Object> responseDto = new BaseResponseDto<>(SttCmsResultStatus.SUCCESS);

        try {
        	SttSessionResponseDto sttSessionResponseDto = sttSessionService.getCoreChannelApi();

        	log.info("sttSessionResponseDto >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(sttSessionResponseDto));
        	if(sttSessionResponseDto.getResultCode().equals("0000")) {
        		
        		sttSessionService.setEachServerSessionMaxCount(sttSessionResponseDto.getServerInfo());
        		
        		SessionResponseDto sessionResponseDto = new SessionResponseDto();
            	sessionResponseDto.setResultCode(sttSessionResponseDto.getResultCode());
            	sessionResponseDto.setResultMsg(sttSessionResponseDto.getResultMsg());
            	sessionResponseDto.setServerInfo(sttSessionService.convertData(sttSessionResponseDto.getServerInfo(), sessionRequestDto));
            	sessionResponseDto.setDelay(sttSessionService.setDelay());
            	responseDto.setResult(sessionResponseDto);
        	} else {
        		responseDto.setResultCode(sttSessionResponseDto.getResultCode());
        		responseDto.setResultMsg(sttSessionResponseDto.getResultMsg());
        		responseDto.setResult(sttSessionService.setDelay());
        	}
        } catch (SttException e) {
            log.error("[ERROR] channel info : {}", e.getMessage());
            responseDto.setResultCode(e.getStatus().getResultCode());
            responseDto.setResultMsg(e.getStatus().getResultMessage());
        } catch (Exception e) {
            log.error("[ERROR] channel info : {}", e.getMessage());
            responseDto.setResultCode(INTERNAL_SERVER_ERROR.getResultCode());
            responseDto.setResultMsg(INTERNAL_SERVER_ERROR.getResultMessage());
        }

        return ResponseEntity.ok(responseDto);
    }
}
