package com.kt.smp.fileutil.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jieun.chang
 * @title SttTrainDataExcelRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-034-19
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttTrainDataExcelRequestDto {

    private SttTrainDataSearchCondition searchCondition;

    private String callBackUrl;

    private String fileName;
}
