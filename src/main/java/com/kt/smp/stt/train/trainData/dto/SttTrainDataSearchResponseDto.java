package com.kt.smp.stt.train.trainData.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttTrainDataSearchResponseDto extends PageResponse {
    private final Page<SttTrainDataVO> result;

	public SttTrainDataSearchResponseDto(Page<SttTrainDataVO> result) {
		this.result = result;

		setPageInfo();
	}

	public void setPageInfo() {
		setPageNum(result.getPageNum());
		setPages(result.getPages());
		setTotal(result.getTotal());
		setPageSize(result.getPageSize());
	}
}
