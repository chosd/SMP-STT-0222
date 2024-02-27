package com.kt.smp.stt.callinfo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.callinfo.dto.CallInfoLogVO;
import com.kt.smp.stt.callinfo.dto.CallInfoSearchConditionDto;
import com.kt.smp.stt.callinfo.dto.StreamUrlParamsVO;
import com.kt.smp.stt.callinfo.dto.SttCallInfoDto;

@Mapper
public interface SttCallInfoMapper {

	public Page<SttCallInfoDto> search(CallInfoSearchConditionDto callInfoSearchConditionDto);

	public SttCallInfoDto getCallInfoByCallId(String callId);
	
	public SttCallInfoDto getCallInfoByApplicationId(String applicationId);
	
	public List<CallInfoLogVO> getCallInfoLogListBySttResultIdx(Integer callInfoId);

	public String getWavFilePathByCallId(String callId);
	
	public String getWavFilePathByApplicationId(String callId);

	public StreamUrlParamsVO getStreamingUrlParamsByCallId(String callId);
	
	public StreamUrlParamsVO getStreamingUrlParamsByApplicationId(String applicationId);

	public void updateWavFilePath(Map<String, String> updateWavFileMap);

	public List<CallInfoLogVO> getCallInfoLogListByApplicationId(String applicationId);
	
	public int countCallInfoLogByApplicationId(String applicationId);

	public String getCallStatusByApplicationId(String applicationId);

}
