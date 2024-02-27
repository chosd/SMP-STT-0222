package com.kt.smp.stt.verify.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyMultipartRequestDto {
	private String serviceCode;
	private String callbackUrl;
}
