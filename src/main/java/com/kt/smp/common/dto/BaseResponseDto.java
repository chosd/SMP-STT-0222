package com.kt.smp.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.stt.common.component.SttCmsResultStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @title SMP STT 응답 base response dto
 * @since 2022.02.17
 * @author soohyun
 * @see  <pre><pre>
 */
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDto<T> {
	private String resultCode;
	private String resultMsg;

	private T result;
	/**
	 * Instantiates a new Base response dto.
	 * @title 기본 생성자. 생성 시 현재시각 입력
	 * @author soohyun
	 * @since 2022.02.17
	 */


	public BaseResponseDto(SttCmsResultStatus status) {
		this.result = result;
		resultCode = SttCmsResultStatus.SUCCESS.getResultCode();
		resultMsg = SttCmsResultStatus.SUCCESS.getResultMessage();
	}

	@JsonIgnore
	public Boolean isSuccess() {
		return this.resultCode.equals(SttCmsResultStatus.SUCCESS.getResultCode());
	}


	public void setResultCodeAndMsg(SttCmsResultStatus status) {
		resultCode = status.getResultCode();
		resultMsg = status.getResultMessage();
	}
}
