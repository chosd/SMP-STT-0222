package com.kt.smp.stt.deploy.deploy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;
import com.kt.smp.stt.deploy.deploy.domain.SttDeployMngVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttDeploySearchResponseDto extends PageResponse {

    private final Page<SttDeployMngVO> result;

    public SttDeploySearchResponseDto(Page<SttDeployMngVO> result) {
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
