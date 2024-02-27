package com.kt.smp.fileutil.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BaseExcelRequestDto {
	private String excelFileName;
	private String callBackUrl;

	public BaseExcelRequestDto() {
	}

	public BaseExcelRequestDto(String excelFileName, String callBackUrl) {
		this.excelFileName = excelFileName;
		this.callBackUrl = callBackUrl;
	}
}
