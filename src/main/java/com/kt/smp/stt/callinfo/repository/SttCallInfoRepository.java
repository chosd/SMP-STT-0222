package com.kt.smp.stt.callinfo.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.callinfo.dto.CallInfoLogVO;
import com.kt.smp.stt.callinfo.dto.CallInfoSearchConditionDto;
import com.kt.smp.stt.callinfo.dto.StreamUrlParamsVO;
import com.kt.smp.stt.callinfo.dto.SttCallInfoDto;
import com.kt.smp.stt.callinfo.mapper.SttCallInfoMapper;

@Repository
public class SttCallInfoRepository {

	private final SttCallInfoMapper mapper;
	
	public SttCallInfoRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttCallInfoMapper.class);
    }
	
	public Page<SttCallInfoDto> search(CallInfoSearchConditionDto callInfoSearchConditionDto) {
		return mapper.search(callInfoSearchConditionDto);
	}

	public List<CallInfoLogVO> getCallInfoLogListBySttResultIdx(Integer sttResultIdx) {
		return mapper.getCallInfoLogListBySttResultIdx(sttResultIdx);
	}
	
	public String getWavFilePathByCallId(String callId) {
		return mapper.getWavFilePathByCallId(callId);
	}
	
	public String getWavFilePathByApplicationId(String applicationId) {
		return mapper.getWavFilePathByApplicationId(applicationId);
	}

	public StreamUrlParamsVO getStreamingUrlParamsByCallId(String callId) {
		return mapper.getStreamingUrlParamsByCallId(callId);
	}
	
	public StreamUrlParamsVO getStreamingUrlParamsByApplicationId(String callId) {
		return mapper.getStreamingUrlParamsByApplicationId(callId);
	}

	public SttCallInfoDto getCallInfoByCallId(String callId) {
		return mapper.getCallInfoByCallId(callId);
	}
	
	public SttCallInfoDto getCallInfoByApplicationId(String applicationId) {
		return mapper.getCallInfoByApplicationId(applicationId);
	}

	public void updateWavFilePath(Map<String, String> updateWavFileMap) {
		mapper.updateWavFilePath(updateWavFileMap);
	}

	public List<CallInfoLogVO> getCallInfoLogListByApplicationId(String applicationId) {
		return mapper.getCallInfoLogListByApplicationId(applicationId);
	}
	
	public int countCallInfoLogByApplicationId(String applicationId) {
		return mapper.countCallInfoLogByApplicationId(applicationId);
	}

	public String getCallStatusByApplicationId(String applicationId) {
		return mapper.getCallStatusByApplicationId(applicationId);
	}
	
}
