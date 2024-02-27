package com.kt.smp.stt.train.trainData.domain;

/**
 * @title STT 학습데이터 대량 등록 요청 타입
 * @since 2022.05.02
 * @author jieun.chang
 * @see  <pre></pre>
 */
public enum SttTrainDataBulkProcessType {
	INSERT("등록을"), DELETE("삭제를");

	private String msg;

	SttTrainDataBulkProcessType(String name) {
		this.msg = name;
	}

	public String getMsg() {
		return msg;
	}
}
