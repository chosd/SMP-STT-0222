package com.kt.smp.stt.callinfo.dto;

import lombok.Data;

@Data
public class CallInfoLogVO {
	private Long sttResultIdx;			// 테이블 pk
	private String applicationId;		// 상담 ID
	private String sttId;					// STT ID
	private String speakerType; 		// TX , RX
	private Integer sttSeq;				// STT SEQ
	private Integer startTimeStamp;		// 시작 타임스탬프
	private Integer endTimeStamp;		// 종료 타임스탬프
	private String sttJson;				// STT JSON 인식 결과
	private String sttText;				// STT 인식결과
	private String confidence;			// STT 신뢰도 값
	private String startTime;			// 발화 시작 시간
	private String endTime;				// 발화 종료 시간
	
	private String[] words;
	private String transcript;
}
