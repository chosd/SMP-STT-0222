package com.kt.smp.stt.deploy.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;
import com.kt.smp.stt.deploy.model.domain.SttDeployModelVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttDeployModelSearchResponseDto extends PageResponse {

    private final Page<SttDeployModelVO> result;

    public SttDeployModelSearchResponseDto(Page<SttDeployModelVO> result) {
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
