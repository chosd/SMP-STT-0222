package com.kt.smp.stt.error.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.error.dto.SttErrorInsertDto;
import com.kt.smp.stt.error.dto.SttErrorListDto;
import com.kt.smp.stt.error.dto.SttErrorSearchDto;
import com.kt.smp.stt.error.dto.SttErrorTypeDto;
import com.kt.smp.stt.error.enums.SttErrorEndpoint;
import com.kt.smp.stt.error.repository.SttErrorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SttErrorService {
	
	private final SttErrorRepository sttErrorRepository;
	
	public int insert(List<SttErrorInsertDto> dto) {
		return sttErrorRepository.insert(dto);
	}
	
	public List<SttErrorTypeDto> selectType() {
		return sttErrorRepository.selectType();
	}
	
	public Page<SttErrorListDto> list(SttErrorSearchDto sttErrorSearchDto) {
		return sttErrorRepository.list(sttErrorSearchDto);
	}
	
	public void setEndpoint(SttErrorListDto dto) {
		if (!dto.getType().equals("IF")) {
			return;
		}
		
		String apiUrl = dto.getEtc();
		
		if (apiUrl == null) {
			return;
		}
		
		if (apiUrl.contains(SttErrorEndpoint.TRAIN.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.TRAIN.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.VERIFY.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.VERIFY.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.DEPLOY.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.DEPLOY.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.TEST.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.TEST.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.CONFIDENCE.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.CONFIDENCE.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.DOWNLOAD.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.DOWNLOAD.getEndpointKor());
			return;
		}
		
		if (apiUrl.contains(SttErrorEndpoint.SESSION.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.SESSION.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.STATISTICS.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.STATISTICS.getEndpointKor());
			return;
		}
		if (apiUrl.contains(SttErrorEndpoint.SYSTEM_STATUS.getEndpointEng())) {
			dto.setEndpoint(SttErrorEndpoint.SYSTEM_STATUS.getEndpointKor());
			return;
		}
	}
	
	public void saveErrorLog(String jsonString, String errorType, String url) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			BaseResultDto dto = objectMapper.readValue(jsonString, BaseResultDto.class);
			saveErrorLog(dto, errorType, url);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void saveErrorLog(BaseResultDto dto, String type, String apiUrl) {
		
		log.info("[SttErrorService.saveErrorLog] Start saving error log");
		
		List<SttErrorInsertDto> sttErrorInsertDtoList = new ArrayList<>();
		SttErrorInsertDto sttErrorInsertDto = new SttErrorInsertDto();
		sttErrorInsertDto.setType(type);
		sttErrorInsertDto.setErrorCode(dto.getResultCode());
		sttErrorInsertDto.setErrorMsg(dto.getResultMsg());
		sttErrorInsertDto.setApiUrl(apiUrl);
		log.info("sttErrorInsertDto >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(sttErrorInsertDto));
		sttErrorInsertDtoList.add(sttErrorInsertDto);
		
		insert(sttErrorInsertDtoList);
	}
	
}
