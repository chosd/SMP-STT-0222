package com.kt.smp.stt.statistics.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchUnit;
import com.kt.smp.stt.statistics.domain.SttStatisticsVO;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttStatisticsSearchResponseDto extends PageResponse {

    private final Page<SttStatisticsVO> result;

    private final Map<String, Page<SttStatisticsVO>> hashmap;

    private final SttStatisticsSearchUnit searchUnit;
    
    public SttStatisticsSearchResponseDto(Page<SttStatisticsVO> result, Map<String, Page<SttStatisticsVO>> hashmap, SttStatisticsSearchUnit searchUnit) {
        this.result = result;
        this.hashmap = hashmap;
        this.searchUnit = searchUnit;

        setPageInfo();
    }

    public void setPageInfo() {
        setPageNum(result.getPageNum());
        setPages(result.getPages());
        setTotal(result.getTotal());
        setPageSize(result.getPageSize());
    }
}
