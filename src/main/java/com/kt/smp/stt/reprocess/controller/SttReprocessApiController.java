/**
 * 
 */
package com.kt.smp.stt.reprocess.controller;

import java.net.SocketTimeoutException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.reprocess.dto.ReprocessLogDto;
import com.kt.smp.stt.reprocess.dto.ReprocessStatusDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessRequestDto;
import com.kt.smp.stt.reprocess.dto.SttReprocessResponseDto;
import com.kt.smp.stt.reprocess.exception.ReprocessException;
import com.kt.smp.stt.reprocess.service.SttReprocessService;
import com.kt.smp.stt.reprocess.service.impl.SttReprocessSuhyupService;

import lombok.RequiredArgsConstructor;

/**
* @FileName : SttReprocessApiController.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 16.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api")
public class SttReprocessApiController {
	
	private final SttReprocessService sttReprocessApiService;
	private Logger log = LoggerFactory.getLogger(SttReprocessSuhyupService.class);

    @SmpServiceApi(
            name = "재처리 상태 조회",
            method = RequestMethod.GET,
            path = "/reprocess/status",
            type = "조회",
            description = "재처리 상태 조회")
    public ResponseEntity<ReprocessStatusDto> getReprocessStatus(
    		@RequestParam("applicationId") String applicationId) {
    	ReprocessStatusDto result = sttReprocessApiService.getReprocessStatus(applicationId);
    	
    	return ResponseEntity.ok(result);
    }
    
    @SmpServiceApi(
            name = "재처리 이력 조회",
            method = RequestMethod.GET,
            path = "/reprocess/log",
            type = "조회",
            description = "재처리 이력 조회")
    public ResponseEntity<ReprocessLogDto> getReprocessLog(@RequestParam("applicationId") String applicationId) {
    	ReprocessLogDto result = sttReprocessApiService.getReprocessLog(applicationId);
    	
    	return ResponseEntity.ok(result);
    }
    
    @SmpServiceApi(
            name = "재처리 요청",
            method = RequestMethod.POST,
            path = "/reprocess",
            type = "조회",
            description = "재처리 요청")
    public ResponseEntity<SttReprocessResponseDto> reprocess(
    		@RequestBody List<SttReprocessRequestDto> sttReprocessRequestDto) {
    	
    	SttReprocessResponseDto result = sttReprocessApiService.reprocess(sttReprocessRequestDto);
        return ResponseEntity.ok(result);
    }
    
    @ExceptionHandler(ReprocessException.class)
    public ResponseEntity<String> handleReprocessException(ReprocessException exception) {
    	log.error(exception.getMessage() + exception);
    	
    	return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }
    
    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<String> handleSocketTimeoutException(SocketTimeoutException exception) {
    	exception.printStackTrace();
    	return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("요청 시간이 초과되었습니다. 관리자에게 문의해주세요.");
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
    	log.error("[SttReprocessApiController] --> ", exception.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 에러가 발생하였습니다.");
    }
    
}
