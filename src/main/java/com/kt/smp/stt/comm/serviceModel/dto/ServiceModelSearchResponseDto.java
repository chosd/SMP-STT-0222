package com.kt.smp.stt.comm.serviceModel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.train.trainData.dto.PageResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceModelSearchResponseDto extends PageResponse {
    private final Page<ServiceModelVO> result;

	public ServiceModelSearchResponseDto(Page<ServiceModelVO> result) {
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
