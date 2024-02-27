package com.kt.smp.stt.callinfo.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SttCallInfoDetailResponseDto {

	private Long id;				// id
	private Long callInfoId;
	private String callKey;			// 콜키
	private String wavFilePath;		// 음원 경로
	
	private List<CallInfoLogVO> callInfoLog;	// 대화록
	
}
