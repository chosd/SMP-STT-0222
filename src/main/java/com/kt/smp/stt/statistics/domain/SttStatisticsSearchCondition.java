package com.kt.smp.stt.statistics.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.common.dto.PageParam;
import com.kt.smp.stt.common.ServiceModel;
import lombok.*;

/**
 * @author jaime
 * @title SttStatisticSearchCondition
 * @see\n <pre>
 * </pre>
 * @since 2022-07-04
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SttStatisticsSearchCondition extends PageParam {

    private Long serviceModelId;

    private String serviceCode;

    private SttStatisticsSearchUnit searchUnit = SttStatisticsSearchUnit.MINUTE;

    private String from;

    private String to;
    
    private Boolean isPagingSearch;		// 페이지 별 검색이면 true. 그래프 검색이면 false
}
