package com.kt.smp.stt.error.dto;

import lombok.Data;

@Data
public class SttErrorInsertDto {
	public String type;
	public String errorCode;
	public String errorMsg;
	public String apiUrl;
	public String serverId;
	public String errorPoint;
	public long threshold;
	public long statusValue;
	public String errorTime;
}
