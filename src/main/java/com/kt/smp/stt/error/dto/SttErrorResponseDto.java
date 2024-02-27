/**
 * 
 */
package com.kt.smp.stt.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.kt.smp.common.dto.PageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttErrorResponseDto extends PageResponse{
	
	private final Page<SttErrorListDto> result;
	
	public SttErrorResponseDto(Page<SttErrorListDto> result) {
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
