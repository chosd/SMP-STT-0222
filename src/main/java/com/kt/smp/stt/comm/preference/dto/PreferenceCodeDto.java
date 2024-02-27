/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat
public class PreferenceCodeDto {
	
	private String codeKey;
	private String codeValue;
	private String description;
	
}
