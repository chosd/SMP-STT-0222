package com.kt.smp.stt.error.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.common.dto.PageParam;
import com.kt.smp.stt.common.SttSearchUnit;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttErrorSearchDto extends PageParam {
	
    private String type;

    private SttSearchUnit searchUnit = SttSearchUnit.MINUTE;

    private String from;

    private String to;
}
