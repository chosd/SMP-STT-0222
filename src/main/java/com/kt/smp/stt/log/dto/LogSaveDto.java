package com.kt.smp.stt.log.dto;

import java.time.LocalDateTime;

import com.kt.smp.stt.log.type.CallDirection;
import com.kt.smp.stt.log.type.CallStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogSaveDto {

    private String callKey;					// 콜 식별정보
    private String customerIdentifier;		// 고객 식별정보
    private CallDirection direction;		// tx(0),rx(1)
    private LocalDateTime startAt;			// 발화 인입시간	// -> 서버 수신 시간으로 변경함 JHIL
    private LocalDateTime endAt;			// 발화 종료시간	// -> 사용안함 JHIL
    private String wavFilePath;				// 콜 전체 음성파일 저장 경로
    private Integer serviceModelId;			// 
    private Integer serviceCode;			// 
    private String assistantId;				//
    private CallStatus status;				// 정보 없음(X)
    private String failCause;				// 정보 없음(X)
    private String transcript;				// STT결과 문장
    private Long transcriptStart;			// STT 시작시간
    private Long transcriptEnd;				// STT 종료시간
    private Double cer;						// 신뢰도
    
    // 추가
    private String sentenceWavPath;			// 발화 단위의 음성 저장 경로(파일명 포함)
    private String words;					// Word 단위 신뢰도 정보
    
    private Long startTime;					// 발화 시간 (상대 시간)
    private Long endTime;
}
