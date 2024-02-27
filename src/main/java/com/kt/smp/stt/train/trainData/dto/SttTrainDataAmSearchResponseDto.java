package com.kt.smp.stt.train.trainData.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttTrainDataAmSearchResponseDto extends PageResponse {
    
	private final Page<SttTrainAmDataVO> result;

	public SttTrainDataAmSearchResponseDto(Page<SttTrainAmDataVO> result) {
		
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
