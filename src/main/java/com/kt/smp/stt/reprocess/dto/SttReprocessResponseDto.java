/**
 * 
 */
package com.kt.smp.stt.reprocess.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class SttReprocessResponseDto {
	private List<String> callKeyList;
	private List<String> applicationList;
	private Integer httpStatus;
}
