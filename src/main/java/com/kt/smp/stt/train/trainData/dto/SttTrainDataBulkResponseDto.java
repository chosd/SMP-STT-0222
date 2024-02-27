package com.kt.smp.stt.train.trainData.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * @title STT 학습데이터 대량등록 응답 DTO
 * @since 2022.05.12
 * @author jieun.chang
 * @see <pre></pre>
 */
@Getter
@Builder
public class SttTrainDataBulkResponseDto {
	private int code;
	private String msg;
	private String key; // uploadKey 값
}
