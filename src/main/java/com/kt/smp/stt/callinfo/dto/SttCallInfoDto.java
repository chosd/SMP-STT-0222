package com.kt.smp.stt.callinfo.dto;

import java.util.List;

import com.kt.smp.stt.callinfo.enums.CallStatus;
import com.kt.smp.stt.reprocess.enums.ReprocessStatus;

import lombok.Data;

@Data
public class SttCallInfoDto {
	
	/*	CALL_INFO TABLE COLUMNS  */
	private Integer	sttCallIdx; 					// 콜 정보 ID
	private String applicationId;					// 상담사 ID
	private String recId;							// 녹취 ID
	private String callId;							// 콜키 정보
	private String sttId;							// STT ID
	private String deviceId;						// 요청 Client ID
	private String serviceCode; 					// 서비스 모델 ID
	private String serviceModelName; 				// 서비스 모델 ID
	private CallStatus callStatus;					// 콜 상태 ( START: 1000, Progress: 2000, END: 3000, 재처리: 4000 )
	private String callStartTime;					// 콜 시작시간
	private String callEndTime;						// 콜 종료시간
	private String wavFilePath;						// 음원파일 경로
	private String wavFileName;						// 음원파일 경로
	private String reprocessApplicationId;			// 재처리 중일 때 application is not null
	private List<CallInfoLogVO> conversationList;	// 대화록
}
