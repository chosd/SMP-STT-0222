package com.kt.smp.stt.callinfo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kt.smp.base.annotation.SmpServiceApi;
import com.kt.smp.stt.callinfo.dto.CallInfoSearchConditionDto;
import com.kt.smp.stt.callinfo.dto.CallInfoSearchResponseDto;
import com.kt.smp.stt.callinfo.dto.SttCallInfoDto;
import com.kt.smp.stt.callinfo.exception.CallInfoException;
import com.kt.smp.stt.callinfo.service.CallInfoLogExcelDownloadService;
import com.kt.smp.stt.callinfo.service.SttCallInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api")
public class SttCallInfoApiController {
	
	private final SttCallInfoService callInfoService;
	private final CallInfoLogExcelDownloadService callInfoLogExcelDownloadService;
	
		@SmpServiceApi(
	            name = "상담 내역 청취 리스트 조회",
	            method = RequestMethod.GET,
	            path = "/callinfo",
	            type = "조회",
	            description = "상담 내역 청취 리스트 조회")
	    public ResponseEntity<CallInfoSearchResponseDto> search(
	    		@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
	    		@ModelAttribute CallInfoSearchConditionDto callInfoSearchConditionDto) {
	    	
	    	PageHelper.startPage(pageNum, callInfoSearchConditionDto.getPageSize());
	    	Page<SttCallInfoDto> page = callInfoService.search(callInfoSearchConditionDto);
	    	
	    	return ResponseEntity.ok(new CallInfoSearchResponseDto(page));
	    }
	    
	    @SmpServiceApi(
	            name = "상담 내역 청취 상세 조회 (call key로 조회)",
	            method = RequestMethod.GET,
	            path = "/callinfo/{callId}",
	            type = "조회",
	            description = "상담 내역 청취 상세 조회")
	    public ResponseEntity<SttCallInfoDto> searchDetailByCallKey(
	    		@PathVariable("callId") String callId) {
	    	return ResponseEntity.status(HttpStatus.OK).body(callInfoService.getCallInfoByCallId(callId));
	    }
	    
	    @CrossOrigin(origins = "*")
	    @SmpServiceApi(
	            name = "상담 내역 청취 상세 조회 (application id로 조회)",
	            method = RequestMethod.GET,
	            path = "/callinfo2/{applicationId}",
	            type = "조회",
	            description = "상담 내역 청취 상세 조회")
	    public ResponseEntity<SttCallInfoDto> searchDetailByApplicationId(
	    		@PathVariable("applicationId") String applicationId) {
	    	return ResponseEntity.status(HttpStatus.OK).body(callInfoService.getCallInfoByApplicationId(applicationId));
	    }
	    
	    @SmpServiceApi(
	            name = "콜 상태 조회",
	            method = RequestMethod.GET,
	            path = "/callinfo/status",
	            type = "조회",
	            description = "콜 상태 조회")
	    public ResponseEntity<String> searchCallStatus(
	    		@RequestParam String applicationId) {
	    	return ResponseEntity.status(HttpStatus.OK).body(callInfoService.getCallStatusByApplicationId(applicationId));
	    }
	    
	    @SmpServiceApi(
	            name = "상담 내역 청취 리스트 다운로드",
	            method = RequestMethod.GET,
	            path = "/callinfo/download/{applicationId}",
	            type = "조회",
	            description = "상담 내역 청취 리스트 다운로드")
	    public void	callInfoListDownload(@PathVariable String applicationId,
                HttpServletResponse response) throws UnsupportedEncodingException {
			String fileNameUtf8 = URLEncoder.encode(callInfoLogExcelDownloadService.getFileName(applicationId),
					"UTF-8");
			fileNameUtf8 = fileNameUtf8.replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileNameUtf8 + ".xlsx");
			// 엑셀파일 다운로드
			try (OutputStream os = response.getOutputStream()) {
				List<String> headers = callInfoLogExcelDownloadService.getHeaders();
				callInfoLogExcelDownloadService.createWorksheet(os, applicationId, headers);

			} catch (IOException e) {
				log.error("[ERROR] download callInfo log data List : {}", e.getMessage());
			}
	    }
	    
	    @CrossOrigin(origins = "*")
	    @SmpServiceApi(
	            name = "상담 내역 청취 음원 조회",
	            method = RequestMethod.GET,
	            path = "/callinfo/wav/{applicationId}",
	            type = "조회",
	            description = "상담 내역 청취 음원 조회")
	    public ResponseEntity<InputStreamResource> getCallInfoWavFile(@PathVariable String applicationId){
	        byte[] wavFileBytes = callInfoService.getCallInfoWavFile(applicationId);
	        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(wavFileBytes));
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentLength(wavFileBytes.length);
	    	
			return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	    }
	    
	    @ExceptionHandler(CallInfoException.class)
	    public ResponseEntity<String> handleCallInfoException(CallInfoException exception) {
	    	exception.printStackTrace();
	    	return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
	    }
	    
	    @ExceptionHandler(SocketTimeoutException.class)
	    public ResponseEntity<String> handleSocketTimeoutException(SocketTimeoutException exception) {
	    	exception.printStackTrace();
	    	return ResponseEntity.status(500).body("요청 시간이 초과되었습니다. 관리자에게 문의해주세요.");
	    }
	    
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<String> handleException(Exception exception) {
	    	exception.printStackTrace();
	    	return ResponseEntity.status(500).body("알 수 없는 에러가 발생하였습니다.");
	    }
}
