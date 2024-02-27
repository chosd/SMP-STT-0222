/**
 * 
 */
package com.kt.smp.stt.reprocess.dto;

import lombok.Data;

@Data
public class SttReprocessRequestDto {
	private String applicationId;
	private String recId;
	private String callStartTime;
	private String callEndTime;
	private String sttResults;
	private String description;
	private String wavFileName;
	private String serviceCode;
	private String callId;
	private String speakerType;
}
