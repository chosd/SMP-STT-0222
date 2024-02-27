package com.kt.smp.stt.comm.serviceModel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @title Base request dto. 외부 시스템에서 값을 전달할 때 전달할 때 각 필드의 값이 바뀔 수 있는 경우 사용한다.
 * @since 2022.02.21
 * @author soohyun
 * @see  <pre><pre>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequestDto {

	private Long id;
	private String reqId; // 요청자 ID (ServletRequest에서 조회)
	private String reqIp; // 요청자 IP (ServletRequest에서 조회)
}
