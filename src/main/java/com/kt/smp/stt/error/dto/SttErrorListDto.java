package com.kt.smp.stt.error.dto;

import lombok.Data;

@Data
public class SttErrorListDto {
	private Integer id;
	private String errorTime;
	private String type;
	private String code;
	private String contents;
	private String etc;
	private String endpoint;
}
