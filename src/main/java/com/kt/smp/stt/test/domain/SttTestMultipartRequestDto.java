package com.kt.smp.stt.test.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SttTestMultipartRequestDto {
	private String serviceCode;
	private String callbackUrl;
}
