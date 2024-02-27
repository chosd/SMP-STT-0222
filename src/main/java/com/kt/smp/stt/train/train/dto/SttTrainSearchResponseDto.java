package com.kt.smp.stt.train.train.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;
import com.kt.smp.stt.train.train.domain.SttTrainVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttTrainSearchResponseDto extends PageResponse {

    private final Page<SttTrainVO> result;

    public SttTrainSearchResponseDto(Page<SttTrainVO> result) {
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
