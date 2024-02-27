package com.kt.smp.stt.train.trainData.dto;

import com.kt.smp.stt.train.trainData.cache.SttTrainDataBulkCache;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @title STT 학습데이터 대량 등록 시 진행 상황 응답 DTO
 * @since 2022.05.12
 * @author jieun.chang
 * @see <pre></pre>
 */
@Getter
@Builder
public class SttTrainDataBulkProgressResDto {

	private int progressCode; // -1: 오류, 0: 진행중, 1:완료
	private String msg;

	private SttTrainDataBulkCache.ResultCode resultCode;
	private int totalCount;
	private int successCount;
	private int currCount;

	@Builder.Default
	private List<SttTrainDataBulkSaveReqDto> failList = new ArrayList<>();
}
